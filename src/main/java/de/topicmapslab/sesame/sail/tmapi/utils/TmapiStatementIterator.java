/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import info.aduna.iteration.LookAheadIteration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

	
	private TopicMap[] topicMaps;
	private volatile int statementIdx = -1;
	private Locator subj, pred, obj;
	private ValueFactory valueFactory;
	private List<Statement> statements;
	private TmapiStatementFactory statementFactory;
	private Iterator<Statement> iterator;

	


	public TmapiStatementIterator(Sail tmapiStore, Locator resource, Locator uri, Locator value,
			TopicMap... contexts) {

		this.subj = resource;
		this.pred = uri;
		this.obj = value;
		this.topicMaps = contexts;
		this.valueFactory = tmapiStore.getValueFactory();
		this.statements = new LinkedList<Statement>();
		this.statementFactory = new TmapiStatementFactory(valueFactory);

		forTopicMpas();

		this.iterator = statements.iterator();
	}





	@Override
	protected Statement getNextElement() {
		statementIdx++;
		if (iterator.hasNext())
			return iterator.next();
		return null;
	}
	
	private void forTopicMpas(){
		for (TopicMap tm : topicMaps)
			try {
				createList(tm);
			} catch (Exception e) {
				e.printStackTrace();
			}

	}
	
	
	private void createList(TopicMap tm) throws SailException{
		Iterator<Topic> tIterator = tm.getTopics().iterator();
		Topic topic;
		Iterator<Role> thisRolesIterator, otherRolesIterator;
		Role thisRole, otherRole;

		while (tIterator.hasNext()) {
			topic = tIterator.next();
			addCharacteristics(topic);
			thisRolesIterator = topic.getRolesPlayed().iterator();
			while (thisRolesIterator.hasNext()) {
				thisRole = thisRolesIterator.next();
				otherRolesIterator = thisRole.getParent().getRoles().iterator();
				while (otherRolesIterator.hasNext()) {
					otherRole = otherRolesIterator.next();
					if (!thisRole.getType().equals(otherRole.getType())) {
						statements.add(statementFactory.create(topic, otherRole.getType(), otherRole.getPlayer()));
					}
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
	
	


}
