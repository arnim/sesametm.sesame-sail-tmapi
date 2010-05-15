/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.live;

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

	
	private LiveStore tmapiStore;
	private TopicMapSystem tmSystem;
	private SailTopic subj, pred, obj;
	boolean explicitOnly;
	private TopicMap[] topicMaps;
	private volatile int statementIdx = -1;

	public TmapiStatementIterator(LiveStore tmapiStore, SailTopic subject, SailTopic predicate,
			SailTopic object, boolean explicitOnly,TopicMap... contexts) {
		this.tmapiStore = tmapiStore;
		this.tmSystem = tmapiStore.getTmSystem();
		this.subj = subject;
		this.pred = predicate;
		this.obj = object;
		this.topicMaps = contexts;
		

	}
	




	@Override
	protected ContextStatementImpl getNextElement() {
		statementIdx++;
		if (statementIdx < 1){
			System.out.println("isss");
			return new ContextStatementImpl(new URIImpl("http://www.fixreturn.org/1"), new URIImpl("http://www.google.com/assoType"), new URIImpl("http://www.fixreturn.org/3"), new URIImpl("http://www.fixreturn.org/4")) ;
		}
		return null;
	}
	

	
	


}
