/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.tmapi2tm;

import info.aduna.concurrent.locks.Lock;
import info.aduna.concurrent.locks.ReadPrefReadWriteLockManager;
import info.aduna.concurrent.locks.ReadWriteLockManager;
import info.aduna.iteration.CloseableIteration;
import info.aduna.iteration.EmptyIteration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ContextStatementImpl;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailBase;
import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.Locator;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.sesametm.tmapi2tm.model.SailTopic;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStore extends SailBase {
	
	
	
	/**
	 * Factory/cache for TmapiValue objects.
	 */
	private TmapiValueFactory valueFactory = new TmapiValueFactory();
	
	private TopicMapSystem tmSystem;
	
	
	/**
	 * Lock manager used to give the snapshot cleanup thread exclusive access to
	 * the statement list.
	 */
	private final ReadWriteLockManager statementListLockManager = new ReadPrefReadWriteLockManager(
			debugEnabled());

	/**
	 * @throws TMAPIException 
	 * @throws FactoryConfigurationException 
	 * 
	 */
	public TmapiStore() throws FactoryConfigurationException, TMAPIException {
		setTmSystem(TopicMapSystemFactory.newInstance().newTopicMapSystem());
	}
	
	/**
	 * 
	 * @param tmSys
	 */
	public TmapiStore(TopicMapSystem tmSys){
		this.setTmSystem(tmSys);
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailBase#getConnectionInternal()
	 */
	protected SailConnection getConnectionInternal() throws SailException {
		return new TmapiSailConnection(this);
	}


	protected void shutDownInternal() throws SailException {
		System.out.println("shutDownInternal");
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.Sail#getValueFactory()
	 */
	public ValueFactory getValueFactory() {
		return valueFactory;
	}

	/* (non-Javadoc)
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
	
	protected <X extends Exception> CloseableIteration<ContextStatementImpl, X> createStatementIterator(
			Class<X> class1, Resource subj, URI pred,
			Value obj, boolean includeInferred, Resource[] contexts) {
		
		TopicMap[] relevantMSs = getTopicMaps(contexts);
		Locator l;
		SailTopic subject = null, predicate = null, object = null;
		
		l = getLocator(subj);
		if (l != null) {
			try {
				subject = new SailTopic(l, relevantMSs);
			} catch (SailException e) {
				return new EmptyIteration<ContextStatementImpl, X>();
			}
		}
		
		l = getLocator(pred);
		if (l != null) {
			try {
				predicate = new SailTopic(l, relevantMSs);
			} catch (SailException e) {
				return new EmptyIteration<ContextStatementImpl, X>();
			}
		}
		
		l = getLocator(obj);
		if (l != null) {
			try {
				object = new SailTopic(l, relevantMSs);
			} catch (SailException e) {
				return new EmptyIteration<ContextStatementImpl, X>();
			}
		}

		return new TmapiStatementIterator<X>(this, subject, predicate, object, includeInferred, relevantMSs);
	}
	
	/**
	 * 
	 * 
	 * @param contexts
	 * @return	A List of {@link TopicMap} to be queried.
	 */
	private TopicMap[] getTopicMaps(Resource... contexts){
		LinkedList<TopicMap> topicMpas = new LinkedList<TopicMap>();
		Set<Locator> knownLocators = tmSystem.getLocators();
		Locator l;
		if (contexts.length > 0){
			HashSet<Locator> relevantLocators = new HashSet<Locator>();
			for (Resource context :contexts){
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
		return (TopicMap[])topicMpas.toArray(new TopicMap[topicMpas.size()]);
	}
	
	/**
	 * 
	 * @param v {@link Value}
	 * @return The {@link Locator} representation of the given {@link Value}
	 */
	private Locator getLocator(Value v){
		try {
			return tmSystem.createLocator(v.stringValue());
		} catch (Exception e) {
			return null;
		}
	}
	

	protected Lock getStatementsReadLock()
	throws SailException
{
	try {
		return statementListLockManager.getReadLock();
	}
	catch (InterruptedException e) {
		throw new SailException(e);
	}
}


	

}
