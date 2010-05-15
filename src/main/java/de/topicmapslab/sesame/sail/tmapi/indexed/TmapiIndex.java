/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.sail.tmapi.indexed;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.tmapi.core.Locator;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.sesame.sail.tmapi.TmapiStore;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiIndex {

	private TopicMapSystem tmSys;
	private SailConnection con;
	private TmapiStore tmapiStore;

	public TmapiIndex(TmapiStore tmapiStore) {
		this.tmSys = tmapiStore.getTopicMapSystem();
		this.tmapiStore = tmapiStore;
	}

	
	
	/**
	 * @return {@link TopicMapSystem}
	 */
	public TopicMapSystem getTopicMapSystem() {
		return tmSys;
	}
	
	public void index(SailConnection con) throws SailException{
		this.con  = con;
		index();
	}
	
	public void index() throws SailException{
		Iterator<TopicMapHandler> tmHandlerIterator = getTopicMapHandlers().iterator();
		TopicMapHandler topicMapHandler; 
		while (tmHandlerIterator.hasNext()) {
			topicMapHandler =  tmHandlerIterator.next();
			topicMapHandler.index();
		}
	}
	
	private Set<TopicMapHandler> getTopicMapHandlers(){
		Set<TopicMapHandler> topicMaps = new HashSet<TopicMapHandler>();
		Set<Locator> knownLocators = tmSys.getLocators();
		Iterator<Locator> locatorsIterator = knownLocators.iterator();
		while (locatorsIterator.hasNext()) {
			try {
				topicMaps.add(new TopicMapHandler(tmapiStore, locatorsIterator.next()));
			} catch (SailException e) {
				e.printStackTrace();
			}
				}
		return topicMaps;
	}


	
	/**
	 * @return the con
	 */
	public SailConnection getCon() {
		return con;
	}

}
