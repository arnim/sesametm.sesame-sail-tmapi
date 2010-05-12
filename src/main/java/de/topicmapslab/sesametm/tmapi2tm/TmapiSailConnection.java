/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import info.aduna.concurrent.locks.Lock;
import info.aduna.iteration.CloseableIteration;
import info.aduna.iteration.CloseableIteratorIteration;
import info.aduna.iteration.LockingIteration;

import java.util.ArrayList;
import java.util.Iterator;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
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
import org.tmapi.core.TopicMapSystem;

/**
 * @author Arnim Bleier
 * 
 */
public class TmapiSailConnection extends SailConnectionBase {

	private TopicMapSystem tmSystem;
	protected final TmapiStore store;

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
	protected void addStatementInternal(Resource arg0, URI arg1, Value arg2,
			Resource... arg3) throws SailException {
		System.out.println("adding " + arg0 + arg1 + arg2 + arg3);
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
		System.out.println("clearInternal");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openrdf.sail.helpers.SailConnectionBase#clearNamespacesInternal()
	 */
	@Override
	protected void clearNamespacesInternal() throws SailException {
		System.out.println("clearNamespacesInternal");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.helpers.SailConnectionBase#closeInternal()
	 */
	@Override
	protected void closeInternal() throws SailException {
		System.out.println("closeInternal");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.helpers.SailConnectionBase#commitInternal()
	 */
	@Override
	protected void commitInternal() throws SailException {
		System.out.println("commitInternal");

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
//		System.out.println("Incoming query model:\n{}"+ tupleExpr.toString());
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
			contextIDs.add(new URIImpl(allBaseIris.next().toExternalForm()));
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
		System.out.println("getNamespaceInternal");
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
		System.out.println("wichtige in CloseableIteration 1");
		return null;
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
			Resource subj, URI predi, Value obj, boolean arg3, Resource... arg4)
			throws SailException {

		
		Lock stLock = store.getStatementsReadLock();
		CloseableIteration<BindingSet, QueryEvaluationException> iter;
		
		
		System.out.println("getStatementsInternal: " + subj + " " +  predi + " " + obj );
		return null;

		
//		System.out.println("Incoming query model:\n{}"+ tupleExpr.toString());
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
		System.out.println("removeNamespaceInternal");

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
		System.out.println("removeNamespaceInternal");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openrdf.sail.helpers.SailConnectionBase#rollbackInternal()
	 */
	@Override
	protected void rollbackInternal() throws SailException {
		System.out.println("rollbackInternal");

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
		System.out.println("setNamespaceInternal");

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
		System.out.println("sizeInternal 0");
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
		System.out.println("startTransactionInternal");
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
			return store.createStatementIterator(QueryEvaluationException.class, subj, pred, obj,
					!includeInferred, contexts);
		}
	} // end inner class TmapiTripleSource

}
