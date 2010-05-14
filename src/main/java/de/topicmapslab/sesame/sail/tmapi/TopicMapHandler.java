/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import java.util.Iterator;
import java.util.Set;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

/**
 * @author Arnim Bleier
 *
 */
public class TopicMapHandler {

	private TopicMap tm;
	private Set<Topic> topics;
	private TmapiStore tmapiStore;
	private SailConnection con;
	private ValueFactory valueFactory;
	private URI context;
	private TmapiStatementHandler statementFactory;

	/**
	 * @throws SailException 
	 * 
	 */
	public TopicMapHandler(TmapiStore tmapiStore, Locator l) throws SailException {
		this.tmapiStore = tmapiStore;
		this.tm = tmapiStore.getTopicMapSystem().getTopicMap(l);
		this.con = tmapiStore.getConnection();
		this.valueFactory = this.tmapiStore.getValueFactory();
		this.context = this.valueFactory.createURI(l.toExternalForm());
		this.statementFactory = new TmapiStatementHandler(valueFactory, con, context);
	}
	
	public void index() throws SailException{
		this.topics = tm.getTopics();
		Iterator<Topic> tIterator = topics.iterator();
		Topic topic;
		while (tIterator.hasNext()) {
			topic = tIterator.next();
			addCharacteristics(topic);
		}
	}
	
	private void addCharacteristics(Topic t){
		Iterator<Name> names = t.getNames().iterator();
		while (names.hasNext()) {
			statementFactory.add(names.next());
		}
		Iterator<Occurrence> occurrences = t.getOccurrences().iterator();
		while (occurrences.hasNext()) {
			statementFactory.add(occurrences.next());
		}
	}
	

}
