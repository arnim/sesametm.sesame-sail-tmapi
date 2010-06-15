/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import info.aduna.iteration.LookAheadIteration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;

/**
 * @author Arnim Bleier
 * 
 */
public class TmapiStatementIterator<X extends Exception> extends
		LookAheadIteration<Statement, X> {

	private volatile int statementIdx = -1;
	private ValueFactory valueFactory;
	private Set<Statement> statements;
	private TmapiStatementFactory statementFactory;
	private Iterator<Statement> iterator;

	public TmapiStatementIterator(Sail tmapiStore, Locator subj, Locator pred,
			Locator obj, TopicMap... topicMaps) {

		this.valueFactory = tmapiStore.getValueFactory();
		this.statements = new HashSet<Statement>();
		this.statementFactory = new TmapiStatementFactory(valueFactory);

		try {
			forTopicMpas(subj, pred, obj, topicMaps);
		} catch (SailException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.iterator = statements.iterator();
	}

	@Override
	protected Statement getNextElement() {
		statementIdx++;
		if (iterator.hasNext())
			return iterator.next();
		return null;
	}
	
	public Set<Statement> getStatements(){
		return statements;
	}
	
	public TmapiStatementFactory getStatementFactory(){
		return statementFactory;
	}

	private void forTopicMpas(Locator subj, Locator pred, Locator obj,
			TopicMap... topicMaps) throws SailException, InterruptedException {
		Topic sTopic = null, pTopic = null, oTopic = null;		
		for (TopicMap tm : topicMaps) {

			sTopic = getTopic(subj, tm);
			pTopic = getTopic(pred, tm);
			oTopic = getTopic(obj, tm);

			new MultiLocatorHandler(subj, pred, obj, tm, this).evaluate();
			
//			new SeeAlsoHandler(subj, pred, obj, tm, this).evaluate();

		
			
			if (sTopic == null && subj != null || pred != null && (pTopic == null && !RDF.TYPE.toString().equals(pred.toExternalForm()) )
				 || oTopic == null && obj != null) {
				

				// Q has no match in this tm
			} else {
				if (pTopic == null && pred != null && RDF.TYPE.toString().equals(pred.toExternalForm()))
					createTypeList(sTopic, oTopic, tm);
				else if (sTopic == null && pTopic == null && oTopic == null)
					createListXXX(tm);
				else if (sTopic != null && pTopic == null && oTopic == null)
					createListSXX(sTopic, tm);
				else if (sTopic != null && pTopic != null && oTopic == null)
					createListSPX(sTopic, pTopic, tm);
				else if (sTopic == null && pTopic != null && oTopic == null)
					createListXPX(pTopic, tm);
				else if (sTopic != null && pTopic != null && oTopic != null)
					createListSPO(sTopic, pTopic, oTopic, tm);
				else if (sTopic == null && pTopic == null && oTopic != null)
					createListXXO(oTopic, tm);
				else if (sTopic == null && pTopic != null && oTopic != null)
					createListXPO(pTopic, oTopic, tm);
				else
					System.err
							.println("You should never read this! TmapiStatementIterator:104 ");
			}
		}

	}
	
	
	private void createTypeList(Topic sTopic, Topic oTopic, TopicMap tm){
		
		if (sTopic == null && oTopic == null)
			createTypeListxPx(tm);
		else if (sTopic != null && oTopic == null )
			createTypeListSPX(sTopic, tm);
		else if (sTopic == null && oTopic != null)
			createTypeListXPO(oTopic, tm);
		else if (sTopic != null && oTopic != null)
			createTypeListSPO(sTopic, oTopic, tm);
		else
			System.err
					.println("You should never read this! TmapiStatementIterator:104 ");
		
		

	}
	
	private void createTypeListxPx(TopicMap tm){
		Iterator<Topic> topicsIterator = tm.getTopics().iterator();
		while (topicsIterator.hasNext()) {
			createTypeListSPX(topicsIterator.next(), tm);
		}
	}
	
	private void createTypeListSPX(Topic sTopic, TopicMap tm){
		Iterator<Topic> typesIterator = sTopic.getTypes().iterator();
		while (typesIterator.hasNext()) {
			statements.add(statementFactory.create(sTopic, RDF.TYPE, typesIterator.next()));
		}
	}
	
	private void createTypeListXPO(Topic oTopic, TopicMap tm){
		Iterator<Topic> subjectIterator = tm.getIndex(TypeInstanceIndex.class).getTopics(oTopic).iterator();
		while (subjectIterator.hasNext()) {
			statements.add(statementFactory.create(subjectIterator.next(), RDF.TYPE, oTopic));	
		}
	}
	
	private void createTypeListSPO(Topic sTopic, Topic oTopic, TopicMap tm){
		Topic objectTopic;
		Iterator<Topic> typesIterator = sTopic.getTypes().iterator();
		while (typesIterator.hasNext()) {
			objectTopic =  typesIterator.next();
			if (objectTopic.equals(oTopic))
				statements.add(statementFactory.create(sTopic, RDF.TYPE, objectTopic));
		}
	}
	
	
	

	
	private void createListXXX(TopicMap tm) throws SailException {
		Iterator<Topic> tIterator = tm.getTopics().iterator();
		while (tIterator.hasNext()) {
			createListSXX(tIterator.next(), tm);
		}
	}

	private void createListSXX(Topic subj, TopicMap tm) throws SailException {
		addCharacteristics(subj);
		createTypeListSPX(subj, tm);
		Role subjectRole, objectRole;
		Iterator<Role> subjectRolesIterator = subj.getRolesPlayed().iterator(), objectsRolesIterator;
		while (subjectRolesIterator.hasNext()) {
			subjectRole = subjectRolesIterator.next();
			objectsRolesIterator = subjectRole.getParent().getRoles().iterator();
			while (objectsRolesIterator.hasNext()) {
				objectRole = objectsRolesIterator.next();
				if (!subjectRole.getType().equals(objectRole.getType())) {
					statements.add(statementFactory.create(subj, objectRole
							.getType(), objectRole.getPlayer()));
				}
			}
		}

	}

	private void createListSPX(Topic subj, Topic pred, TopicMap tm)
			throws SailException {
		addCharacteristics(subj, pred);
		Topic subjectRoleType;
		Role subjectRole, objectRole;
		Iterator<Role> subjectRolesIterator = subj.getRolesPlayed().iterator(), ojectRolesIterator;
		while (subjectRolesIterator.hasNext()) {
			subjectRole = subjectRolesIterator.next();
			subjectRoleType = subjectRole.getType();
			ojectRolesIterator = subjectRole.getParent().getRoles().iterator();
			while (ojectRolesIterator.hasNext()) {
				objectRole = ojectRolesIterator.next();
				if (!subjectRoleType.equals(objectRole.getType())
						&& objectRole.getType().equals(pred)) {
					statements.add(statementFactory.create(subj, objectRole
							.getType(), objectRole.getPlayer()));
				}
			}
		}
	}

	private void createListXPX(Topic pred, TopicMap tm) throws SailException {
		Iterator<Topic> tIterator = tm.getTopics().iterator();
		while (tIterator.hasNext()) {
			createListSPX(tIterator.next(), pred, tm);
		}
	}

	private void createListSPO(Topic subj, Topic pred, Topic obj, TopicMap tm)
			throws SailException {
		Role subjectRole, objectRole;
		Iterator<Role> subjectRolesIterator, objectRolesIterator = obj.getRolesPlayed().iterator();
		while (objectRolesIterator.hasNext()) {
			objectRole = objectRolesIterator.next();
			if (objectRole.getType().equals(pred)){
				subjectRolesIterator = objectRole.getParent().getRoles().iterator();
				while (subjectRolesIterator.hasNext()) {
					subjectRole = subjectRolesIterator.next();
					if (subjectRole.getPlayer().equals(subj))
						statements.add(statementFactory.create(subj, pred, obj));
				}
			}
			
			
		}
		
		
		
		
	}

	private void createListXXO(Topic obj, TopicMap tm) throws SailException {
		createTypeListXPO(obj, tm);
		Role objectRole, subjectRole;
		Topic objectRoleType;
		Iterator<Role> subjectRoleIterator;
		Iterator<Role> objectRolesIterator = obj.getRolesPlayed().iterator();
		while (objectRolesIterator.hasNext()) {
			objectRole = objectRolesIterator.next();
			objectRoleType = objectRole.getType();

			subjectRoleIterator = objectRole.getParent().getRoles().iterator();
			while (subjectRoleIterator.hasNext()) {
				subjectRole = subjectRoleIterator.next();
				if (!subjectRole.getType().equals(objectRoleType)) {
					statements.add(statementFactory.create(subjectRole
							.getPlayer(), objectRoleType, obj));
				}
			}

		}
	}

	private void createListXPO(Topic pred, Topic obj, TopicMap tm)
			throws SailException {
		Iterator<Role> objectRolesIterator = obj.getRolesPlayed(pred)
				.iterator(), sbjectRolesIterator;
		Role objectRole, subjectRole;
		while (objectRolesIterator.hasNext()) {
			objectRole = objectRolesIterator.next();
			sbjectRolesIterator = objectRole.getParent().getRoles().iterator();
			while (sbjectRolesIterator.hasNext()) {
				subjectRole = sbjectRolesIterator.next();
				if (!subjectRole.getType().equals(pred))
					statements.add(statementFactory.create(subjectRole
							.getPlayer(), pred, obj));
			}

		}
	}

	private void addCharacteristics(Topic t) {
		Iterator<Name> names = t.getNames().iterator();
		while (names.hasNext()) {
			statements.add(statementFactory.create(names.next()));
		}
		Iterator<Occurrence> occurrences = t.getOccurrences().iterator();
		while (occurrences.hasNext()) {
			statements.add(statementFactory.create(occurrences.next()));
		}
	}

	private void addCharacteristics(Topic t, Topic type) {
		Iterator<Name> names = t.getNames(type).iterator();
		while (names.hasNext()) {
			statements.add(statementFactory.create(names.next()));
		}
		Iterator<Occurrence> occurrences = t.getOccurrences(type).iterator();
		while (occurrences.hasNext()) {
			statements.add(statementFactory.create(occurrences.next()));
		}
	}


	Topic getTopic(Locator l, TopicMap tm) {
		if (l == null)
			return null;
		Topic t = null;
		t = tm.getTopicBySubjectIdentifier(l);
		if (t != null)
			return t;
		t = tm.getTopicBySubjectLocator(l);
		if (t != null)
			return t;
		try {
			t = (Topic) tm.getConstructByItemIdentifier(l);
		} catch (ClassCastException e) {
			// Found Construct is not a Topic
		}
		return t;
	}

}
