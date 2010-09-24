///*
// * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
// * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
// */
//
//package de.topicmapslab.sesame.sail.tmapi.utils;
//
//import info.aduna.iteration.LookAheadIteration;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Set;
//
//import org.openrdf.model.Statement;
//import org.openrdf.model.ValueFactory;
//import org.openrdf.sail.Sail;
//import org.openrdf.sail.SailException;
//import org.tmapi.core.Locator;
//import org.tmapi.core.Topic;
//import org.tmapi.core.TopicMap;
//
//import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
//import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
//import de.topicmapslab.tmql4j.common.model.query.IQuery;
//import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
//import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
//
///**
// * Iterator implementation based on TMQL query engine.
// * 
// * @author Sven Krosse
// * 
// */
//public class TmqlStatementIterator<X extends Exception> extends
//		LookAheadIteration<Statement, X> {
//
//	private volatile int statementIdx = -1;
//	private ValueFactory valueFactory;
//	private Set<Statement> statements;
//	private TmapiStatementFactory statementFactory;
//	private Iterator<Statement> iterator;
//
//	public TmqlStatementIterator(Sail tmapiStore, Locator subj, Locator pred,
//			Locator obj, TopicMap... topicMaps) {
//
//		this.valueFactory = tmapiStore.getValueFactory();
//		this.statements = new HashSet<Statement>();
//		this.statementFactory = new TmapiStatementFactory(valueFactory);
//
//		try {
//			forTopicMaps(subj, pred, obj, topicMaps);
//		} catch (SailException e) {
//			//NOTHING TO DO
//		}
//
//		this.iterator = statements.iterator();
//	}
//
//	@Override
//	protected Statement getNextElement() {
//		statementIdx++;
//		if (iterator.hasNext())
//			return iterator.next();
//		return null;
//	}
//
//	private void forTopicMaps(Locator subj, Locator pred, Locator obj,
//			TopicMap... topicMaps) throws SailException {
//		for (TopicMap tm : topicMaps) {
//			try {
//				ITMQLRuntime runtime = TMQLRuntimeFactory.newFactory()
//						.newRuntime(tm);
//				IQuery query = runtime.run(SparqlTmqlUtils.toPredicateQuery(
//						subj, pred, obj));
////				System.out.println(query.toString());
//				for (IResult result : query.getResults()) {
//					Iterator<Object> iterator = result.iterator();
//					Object s = iterator.next();
//					if (s instanceof Collection<?>) {
//						s = ((Collection<?>) s).iterator().next();
//					}
//					Object p = iterator.next();
//					if (p instanceof Collection<?>) {
//						p = ((Collection<?>) p).iterator().next();
//					}
//					Object o = iterator.next();
//					if (o instanceof Collection<?>) {
//						o = ((Collection<?>) o).iterator().next();
//					}
//					Statement statement = statementFactory.create((Topic) s,
//							(Topic) p, o);
////					System.out.println(statement);
//					statements.add(statement);
//				}
//
//				query = runtime.run(SparqlTmqlUtils.toCharacteristicsQuery(
//						subj, pred, obj));
////				System.out.println(query.toString());
//				for (IResult result : query.getResults()) {
//					Iterator<Object> iterator = result.iterator();
//					Object s = iterator.next();
//					if (s instanceof Collection<?>) {
//						s = ((Collection<?>) s).iterator().next();
//					}
//					Object p = iterator.next();
//					if (p instanceof Collection<?>) {
//						p = ((Collection<?>) p).iterator().next();
//					}
//					Object o = iterator.next();
//					if (o instanceof Collection<?>) {
//						o = ((Collection<?>) o).iterator().next();
//					}
//					Statement statement = statementFactory.create((Topic) s,
//							(Topic) p, o);
////					System.out.println(statement);
//					statements.add(statement);
//				}
//
//			} catch (TMQLRuntimeException e) {
//				throw new SailException(e);
//			}
//		}
//	}
//}
