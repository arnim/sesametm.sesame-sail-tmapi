/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import org.tmapi.core.Locator;
import org.tmapi.core.TopicMap;

import de.topicmapslab.sesame.sail.tmapi.utils.TmapiStatementIterator;

/**
 * @author Arnim Bleier
 *
 */
public interface SailTmapiPlugin {
	
	void evaluate(Locator subject, Locator predicate, Locator object, TopicMap tm, TmapiStatementIterator<?> statementIterator );
	
}
