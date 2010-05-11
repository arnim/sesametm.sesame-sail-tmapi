/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import java.util.ArrayList;

import info.aduna.iteration.CloseableIteration;
import info.aduna.iteration.CloseableIterationBase;
import info.aduna.iteration.CloseableIteratorIteration;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.Dataset;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailBase;
import org.openrdf.sail.helpers.SailConnectionBase;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapExistsException;
import org.tmapi.core.TopicMapSystem;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiSailConnection extends SailConnectionBase {
	
	
	private TopicMapSystem tmSystem;

	/**
	 * @param sailBase
	 */
	public TmapiSailConnection(SailBase sailBase) {
		super(sailBase);
		tmSystem = ((TmapiStore) sailBase).getTmSystem();
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#addStatementInternal(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value, org.openrdf.model.Resource[])
	 */
	@Override
	protected void addStatementInternal(Resource arg0, URI arg1, Value arg2,
			Resource... arg3) throws SailException {
		System.out.println("hier" + arg0 + arg1 + arg2 + arg3);
		TopicMap tm;
		try {
			tm = tmSystem.createTopicMap(arg3[0].toString());
		} catch (TopicMapExistsException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#clearInternal(org.openrdf.model.Resource[])
	 */
	@Override
	protected void clearInternal(Resource... arg0) throws SailException {
		System.out.println("clearInternal");

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#clearNamespacesInternal()
	 */
	@Override
	protected void clearNamespacesInternal() throws SailException {
		System.out.println("clearNamespacesInternal");

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#closeInternal()
	 */
	@Override
	protected void closeInternal() throws SailException {
		System.out.println("closeInternal");

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#commitInternal()
	 */
	@Override
	protected void commitInternal() throws SailException {
		System.out.println("commitInternal");

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#evaluateInternal(org.openrdf.query.algebra.TupleExpr, org.openrdf.query.Dataset, org.openrdf.query.BindingSet, boolean)
	 */
	@Override
	protected CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluateInternal(
			TupleExpr arg0, Dataset arg1, BindingSet arg2, boolean arg3)
			throws SailException {
		System.out.println("wichtige in CloseableIteration");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getContextIDsInternal()
	 */
	@Override
	protected CloseableIteration<? extends Resource, SailException> getContextIDsInternal()
			throws SailException {
		System.out.println("wichtige in CloseableIteration 3");
		CloseableIteration<? extends Resource, SailException> result = new CloseableIterationBase<Resource, SailException>() {

			public boolean hasNext() throws SailException {
				// TODO Auto-generated method stub
				return false;
			}

			public Resource next() throws SailException {
				// TODO Auto-generated method stub
				return null;
			}

			public void remove() throws SailException {
				// TODO Auto-generated method stub
				
			}};
			System.out.println("  33");
			
			ArrayList<Resource> contextIDs = new ArrayList<Resource>(32);
			contextIDs.add(new URIImpl("http://www.openrdf.org/doc/sesame2/2.3.1/apidocs/"));

			CloseableIteratorIteration<Resource, SailException> result2 = new CloseableIteratorIteration<Resource, SailException>(contextIDs.iterator());
			
		return result2;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getNamespaceInternal(java.lang.String)
	 */
	@Override
	protected String getNamespaceInternal(String arg0) throws SailException {
		System.out.println("getNamespaceInternal");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getNamespacesInternal()
	 */
	@Override
	protected CloseableIteration<? extends Namespace, SailException> getNamespacesInternal()
			throws SailException {
		System.out.println("wichtige in CloseableIteration 1");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getStatementsInternal(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value, boolean, org.openrdf.model.Resource[])
	 */
	@Override
	protected CloseableIteration<? extends Statement, SailException> getStatementsInternal(
			Resource arg0, URI arg1, Value arg2, boolean arg3, Resource... arg4)
			throws SailException {
		System.out.println("wichtige in CloseableIteration 2");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#removeNamespaceInternal(java.lang.String)
	 */
	@Override
	protected void removeNamespaceInternal(String arg0) throws SailException {
		System.out.println("removeNamespaceInternal");

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#removeStatementsInternal(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value, org.openrdf.model.Resource[])
	 */
	@Override
	protected void removeStatementsInternal(Resource arg0, URI arg1,
			Value arg2, Resource... arg3) throws SailException {
		System.out.println("removeNamespaceInternal");

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#rollbackInternal()
	 */
	@Override
	protected void rollbackInternal() throws SailException {
		System.out.println("rollbackInternal");

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#setNamespaceInternal(java.lang.String, java.lang.String)
	 */
	@Override
	protected void setNamespaceInternal(String arg0, String arg1)
			throws SailException {
		System.out.println("setNamespaceInternal");

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#sizeInternal(org.openrdf.model.Resource[])
	 */
	@Override
	protected long sizeInternal(Resource... arg0) throws SailException {
		System.out.println("sizeInternal 0");
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#startTransactionInternal()
	 */
	@Override
	protected void startTransactionInternal() throws SailException {
		System.out.println("startTransactionInternal");
	}

}
