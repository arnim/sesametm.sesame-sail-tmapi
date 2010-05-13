package de.topicmapslab.sesametm.tmapi2tm.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.sail.SailException;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;



/**
 * @author Arnim Bleier
 *
 */
public class SailTopic {
	
	Set<Topic> topics;
	TopicMap[] topicMaps;
	Locator locator;
	Set<Locator> locators;
	
	public SailTopic(Locator locator, TopicMap[] topicMaps) throws SailException{
		this.topicMaps = topicMaps;
		this.locator = locator;
		topics = new HashSet<Topic>();
		Topic t = null;
		for (TopicMap tm :topicMaps){ // getting all the Topics in the Maps
			t = tm.getTopicBySubjectIdentifier(locator);
			if (t != null)
				topics.add(t);
			t = tm.getTopicBySubjectLocator(locator);
			if (t != null)
				topics.add(t);
			try {
				t = null;
				t = (Topic) tm.getConstructByItemIdentifier(locator);
				if (t != null)
					topics.add(t);
			} catch (ClassCastException e) {}
		} // end of getting all the Topics in the Maps
		if (!existis())
			throw new SailException("No topics found with the IRI " + locator);
		locators = new HashSet<Locator>();
		Iterator<Topic> tIterator = topics.iterator();
		while (tIterator.hasNext()) {
			t = tIterator.next();
			locators.addAll(t.getItemIdentifiers());
			locators.addAll(t.getSubjectIdentifiers());
			locators.addAll(t.getSubjectLocators());
		}
	}

	private boolean existis(){
		return topics.size() > 0;
	}
	
	public Set<Topic> getWrapped(){
		return topics;
	}
	
	public Set<Locator> getLocators(){
		return locators;
	}

	@Override
	public String toString() {
		return "SailTopic [locators=" + locators + ", topicMaps="
				+ Arrays.toString(topicMaps) + "]";
	}
	
	public Set<TmapiValue> getCharacteristics(SailTopic type){
		Set<TmapiValue> values = new HashSet<TmapiValue>();
		return values;
	}

}