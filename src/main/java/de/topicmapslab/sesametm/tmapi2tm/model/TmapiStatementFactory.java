/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.tmapi2tm.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openrdf.sail.SailException;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStatementFactory {
	
	
	private static List<SailTopic> statements;
	private SailTopic subj, predi, obj;
	private TopicMap[] topicMaps;
	
	public TmapiStatementFactory(SailTopic subj, SailTopic predi, SailTopic obj, TopicMap[] relevantMSs){
		this.subj = subj;
		this.predi = predi;
		this.obj = obj;
		this.topicMaps = relevantMSs;
	}

	public List<SailTopic> generateStatements() {
		statements = new LinkedList<SailTopic>();	
		System.out.println(" ->factory-getStatements-  : " + subj + " : " + predi + " : " + obj );
		
		
		try {
			System.out.println(obj.getCharacteristics(new SailTopic(obj.getWrapped().iterator().next().getParent().createLocator("http://www.google.com/ObjectROletype"), topicMaps)));
		} catch (SailException e) {
			e.printStackTrace();
		}
		
		return statements;
	}
	
	
	private Set<SailTopic> getAllTopics(TopicMap... contexts){
		Set<SailTopic> topics = new HashSet<SailTopic>();
		Iterator<Topic> tIterator;
		for (TopicMap tm : contexts){
			tIterator = tm.getTopics().iterator();
			while (tIterator.hasNext()) {
				try {
					topics.add(new SailTopic(tIterator.next(), contexts));
				} catch (SailException e) {
					e.printStackTrace();
				}
			}
		}
		return topics;
	}

}
