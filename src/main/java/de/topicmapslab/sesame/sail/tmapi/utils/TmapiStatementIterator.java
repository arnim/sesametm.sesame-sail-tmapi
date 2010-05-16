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
		
		
		forTopicMpas(subj, pred, obj, topicMaps);

		this.iterator = statements.iterator();
	}


	private void forTopicMpas(Locator subj, Locator pred, Locator obj,
			TopicMap... topicMaps){
		for (TopicMap tm : topicMaps)
			try {
				if (subj == null && pred == null && obj == null)
					createListXXX(tm);
				else if (subj != null && pred == null && obj == null)
					createListSXX(subj, tm);
				else if (subj != null && pred != null && obj == null)
					createListSPX(subj, pred, tm);
				else if (subj == null && pred != null && obj == null)
					createListXPX(pred, tm);
				else if (subj != null && pred != null && obj == null)
					createListSPO(subj, pred, obj, tm);
				else // this code should never be reached
					throw new RuntimeException();
			} catch (Exception e) {
				e.printStackTrace();
			}

	}
	
	
	private void createListXXX(TopicMap tm) throws SailException{
		Iterator<Topic> tIterator = tm.getTopics().iterator();

		while (tIterator.hasNext()) {
			createListSXX(tIterator.next(), tm);
		}
	}
	
	private void createListSXX(Locator subj , TopicMap tm) throws SailException {
		Topic t = getTopic(subj, tm);
		if (t != null)
			createListSXX(t, tm);
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
	
	private void createListSPX(Locator subj , Locator pred, TopicMap tm) throws SailException {
		
	}
	
	private void createListXPX(Locator pred , TopicMap tm) throws SailException {
		
	}
	
	private void createListSPO(Locator subj , Locator pred , Locator obj, TopicMap tm) throws SailException {
		
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
	
	

	@Override
	protected Statement getNextElement() {
		statementIdx++;
		if (iterator.hasNext())
			return iterator.next();
		return null;
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
