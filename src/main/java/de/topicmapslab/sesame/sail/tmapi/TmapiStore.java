/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;


import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;
import org.tmapi.core.TopicMapSystem;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStore extends MemoryStore {

	final String READ_ONLY_MESSAGE = "sail is read-only";
	private TopicMapSystem tmSys;
	private TmapiIndex indexer;
	
	
	public TmapiStore(TopicMapSystem tmSys){
		this.tmSys = tmSys;
		this.indexer = new TmapiIndex(this);
	}
	
	/**
	 * Indexes this repository. 
	 * 
	 * @throws SailException
	 *         when indexing of the store failed.
	 */
	protected void index()
		throws SailException{
		logger.debug("Indexing TmapiStore...");
		indexer.index();
	}
	

	public void setPersist(boolean persist){
		throw new IllegalStateException(READ_ONLY_MESSAGE);
	}


	/**
	 * @return {@link TopicMapSystem}
	 */
	public TopicMapSystem getTopicMapSystem() {
		return tmSys;
	}



}
