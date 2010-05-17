/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.sail.tmapi;


import java.io.File;
import java.util.Iterator;

import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;
import org.tmapi.core.Locator;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.sesametm.sail.tmapi.live.LiveStore;
import de.topicmapslab.sesametm.sail.tmapi.utils.TmapiStatementIterator;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStore implements Sail {

	final String READ_ONLY_MESSAGE = "sail is read-only";
	private TopicMapSystem tmSys;
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
		if (config == CONFIG.INDEXED){
			
			Iterator<Locator> locatorsIterator = tmSys.getLocators().iterator();
			Locator l;

			while (locatorsIterator.hasNext()) {
				try {
					l = locatorsIterator.next();
					TmapiStatementIterator si = new TmapiStatementIterator( this, (Locator) null, (Locator) null, (Locator) null,  tmSys.getTopicMap(l));
					while (si.hasNext()) {
						 Statement statement = (Statement) si.next();
						 getConnection().addStatement(statement.getSubject(), statement.getPredicate(), statement.getObject(), getValueFactory().createURI(l.toExternalForm()));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		else
			throw new SailException();
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
		if (config == CONFIG.INDEXED)
			index();
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
