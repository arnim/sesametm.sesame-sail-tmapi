/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.live;

import info.aduna.iteration.LookAheadIteration;

import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.tmapi.core.Locator;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;


/**
 * @author Arnim Bleier
 *
 */
public class TmapiStatementIterator <X extends Exception> extends LookAheadIteration<Statement, X> {

	
	private LiveStore tmapiStore;
	private TopicMapSystem tmSystem;
	boolean explicitOnly;
	private TopicMap[] topicMaps;
	private volatile int statementIdx = -1;
	private Locator subj;
	private Locator pred;
	private Locator obj;
	private ValueFactory valueFactory;

	public TmapiStatementIterator(LiveStore tmapiStore, Locator subject, Locator predicate,
			Locator object, boolean explicitOnly,TopicMap... contexts) {
		this.tmapiStore = tmapiStore;
		this.tmSystem = tmapiStore.getTmSystem();
		this.subj = subject;
		this.pred = predicate;
		this.obj = object;
		this.topicMaps = contexts;
		this.valueFactory = this.tmapiStore.getValueFactory();

	}
	




	@Override
	protected Statement getNextElement() {
		statementIdx++;
		if (statementIdx < 1){
			System.out.println("isss");
			return valueFactory.createStatement(valueFactory.createURI("http://www.fixreturn.org/1"), 
					valueFactory.createURI("http://www.google.com/assoType"), 
					valueFactory.createURI("http://www.fixreturn.org/3"));

		}
		return null;
	}
	

	
	


}
