/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

/**
 * @author Arnim Bleier
 *
 */
public class MapSpider implements Runnable {

	private Iterable statements;
	private TopicMap tm;
	private Topic sTopic;
	private Topic pTopic;
	private Topic oTopic;

	/**
	 * 
	 */
	public MapSpider(Iterable statements, TopicMap tm, Topic sTopic, Topic pTopic, Topic oTopic) {
		this.statements = statements;
		this.tm = tm;
		this.sTopic = sTopic;
		this.pTopic = pTopic;
		this.oTopic = oTopic;
	}


	public void run() {
		selectXXX();

	}

	private void selectXXX() {
		// TODO Auto-generated method stub
		
	}

}
