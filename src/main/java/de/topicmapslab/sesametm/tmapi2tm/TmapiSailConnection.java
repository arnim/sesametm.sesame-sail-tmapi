/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import info.aduna.iteration.CloseableIteration;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.Dataset;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailBase;
import org.openrdf.sail.helpers.SailConnectionBase;

/**
 * @author userunknown
 *
 */
public class TmapiSailConnection extends SailConnectionBase {

	/**
	 * @param sailBase
	 */
	public TmapiSailConnection(SailBase sailBase) {
		super(sailBase);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#addStatementInternal(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value, org.openrdf.model.Resource[])
	 */
	@Override
	protected void addStatementInternal(Resource arg0, URI arg1, Value arg2,
			Resource... arg3) throws SailException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#clearInternal(org.openrdf.model.Resource[])
	 */
	@Override
	protected void clearInternal(Resource... arg0) throws SailException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#clearNamespacesInternal()
	 */
	@Override
	protected void clearNamespacesInternal() throws SailException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#closeInternal()
	 */
	@Override
	protected void closeInternal() throws SailException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#commitInternal()
	 */
	@Override
	protected void commitInternal() throws SailException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#evaluateInternal(org.openrdf.query.algebra.TupleExpr, org.openrdf.query.Dataset, org.openrdf.query.BindingSet, boolean)
	 */
	@Override
	protected CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluateInternal(
			TupleExpr arg0, Dataset arg1, BindingSet arg2, boolean arg3)
			throws SailException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getContextIDsInternal()
	 */
	@Override
	protected CloseableIteration<? extends Resource, SailException> getContextIDsInternal()
			throws SailException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getNamespaceInternal(java.lang.String)
	 */
	@Override
	protected String getNamespaceInternal(String arg0) throws SailException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getNamespacesInternal()
	 */
	@Override
	protected CloseableIteration<? extends Namespace, SailException> getNamespacesInternal()
			throws SailException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#getStatementsInternal(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value, boolean, org.openrdf.model.Resource[])
	 */
	@Override
	protected CloseableIteration<? extends Statement, SailException> getStatementsInternal(
			Resource arg0, URI arg1, Value arg2, boolean arg3, Resource... arg4)
			throws SailException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#removeNamespaceInternal(java.lang.String)
	 */
	@Override
	protected void removeNamespaceInternal(String arg0) throws SailException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#removeStatementsInternal(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value, org.openrdf.model.Resource[])
	 */
	@Override
	protected void removeStatementsInternal(Resource arg0, URI arg1,
			Value arg2, Resource... arg3) throws SailException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#rollbackInternal()
	 */
	@Override
	protected void rollbackInternal() throws SailException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#setNamespaceInternal(java.lang.String, java.lang.String)
	 */
	@Override
	protected void setNamespaceInternal(String arg0, String arg1)
			throws SailException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#sizeInternal(org.openrdf.model.Resource[])
	 */
	@Override
	protected long sizeInternal(Resource... arg0) throws SailException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.helpers.SailConnectionBase#startTransactionInternal()
	 */
	@Override
	protected void startTransactionInternal() throws SailException {
		// TODO Auto-generated method stub

	}

}
