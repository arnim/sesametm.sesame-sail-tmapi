/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.tmapi2tm.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStatementFactory {
	
	
	private static List<SailTopic> statements;


	public static List<SailTopic> generateStatements(SailTopic subj, SailTopic predi, SailTopic obj) {
		statements = new LinkedList<SailTopic>();	
		System.out.println(" ->factory--  : " + subj + " : " + predi + " : " + obj );
		return statements;
	}

}
