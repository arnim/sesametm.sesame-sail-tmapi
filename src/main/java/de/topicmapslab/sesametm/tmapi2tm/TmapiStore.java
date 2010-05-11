/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import info.aduna.iteration.CloseableIteration;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ContextStatementImpl;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailBase;
import org.openrdf.sail.memory.model.MemStatement;
import org.openrdf.sail.memory.model.MemStatementIterator;
import org.openrdf.sail.memory.model.ReadMode;
import org.tmapi.core.FactoryConfigurationException;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStore extends SailBase {
	
	
	
	/**
	 * Factory/cache for MemValue objects.
	 */
	private TmapiValueFactory valueFactory = new TmapiValueFactory();
	
	private TopicMapSystem tmSystem;

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
			Class<QueryEvaluationException> class1, Resource subj, URI pred,
			Value obj, boolean b, Resource[] contexts) {
		return new TmapiStatementIterator<X>();
	}
	

}
