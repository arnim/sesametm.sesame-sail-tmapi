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
	}

	private boolean existis(){
		return topics.size() > 0;
	}
	
	public Set<Topic> getWrapped(){
		return topics;
	}
	
	public Set<Locator> getLocators(){
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
	
	public Set<TmapiValue> getCharacteristics(SailTopic type){
		if (values != null)
			return values;
		values = new HashSet<TmapiValue>();
		return null;
	}
	
	public Set<SailAssociation> getAssociations(){
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
