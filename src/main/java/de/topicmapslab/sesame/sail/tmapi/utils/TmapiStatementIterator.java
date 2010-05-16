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
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;


/**
 * @author Arnim Bleier
 *
 */
public class TmapiStatementIterator <X extends Exception> extends LookAheadIteration<Statement, X> {

	
	private volatile int statementIdx = -1;
	private ValueFactory valueFactory;
	private Set<Statement> statements;
	private TmapiStatementFactory statementFactory;
	private Iterator<Statement> iterator;


	public TmapiStatementIterator(Sail tmapiStore, Locator subj, Locator pred, Locator obj,
			TopicMap... topicMaps) {


		this.valueFactory = tmapiStore.getValueFactory();
		this.statements = new HashSet<Statement>();
		this.statementFactory = new TmapiStatementFactory(valueFactory);
		
		
		try {
			forTopicMpas(subj, pred, obj, topicMaps);
		} catch (SailException e) {
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
	

	private void forTopicMpas(Locator subj, Locator pred, Locator obj,
			TopicMap... topicMaps) throws SailException{
		Topic sTopic = null, pTopic = null, oTopic = null;
		for (TopicMap tm : topicMaps){

			System.out.println("for " + subj + " || " + pred + " || " + obj + " || " );

			
			sTopic = getTopic(subj, tm);
			pTopic = getTopic(pred, tm);
			oTopic = getTopic(obj, tm);
			
			
			if (	sTopic == null && subj != null ||
					pTopic == null && pred != null ||
					oTopic == null && obj != null ) {
			
				// Q has no match in this tm
			} else {
			
				if (sTopic == null && pTopic == null && oTopic == null)
					createListXXX(tm);
				else if (sTopic != null && pTopic == null && oTopic == null)
					createListSXX(sTopic, tm);
				else if (sTopic != null && pTopic != null && oTopic == null)
					createListSPX(sTopic, pTopic, tm);
				else if (sTopic == null && pTopic != null && oTopic == null)
					createListXPX(pTopic, tm);
				else if (subj != null && pred != null && oTopic == null)
					createListSPO(sTopic, pTopic, oTopic, tm);
				
			}
		}
	}
	
	
	private void createListXXX(TopicMap tm) throws SailException{
		Iterator<Topic> tIterator = tm.getTopics().iterator();
		while (tIterator.hasNext()) {
			createListSXX(tIterator.next(), tm);
		}
	}
	

	private void createListSXX(Topic subj , TopicMap tm) throws SailException {
		addCharacteristics(subj);
		Role thisRole, otherRole;
		Iterator<Role> thisRolesIterator = subj.getRolesPlayed().iterator(), otherRolesIterator;
		while (thisRolesIterator.hasNext()) {
			thisRole = thisRolesIterator.next();
			otherRolesIterator = thisRole.getParent().getRoles().iterator();
			while (otherRolesIterator.hasNext()) {
				otherRole = otherRolesIterator.next();
				if (!thisRole.getType().equals(otherRole.getType())) {
					statements.add(statementFactory.create(subj, otherRole.getType(), otherRole.getPlayer()));
				}
			}
		}
		
	}
	
	private void createListSPX(Topic subj , Topic pred, TopicMap tm) throws SailException {
		addCharacteristics(subj, pred);
		Topic thisRoleType;
		Role thisRole, otherRole;
		Iterator<Role> thisRolesIterator = subj.getRolesPlayed().iterator(), otherRolesIterator;
		while (thisRolesIterator.hasNext()) {
			thisRole = thisRolesIterator.next();			
			thisRoleType = thisRole.getType();
			otherRolesIterator = thisRole.getParent().getRoles().iterator();
			while (otherRolesIterator.hasNext()) {
				otherRole = otherRolesIterator.next();
				if (!thisRoleType.equals(otherRole.getType()) && otherRole.getType().equals(pred)) {					
					statements.add(statementFactory.create(subj, otherRole.getType(), otherRole.getPlayer()));
				}
			}
		}
	}
	
	private void createListXPX(Topic pred , TopicMap tm) throws SailException {
		Iterator<Topic> tIterator = tm.getTopics().iterator();
		while (tIterator.hasNext()){
			createListSPX(tIterator.next(), pred, tm);
		}
	}
	
	private void createListSPO(Topic subj , Topic pred , Topic obj, TopicMap tm) throws SailException {
		Role thisRole, otherRole;
		Iterator<Role> thisRolesIterator = subj.getRolesPlayed().iterator(), otherRolesIterator;
		while (thisRolesIterator.hasNext()) {
			thisRole = thisRolesIterator.next();
			otherRolesIterator = thisRole.getParent().getRoles().iterator();
			while (otherRolesIterator.hasNext()) {
				otherRole = otherRolesIterator.next();
				if (!thisRole.getType().equals(otherRole.getType())
						&& otherRole.getType().equals(pred)
						&& otherRole.getPlayer().equals(obj)
				) {
					statements.add(statementFactory.create(subj, otherRole.getType(), otherRole.getPlayer()));
				}
			}
		}
	}
	
	
	private void addCharacteristics(Topic t){
		Iterator<Name> names = t.getNames().iterator();
		while (names.hasNext()) {
			statements.add(statementFactory.create(names.next()));
		}
		Iterator<Occurrence> occurrences = t.getOccurrences().iterator();
		while (occurrences.hasNext()) {
			statements.add(statementFactory.create(occurrences.next()));
		}
	}
	
	private void addCharacteristics(Topic t, Topic type){
		Iterator<Name> names = t.getNames(type).iterator();
		while (names.hasNext()) {
			statements.add(statementFactory.create(names.next()));
		}
		Iterator<Occurrence> occurrences = t.getOccurrences(type).iterator();
		while (occurrences.hasNext()) {
			statements.add(statementFactory.create(occurrences.next()));
		}
	}
	
	private Topic getTopic(Locator l, TopicMap tm){
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
