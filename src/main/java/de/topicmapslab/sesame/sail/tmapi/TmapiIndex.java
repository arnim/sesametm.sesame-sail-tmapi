/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.sail.tmapi;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.tmapi.core.Locator;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiIndex {

	private TopicMapSystem tmSys;

	public TmapiIndex(TmapiStore tmapiStore) {
		this.tmSys = tmapiStore.getTopicMapSystem();
	}

	
	
	/**
	 * @return {@link TopicMapSystem}
	 */
	public TopicMapSystem getTopicMapSystem() {
		return tmSys;
	}
	
	public void index(){
		System.out.println(getTopicMaps());
	}
	
	private Set<TopicMap> getTopicMaps(){
		Set<TopicMap> topicMaps = new HashSet<TopicMap>();
		Set<Locator> knownLocators = tmSys.getLocators();
		Iterator<Locator> locatorsIterator = knownLocators.iterator();
		while (locatorsIterator.hasNext()) {
			topicMaps.add(tmSys.getTopicMap(locatorsIterator.next()));
		}
		return topicMaps;
	}

}
