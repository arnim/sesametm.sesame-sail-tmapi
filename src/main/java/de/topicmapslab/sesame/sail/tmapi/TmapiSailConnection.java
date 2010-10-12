/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import info.aduna.concurrent.locks.Lock;
import info.aduna.iteration.CloseableIteration;
import info.aduna.iteration.CloseableIteratorIteration;
import info.aduna.iteration.LockingIteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.NamespaceImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.Dataset;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.TripleSource;
import org.openrdf.query.algebra.evaluation.impl.EvaluationStrategyImpl;
import org.openrdf.query.impl.EmptyBindingSet;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailConnectionBase;
import org.tmapi.core.Locator;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapExistsException;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.sesame.sail.tmapi.utils.TMAPIStatementWriter;



/**
 * @author Arnim Bleier
 * 
 */
public class TmapiSailConnection extends SailConnectionBase {

	private TopicMapSystem tmSystem;
	protected TmapiStore store;

	/**
	 * @param sailBase
	 */
	public TmapiSailConnection(TmapiStore sailBase) {
		super(sailBase);
		this.store = sailBase;
		tmSystem = ((TmapiStore) sailBase).getTmSystem();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#addStatementInternal(org.
	 * openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value,
	 * org.openrdf.model.Resource[])
	 */
	@Override
	protected void addStatementInternal(Resource subject, URI predicate, Value object,
			Resource... contexts) throws SailException {
		for (Resource resource : contexts) {
			new TMAPIStatementWriter(subject, predicate, object, getTopicMaps(resource)).write();
		}
		
	}
	
	
	
	/**
	 * 
	 * 
	 * @param context
	 * @return Gets or creates a {@link TopicMap}.
	 */
	private TopicMap getTopicMaps(Resource context) {
		TopicMap result;
		result = tmSystem.getTopicMap(context.stringValue());
		if (result != null)
			return result;
		try {
			return tmSystem.createTopicMap(context.stringValue());
		} catch (TopicMapExistsException e) {
			e.printStackTrace();
		}
		return result;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#clearInternal(org.openrdf
	 * .model.Resource[])
	 */
	@Override
	protected void clearInternal(Resource... arg0) throws SailException {
		System.err.println(2);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#clearNamespacesInternal()
	 */
	@Override
	protected void clearNamespacesInternal() throws SailException {
		// No Namespace handling
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.helpers.SailConnectionBase#closeInternal()
	 */
	@Override
	protected void closeInternal() throws SailException {
		// Always open
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.helpers.SailConnectionBase#commitInternal()
	 */
	@Override
	protected void commitInternal() throws SailException {
		// No bulk operations
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#evaluateInternal(org.openrdf
	 * .query.algebra.TupleExpr, org.openrdf.query.Dataset,
	 * org.openrdf.query.BindingSet, boolean)
	 */
	@Override
	protected CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluateInternal(
			TupleExpr tupleExpr, Dataset dataset, BindingSet arg2, boolean includeInferred)
			throws SailException {
		tupleExpr = tupleExpr.clone();
		TripleSource tripleSource = new TmapiTripleSource(includeInferred);
		EvaluationStrategyImpl strategy = new EvaluationStrategyImpl(tripleSource, dataset);
		Lock stLock = store.getStatementsReadLock();
		CloseableIteration<BindingSet, QueryEvaluationException> iter;
		try {
			iter = strategy.evaluate(tupleExpr, EmptyBindingSet.getInstance());
			return new LockingIteration<BindingSet, QueryEvaluationException>(stLock, iter);
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
			stLock.release();
			throw new SailException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getContextIDsInternal()
	 */
	@Override
	protected CloseableIteration<? extends Resource, SailException> getContextIDsInternal()
			throws SailException {
		ArrayList<Resource> contextIDs = new ArrayList<Resource>();
		Iterator<Locator> allBaseIris = tmSystem.getLocators().iterator();
		while (allBaseIris.hasNext()) {
			contextIDs.add(store.getValueFactory().createURI(allBaseIris.next().toExternalForm()));
		}
		return new CloseableIteratorIteration<Resource, SailException>(
				contextIDs.iterator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#getNamespaceInternal(java
	 * .lang.String)
	 */
	@Override
	protected String getNamespaceInternal(String arg0) throws SailException {
		// No Namespace handling
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getNamespacesInternal()
	 */
	@Override
	protected CloseableIteration<? extends Namespace, SailException> getNamespacesInternal()
			throws SailException {
		// No Namespace handling
			 return new CloseableIteratorIteration<Namespace, SailException>(new LinkedHashMap<String, NamespaceImpl>(16).values().iterator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#getStatementsInternal(org
	 * .openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value,
	 * boolean, org.openrdf.model.Resource[])
	 */
	@Override
	protected CloseableIteration<? extends Statement, SailException> getStatementsInternal(
			Resource subj, URI pred, Value obj, boolean includeInferred, Resource... contexts)
			throws SailException {		
		Lock stLock = store.getStatementsReadLock();
		 try {
			 return new LockingIteration<Statement, SailException>(stLock, store.createStatementIterator(
						SailException.class, subj, pred, obj, !includeInferred,  contexts));
		} catch (RuntimeException e) {
			stLock.release();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#removeNamespaceInternal(java
	 * .lang.String)
	 */
	@Override
	protected void removeNamespaceInternal(String arg0) throws SailException {
		// No Namespace handling

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#removeStatementsInternal(
	 * org.openrdf.model.Resource, org.openrdf.model.URI,
	 * org.openrdf.model.Value, org.openrdf.model.Resource[])
	 */
	@Override
	protected void removeStatementsInternal(Resource arg0, URI arg1,
			Value arg2, Resource... arg3) throws SailException {
		System.err.println("removeNamespaceInternal");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.helpers.SailConnectionBase#rollbackInternal()
	 */
	@Override
	protected void rollbackInternal() throws SailException {
		// No bulk operations
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#setNamespaceInternal(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	protected void setNamespaceInternal(String arg0, String arg1)
			throws SailException {
		// No Namespace handling

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#sizeInternal(org.openrdf.
	 * model.Resource[])
	 */
	@Override
	protected long sizeInternal(Resource... arg0) throws SailException {
		System.err.println(8);
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#startTransactionInternal()
	 */
	@Override
	protected void startTransactionInternal() throws SailException {
		// No bulk operations
	}
	
	
	/**
	 * Implementation of the TripleSource interface from the Sail Query Model
	 */
	protected class TmapiTripleSource implements TripleSource {

		protected final boolean includeInferred;

		
		public TmapiTripleSource(boolean includeInferred) {
			this.includeInferred = includeInferred;
		}

		public ValueFactory getValueFactory() {
			return store.getValueFactory();
		}

		public CloseableIteration<? extends Statement, QueryEvaluationException> getStatements(
				Resource subj, URI pred, Value obj, Resource... contexts)
				throws QueryEvaluationException {		
			return store.createStatementIterator(QueryEvaluationException.class, subj, pred, obj, !includeInferred, contexts);
		}
	} // end inner class TmapiTripleSource

}
