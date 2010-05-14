/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import java.util.Iterator;
import java.util.Set;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.NotifyingSailConnection;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.model.MemValueFactory;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

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
	}
	
	public void index() throws SailException{
		this.topics = tm.getTopics();
		Iterator<Topic> tIterator = topics.iterator();
		Topic topic;
		con.addStatement(valueFactory.createURI("http://www.google.com/1"), valueFactory.createURI("http://www.google.com/1"), valueFactory.createURI("http://www.google.com/1"), context);

		while (tIterator.hasNext()) {
			topic = tIterator.next();
//			System.out.println(topic);
		}
	}

}
