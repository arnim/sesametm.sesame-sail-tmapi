/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStatementIterator <X extends Exception> extends LookAheadIteration<ContextStatementImpl, X> {

	private int ne = 0;
	
	private TmapiStore tmapiStore;
	private TopicMapSystem tmSystem;
	private Resource subj;
	private URI pred;
	private Value obj;
	boolean explicitOnly;
	private Resource[] contexts;
	

	public TmapiStatementIterator(TmapiStore tmapiStore, Resource subj, URI pred,
			Value obj, boolean explicitOnly,Resource... contexts) {
		this.tmapiStore = tmapiStore;
		this.tmSystem = tmapiStore.getTmSystem();
		this.subj = subj;
		this.pred = pred;
		this.obj = obj;
		this.contexts = contexts;
//				System.out.println(" ->itera--  : " + subj + " : " + pred + " : " + obj + " : " + contexts.length);
//		getTopics(obj, contexts);
		System.out.println(getTopicMaps(contexts).getFirst().getId()+ "dsf");
	}

	@Override
	protected ContextStatementImpl getNextElement() {
		if (ne < 1){
			ne++;
			return new ContextStatementImpl(new URIImpl("http://www.fixreturn.org/1"), new URIImpl("http://www.fixreturn.org/2"), new URIImpl("http://www.fixreturn.org/3"), new URIImpl("http://www.fixreturn.org/4")) ;
		}
		return null;
	}
	
	
	private LinkedList<TopicMap> getTopicMaps(Resource... contexts){
		LinkedList<TopicMap> topicMpas = new LinkedList<TopicMap>();
		Set<Locator> knownLocators = tmSystem.getLocators();
		Locator l;
		if (contexts.length > 0){
			HashSet<Locator> relevantLocators = new HashSet<Locator>();
			for (Resource context :contexts){
				l = tmSystem.createLocator(context.stringValue());
				if (knownLocators.contains(l))
					relevantLocators.add(l);
			}
			knownLocators = relevantLocators;
		}
		Iterator<Locator> locatorsIterator = knownLocators.iterator();
		while (locatorsIterator.hasNext()) {
			topicMpas.add(tmSystem.getTopicMap(locatorsIterator.next()));
		}
		return topicMpas;
	}
	
	private Set<Topic> getTopics(Value iri, Resource... contexts){
		if(contexts.length == 0){
			
			
			System.out.println("hier " + contexts.getClass().toString() + " hier ");
//			Iterator<Locator> knownTMs = tmSystem.getLocators().iterator();
//			contexts = new ArrayList<Value>();
//			while (knownTMs.hasNext()) {
//				Locator locator = (Locator) knownTMs.next();
//				contexts.
//			}
		}
		Set<Topic> topics = new HashSet<Topic>();
		for (Value context :contexts) {
			Topic t = getTopic(tmSystem.createLocator(iri.stringValue()), tmSystem.createLocator(context.stringValue()));
			if (t != null)
				topics.add(t);
		}
		return topics;
	}
	
	private Topic getTopic(Locator iri, Locator context){
		TopicMap tm = tmSystem.getTopicMap(context);
		Topic topic = tm.getTopicBySubjectIdentifier(iri);
		return topic;
		
	}

}
