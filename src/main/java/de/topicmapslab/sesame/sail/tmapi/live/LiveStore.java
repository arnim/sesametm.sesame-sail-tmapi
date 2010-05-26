/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.live;

import info.aduna.concurrent.locks.Lock;
import info.aduna.concurrent.locks.ReadPrefReadWriteLockManager;
import info.aduna.concurrent.locks.ReadWriteLockManager;
import info.aduna.iteration.LookAheadIteration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailBase;
import org.openrdf.sail.memory.model.MemValueFactory;
import org.tmapi.core.Locator;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.sesame.sail.tmapi.CONFIG;
import de.topicmapslab.sesame.sail.tmapi.utils.TmapiStatementIterator;
import de.topicmapslab.sesame.sail.tmapi.utils.TmqlStatementIterator;

/**
 * @author Arnim Bleier
 * 
 */
public class LiveStore extends SailBase {

	/**
	 * Factory/cache for TmapiValue objects.
	 */
	private MemValueFactory valueFactory = new MemValueFactory();

	private TopicMapSystem tmSystem;

	/**
	 * Lock manager used to give the snapshot cleanup thread exclusive access to
	 * the statement list.
	 */
	private final ReadWriteLockManager statementListLockManager = new ReadPrefReadWriteLockManager(
			debugEnabled());

	private String config;

	/**
	 * 
	 * @param tmSys
	 * @param config
	 */
	public LiveStore(TopicMapSystem tmSys, String config) {
		this.setTmSystem(tmSys);
		this.setConfig(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.helpers.SailBase#getConnectionInternal()
	 */
	protected SailConnection getConnectionInternal() throws SailException {
		return new TmapiSailConnection(this);
	}

	protected void shutDownInternal() throws SailException {
		System.out.println("shutDownInternal");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.Sail#getValueFactory()
	 */
	public ValueFactory getValueFactory() {
		return valueFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.Sail#isWritable()
	 */
	public boolean isWritable() throws SailException {
		return false;
	}

	public void setTmSystem(TopicMapSystem tmSystem) {
		this.tmSystem = tmSystem;
	}

	public TopicMapSystem getTmSystem() {
		return tmSystem;
	}

	public <X extends Exception> LookAheadIteration<Statement, X> createStatementIterator(
			Class<X> class1, Resource subj, URI pred, Value obj,
			boolean includeInferred, Resource[] contexts) {

		TopicMap[] relevantMSs = getTopicMaps(contexts);
		Locator subject = null, predicate = null, object = null;

		subject = getLocator(subj);
		predicate = getLocator(pred);
		object = getLocator(obj);

		if (config.equals(CONFIG.TMQL))
			return new TmqlStatementIterator<X>(this, subject, predicate, object,
					relevantMSs);
		return new TmapiStatementIterator<X>(this, subject, predicate, object,
				relevantMSs);
	}

	/**
	 * 
	 * 
	 * @param contexts
	 * @return A List of {@link TopicMap} to be queried.
	 */
	private TopicMap[] getTopicMaps(Resource... contexts) {
		LinkedList<TopicMap> topicMpas = new LinkedList<TopicMap>();
		Set<Locator> knownLocators = tmSystem.getLocators();
		Locator l;
		if (contexts.length > 0) {
			HashSet<Locator> relevantLocators = new HashSet<Locator>();
			for (Resource context : contexts) {
				l = getLocator(context);
				if (knownLocators.contains(l))
					relevantLocators.add(l);
			}
			knownLocators = relevantLocators;
		}
		Iterator<Locator> locatorsIterator = knownLocators.iterator();
		while (locatorsIterator.hasNext()) {
			topicMpas.add(tmSystem.getTopicMap(locatorsIterator.next()));
		}
		return (TopicMap[]) topicMpas.toArray(new TopicMap[topicMpas.size()]);
	}

	/**
	 * 
	 * @param v
	 *            {@link Value}
	 * @return The {@link Locator} representation of the given {@link Value}
	 */
	private Locator getLocator(Value v) {
		try {
			return tmSystem.createLocator(v.stringValue());
		} catch (Exception e) {
			return null;
		}
	}

	public Lock getStatementsReadLock() throws SailException {
		try {
			return statementListLockManager.getReadLock();
		} catch (InterruptedException e) {
			throw new SailException(e);
		}
	}

	public void initialize() throws SailException {

	}

	/**
	 * @param config
	 *            the config to set
	 */
	public void setConfig(String config) {
		this.config = config;
	}

	/**
	 * @return the config
	 */
	public String getConfig() {
		return config;
	}

}
