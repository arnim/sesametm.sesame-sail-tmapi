/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import info.aduna.iteration.LookAheadIteration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.sail.Sail;
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
public class TmapiStatementIterator<X extends Exception> extends
		LookAheadIteration<Statement, X> {

	private volatile int statementIdx = -1;
	private ValueFactory valueFactory;
	private Set<Statement> statements;
	private TmapiStatementFactory statementFactory;
	private Iterator<Statement> iterator;

	public TmapiStatementIterator(Sail tmapiStore, Locator subj, Locator pred,
			Locator obj, TopicMap... topicMaps) {

		this.valueFactory = tmapiStore.getValueFactory();
		this.statements = new HashSet<Statement>();
		this.statementFactory = new TmapiStatementFactory(valueFactory);

		try {
			forTopicMpas(subj, pred, obj, topicMaps);
		} catch (SailException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.iterator = statements.iterator();
	}

	@Override
	protected Statement getNextElement() {
		statementIdx++;
		if (iterator.hasNext())
			return iterator.next();
		return null;
	}

	private void forTopicMpas(Locator subj, Locator pred, Locator obj,
			TopicMap... topicMaps) throws SailException, InterruptedException {
		Topic sTopic = null, pTopic = null, oTopic = null;
		ExecutorService executor = Executors.newFixedThreadPool(3);
		for (TopicMap tm : topicMaps) {

			sTopic = getTopic(subj, tm);
			pTopic = getTopic(pred, tm);
			oTopic = getTopic(obj, tm);


			if (sTopic == null && subj != null || pTopic == null
					&& pred != null || oTopic == null && obj != null) {

				// Q has no match in this tm
			} else {
				executor.execute(new MapSpider(statements, statementFactory, tm, sTopic, pTopic, oTopic));
			}
		}
		
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.HOURS);
	}


	private Topic getTopic(Locator l, TopicMap tm) {
		if (l == null)
			return null;
		Topic t = null;
		t = tm.getTopicBySubjectIdentifier(l);
		if (t != null)
			return t;
		t = tm.getTopicBySubjectLocator(l);
		if (t != null)
			return t;
		try {
			t = (Topic) tm.getConstructByItemIdentifier(l);
		} catch (ClassCastException e) {
			// Found Construct is not a Topic
		}
		return t;
	}

}
