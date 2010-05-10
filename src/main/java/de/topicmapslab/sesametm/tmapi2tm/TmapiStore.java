/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import org.openrdf.model.ValueFactory;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.openrdf.sail.helpers.SailBase;
import org.openrdf.sail.memory.model.MemValueFactory;
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
	private final MemValueFactory valueFactory = new MemValueFactory();
	
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
	}

	/* (non-Javadoc)
	 * @see org.openrdf.sail.Sail#getValueFactory()
	 */
	public ValueFactory getValueFactory() {
		return null;
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
	

}
