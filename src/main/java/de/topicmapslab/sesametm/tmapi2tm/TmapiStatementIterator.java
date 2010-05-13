/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import info.aduna.iteration.LookAheadIteration;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.ContextStatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.sesametm.tmapi2tm.model.SailTopic;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStatementIterator <X extends Exception> extends LookAheadIteration<ContextStatementImpl, X> {

	private int ne = 0;
	
	private TmapiStore tmapiStore;
	private TopicMapSystem tmSystem;
	private SailTopic subj, pred, obj;
	boolean explicitOnly;
	private TopicMap[] topicMaps;
	

	public TmapiStatementIterator(TmapiStore tmapiStore, SailTopic subject, SailTopic predicate,
			SailTopic object, boolean explicitOnly,TopicMap... contexts) {
		this.tmapiStore = tmapiStore;
		this.tmSystem = tmapiStore.getTmSystem();
		this.subj = subject;
		this.pred = predicate;
		this.obj = object;
		this.topicMaps = contexts;
		
		System.out.println(" ->itera--  : " + subject + " : " + predicate + " : " + object + " : " + contexts.length +  " " + contexts[0]);

	}
	




	@Override
	protected ContextStatementImpl getNextElement() {
		if (ne < 1){
			ne++;
			return new ContextStatementImpl(new URIImpl("http://www.fixreturn.org/1"), new URIImpl("http://www.fixreturn.org/2"), new URIImpl("http://www.fixreturn.org/3"), new URIImpl("http://www.fixreturn.org/4")) ;
		}
		return null;
	}
	

	
	


}
