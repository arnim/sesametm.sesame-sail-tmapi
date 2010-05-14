/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.tmapi2tm.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.sail.SailException;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
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
	Set<Locator> locators = null;
	Set<TmapiValue> values = null;
	
	public SailTopic(Topic topic, TopicMap[] topicMaps) throws SailException {
		Set<Locator> ls = new HashSet<Locator>();
		topic.getItemIdentifiers();
		if (ls.isEmpty())
			ls.addAll(topic.getSubjectIdentifiers());
		if (ls.isEmpty())
			ls.addAll(topic.getSubjectLocators());
		if (ls.isEmpty())
			throw new SailException("No topics found");
		this.locator = ls.iterator().next();
		this.topicMaps = topicMaps;
		init();
	}

	public SailTopic(Locator locator, TopicMap[] topicMaps)
			throws SailException {
		this.topicMaps = topicMaps;
		this.locator = locator;
		init();
	}
	
	private void init() throws SailException{
		Topic t = null;
		topics = new HashSet<Topic>();
		for (TopicMap tm : topicMaps) { // getting all the Topics in the Maps
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
			} catch (ClassCastException e) {
			}
		} // end of getting all the Topics in the Maps
		if (!existis())
			throw new SailException("No topics found with the IRI " + locator);
	}


	private boolean existis() {


		return topics.size() > 0;
	}

	public Set<Topic> getWrapped() {
		return topics;
	}

	public Set<Locator> getLocators() {
		if (locators != null)
			return locators;
		locators = new HashSet<Locator>();
		Topic t = null;
		Iterator<Topic> tIterator = topics.iterator();
		while (tIterator.hasNext()) {
			t = tIterator.next();
			locators.addAll(t.getItemIdentifiers());
			locators.addAll(t.getSubjectIdentifiers());
			locators.addAll(t.getSubjectLocators());
		}
		return locators;
	}

	@Override
	public String toString() {
		return "SailTopic [locators=" + getLocators() + ", topicMaps="
				+ Arrays.toString(topicMaps) + "]";
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public Set<TmapiValue> getCharacteristics(SailTopic type) {
		if (values != null)
			return values;
		values = new HashSet<TmapiValue>();
		Iterator<Topic> topicsIterator = topics.iterator();
		Iterator<Topic> typesIterator = type.getWrapped().iterator();
		Iterator<Name> namesIterator;
		Iterator<Occurrence> occurrenceIterator;
		Topic t, aWrappedType;
		while (topicsIterator.hasNext()) {
			t = topicsIterator.next();
			while (typesIterator.hasNext()) {
				aWrappedType = typesIterator.next();
				namesIterator = t.getNames(aWrappedType).iterator();
				while (namesIterator.hasNext()) {
					try {
						values.add(new TmapiValue(namesIterator.next()));
					} catch (Exception e) {
						// topic not existent i the Topic Map
					}
				}
				occurrenceIterator = t.getOccurrences(aWrappedType).iterator();
				while (occurrenceIterator.hasNext()) {
					try {
						values.add(new TmapiValue(occurrenceIterator.next()));
					} catch (Exception e) {
						// topic not existent i the Topic Map
					}
				}
			}
		}
		return values;
	}

	public Set<SailAssociation> getAssociations() {
		Set<SailAssociation> associations = new HashSet<SailAssociation>();
		Iterator<Topic> tIterator = topics.iterator();
		Iterator<Role> roles;
		while (tIterator.hasNext()) {
			roles = tIterator.next().getRolesPlayed().iterator();
			while (roles.hasNext()) {
				associations.add(new SailAssociation(roles.next().getParent()));
			}
		}
		return associations;
	}
	
	

}
