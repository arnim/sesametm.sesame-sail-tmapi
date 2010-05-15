/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;


import java.io.File;

import org.openrdf.model.ValueFactory;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.sesame.sail.tmapi.indexed.TmapiIndex;
import de.topicmapslab.sesame.sail.tmapi.live.LiveStore;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStore implements Sail {

	final String READ_ONLY_MESSAGE = "sail is read-only";
	private TopicMapSystem tmSys;
	private TmapiIndex indexer;
	private Sail store;
	private SailConnection con = null;
	private String config;
	
	
	public TmapiStore(TopicMapSystem tmSys) throws SailException{
		this(tmSys, CONFIG.INDEXED);
	}
	
	public TmapiStore(TopicMapSystem tmSys, String config) throws SailException{
		this.tmSys = tmSys;
		this.config = config;
		setup();
	}
	
	private void setup(){
		if (config == CONFIG.INDEXED) {
			this.indexer = new TmapiIndex(this);
			this.store = new MemoryStore();
		}
		if (config == CONFIG.LIVE) {
			this.store = new LiveStore(tmSys);
		}
	}
	
	public String getConfiguration(){
		return config;
	}
	
	/**
	 * Indexes this repository. 
	 * 
	 * @throws SailException
	 *         when indexing of the store failed.
	 */
	protected void index()
		throws SailException{
		indexer.index(getConnection());
	}
	
	/**
	 * @return {@link TopicMapSystem}
	 */
	public TopicMapSystem getTopicMapSystem() {
		return tmSys;
	}

	public SailConnection getConnection() throws SailException {
		if (con != null)
			return con;
		con = store.getConnection();
		return con;
	}

	public File getDataDir() {
		return null;
	}

	public ValueFactory getValueFactory() {
		return store.getValueFactory();
	}

	public void initialize() throws SailException {
		store.initialize();
	}

	public boolean isWritable() throws SailException {
		return false;
	}

	public void setDataDir(File arg0) {		
	}

	public void shutDown() throws SailException {
		store.shutDown();
	}



}
