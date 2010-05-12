/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import info.aduna.iteration.LookAheadIteration;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.ContextStatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStatementIterator <X extends Exception> extends LookAheadIteration<ContextStatementImpl, X> {

	private int ne = 0;

	public TmapiStatementIterator(Resource subj, URI pred,
			Value obj, boolean explicitOnly,Resource... contexts) {
		//		System.out.println(" ->itera--  : " + subj + " : " + pred + " : " + obj + " : " + contexts.length);
	}

	@Override
	protected ContextStatementImpl getNextElement() {
		if (ne < 4){
			ne++;
			return new ContextStatementImpl(new URIImpl("http://www.fixreturn.org/1"), new URIImpl("http://www.fixreturn.org/2"), new URIImpl("http://www.fixreturn.org/3"), new URIImpl("http://www.fixreturn.org/4")) ;
		}
		return null;
	}

}
