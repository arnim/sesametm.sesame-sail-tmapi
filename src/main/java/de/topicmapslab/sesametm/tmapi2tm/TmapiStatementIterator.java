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
	private LinkedList<TopicMap> topicMpas;
	
	private Locator sLocator = null, pLocator = null, oLocator = null;

	private Set<Topic> sTopics, pTopics;
	

	public TmapiStatementIterator(TmapiStore tmapiStore, Resource subj, URI pred,
			Value obj, boolean explicitOnly,Resource... contexts) {
		this.tmapiStore = tmapiStore;
		this.tmSystem = tmapiStore.getTmSystem();
		this.subj = subj;
		this.pred = pred;
		this.obj = obj;
		this.contexts = contexts;
				System.out.println(" ->itera--  : " + subj + " : " + pred + " : " + obj + " : " + contexts.length);
		
		topicMpas = getTopicMaps(contexts);
		generateLocators();
		// Verify weather all locators exists;
		pTopics = getTopics(tmSystem.createLocator(pred.stringValue()), topicMpas);
//		sTopics = getTopics(tmSystem.createLocator(subj.stringValue()), topicMpas);

		System.out.println(pTopics);
//		System.out.println(getTopic(tmSystem.createLocator("http://www.google.com/predicate"), topicMpas.getFirst()).getSubjectIdentifiers());
		
	}
	
	private Locator generateLocator(Value v){
		try {
			return tmSystem.createLocator(v.stringValue());
		} catch (Exception e) {
			return null;
		}
	}
	
	private void generateLocators(){
		sLocator = generateLocator(subj);
		pLocator = generateLocator(pred);
		oLocator = generateLocator(obj);
	}



	@Override
	protected ContextStatementImpl getNextElement() {
		if (ne < 1){
			ne++;
			return new ContextStatementImpl(new URIImpl("http://www.fixreturn.org/1"), new URIImpl("http://www.fixreturn.org/2"), new URIImpl("http://www.fixreturn.org/3"), new URIImpl("http://www.fixreturn.org/4")) ;
		}
		return null;
	}
	
	/**
	 * 
	 * 
	 * @param contexts
	 * @return	A List of {@link TopicMap} to be queried.
	 */
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
	
//	private Set<Topic> getTopics(Value iri, Resource... contexts){
//		if(contexts.length == 0){
//			
//			
//			System.out.println("hier " + contexts.getClass().toString() + " hier ");
////			Iterator<Locator> knownTMs = tmSystem.getLocators().iterator();
////			contexts = new ArrayList<Value>();
////			while (knownTMs.hasNext()) {
////				Locator locator = (Locator) knownTMs.next();
////				contexts.
////			}
//		}
//		Set<Topic> topics = new HashSet<Topic>();
//		for (Value context :contexts) {
//			Topic t = getTopic(tmSystem.createLocator(iri.stringValue()), tmSystem.createLocator(context.stringValue()));
//			if (t != null)
//				topics.add(t);
//		}
//		return topics;
//	}
	
	/**
	 * 
	 */
	private Set<Topic> getTopics(Locator locator, List<TopicMap> tMaps) {
		Iterator<TopicMap> tmIterator = tMaps.iterator();
		Set<Topic> topics = new HashSet<Topic>();
		Topic t = null;
		while (tmIterator.hasNext()) {
			t = getTopic(locator, tmIterator.next());
			if (t != null)
				topics.add(t);
		}
		return topics;		
	}
	
	/**
	 * 
	 */
	private Topic getTopic(Locator iri, TopicMap tm){
		Topic topic = null;
		topic = tm.getTopicBySubjectIdentifier(iri);
		if (topic == null)
			topic = tm.getTopicBySubjectLocator(iri);
		if (topic == null)
			try {
				topic = (Topic) tm.getConstructByItemIdentifier(iri);
			} catch (ClassCastException e) {}
		return topic;
	}

}
