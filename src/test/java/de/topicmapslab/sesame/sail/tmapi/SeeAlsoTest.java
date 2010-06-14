/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.n3.N3Writer;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

/**
 * @author Arnim Bleier
 * 
 */
public class SeeAlsoTest extends TestCase {

	private static TmapiStore _sail;
	private static Repository _tmapiRepository;
	private static RepositoryConnection _con;
	private static TopicMapSystem _tms;

	final static String baseIRI = "http://www.topicmapslab.de/test/base/";

	@Override
	@Before
	public void setUp() throws Exception {
		_tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		TopicMap tm = _tms.createTopicMap(baseIRI);
		Topic xyz = tm.createTopicBySubjectIdentifier(tm.createLocator(baseIRI
				+ "xyz"));
		Topic worksFor = tm.createTopicBySubjectIdentifier(tm
				.createLocator(baseIRI + "worksFor"));
		Topic employer = tm.createTopicBySubjectIdentifier(tm
				.createLocator(baseIRI + "employer"));
		Topic employee = tm.createTopicBySubjectIdentifier(tm
				.createLocator(baseIRI + "employee"));

		Topic alf = tm.createTopicBySubjectIdentifier(tm.createLocator(baseIRI
				+ "alfsi1"));
		Topic bert = tm.createTopicBySubjectIdentifier(tm.createLocator(baseIRI
				+ "bertsi1"));

		Association awf = tm.createAssociation(worksFor);
		Association bwf = tm.createAssociation(worksFor);

		awf.createRole(employee, alf);
		awf.createRole(employer, xyz);

		bwf.createRole(employee, bert);
		bwf.createRole(employer, xyz);
		
		
		
		
		_sail = new TmapiStore(_tms, CONFIG.LIVE);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
	}

	
//	
//
//	@Test
//	public void testConstruct() throws Exception {
//		_sail = new TmapiStore(_tms, CONFIG.LIVE);
//		_tmapiRepository = new SailRepository(_sail);
//		_tmapiRepository.initialize();
//		_con = _tmapiRepository.getConnection();
//		RDFHandler rdfWriter = new N3Writer(System.out);
//		_con.exportStatements(null, null, null, true, rdfWriter);
//	}
//	
//	
	
	
//	
//	
//	/**
//	 * Test.
//	 * 
//	 */
//	@Test
//	public void testSTO() throws Exception {
//		String queryString = "CONSTRUCT   { ?s <http://www.w3.org/2002/07/owl#sameAs> <http://www.topicmapslab.de/test/base/bertsi1> .  }  WHERE   { ?s <http://www.w3.org/2002/07/owl#sameAs> <http://www.topicmapslab.de/test/base/bertsi1> . }";
//		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
//				queryString);
//		GraphQueryResult result = query.evaluate();
//		assertTrue(result.hasNext());
//		Statement statement = result.next();
//		assertEquals(OWL.SAMEAS, statement.getPredicate());
//		assertFalse(result.hasNext());
//	}
//	
//	
//	
//	
//
//	/**
//	 * Test.
//	 * 
//	 */
//	@Test
//	public void testxPx() throws Exception {
//		String queryString = "CONSTRUCT   { ?s <"+ OWL.SAMEAS.stringValue() + "> ?o . }  WHERE   { ?s <"+ OWL.SAMEAS.stringValue() + "> ?o . }";
//		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
//				queryString);
//		GraphQueryResult result = query.evaluate();
//		assertTrue(result.hasNext());
//		Statement statement = result.next();
//		assertEquals(OWL.SAMEAS, statement.getPredicate());
//		assertTrue(result.hasNext());
//		statement = result.next();
//		assertEquals(OWL.SAMEAS, statement.getPredicate());
//		assertTrue(result.hasNext());
//		statement = result.next();
//		assertEquals(OWL.SAMEAS, statement.getPredicate());
//		assertTrue(result.hasNext());
//		statement = result.next();
//		assertEquals(OWL.SAMEAS, statement.getPredicate());
//		assertFalse(result.hasNext());
//	}
//	
//	
//	/**
//	 * Test.
//	 * 
//	 */
//	@Test
//	public void testxTO() throws Exception {
//		String queryString = "CONSTRUCT   { ?s <"+ OWL.SAMEAS.stringValue() + "> <http://www.topicmapslab.de/test/base/bertsi2> . }  WHERE   { ?s <"+ OWL.SAMEAS.stringValue() + "> <http://www.topicmapslab.de/test/base/bertsi2> . }";
//		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
//				queryString);
//		GraphQueryResult result = query.evaluate();
//		assertTrue(result.hasNext());
//		Statement statement = result.next();
//		assertEquals(OWL.SAMEAS, statement.getPredicate());
//		assertEquals(_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bertsl3"), statement.getSubject());
//		assertFalse(result.hasNext());
//	}
//	
//
//	
//
	@Test
	public void testSxx() throws Exception {
		int i = 0;
		RepositoryResult<Statement> result = _con.getStatements(_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bertsi1"), null,
				null, true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		if (statement.getPredicate().equals(RDFS.SEEALSO))
			i++;
		assertEquals("http://www.topicmapslab.de/test/base/bertsi1", statement
				.getSubject().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base//t/http://www.topicmapslab.de/test/base/bertsi1", statement
				.getObject().stringValue());
		assertTrue(result.hasNext());
		statement = result.next();
		if (statement.getPredicate().equals(RDFS.SEEALSO))
			i++;
		assertEquals("http://www.topicmapslab.de/test/base/bertsi1", statement
				.getSubject().stringValue());
		assertFalse(result.hasNext());
		assertEquals(1, i);
	}

	@Test
	public void testSPx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bertsi1"), RDFS.SEEALSO,
				null, true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals(RDFS.SEEALSO, statement.getPredicate());
		assertFalse(result.hasNext());
	}
//
//
//
//	@Test
//	public void testxxO() throws Exception {
//		int i = 0;
//		RepositoryResult<Statement> result = _con.getStatements(null, null,
//				_con
//				.getValueFactory().createURI(
//						"http://www.topicmapslab.de/test/base/bertsi2"), true);
//		assertTrue(result.hasNext());
//		Statement statement = result.next();
//		if (statement.getPredicate().equals(OWL.SAMEAS))
//			i++;
//		assertEquals("http://www.topicmapslab.de/test/base/bertsl3", statement
//				.getObject().stringValue());
//		assertTrue(result.hasNext());
//		statement = result.next();
//		if (statement.getPredicate().equals(OWL.SAMEAS))
//			i++;
//		assertTrue(result.hasNext());
//		statement = result.next();
//		if (statement.getPredicate().equals(OWL.SAMEAS))
//			i++;
//		assertFalse(result.hasNext());
//		result = _con
//				.getStatements(_con.getValueFactory().createURI(
//						"http://www.topicmapslab.de/test/base/wrong"), null,
//						null, true);
//		assertFalse(result.hasNext());
//		assertEquals(2, i);
//	}


	@Test
	public void testxxx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(null, null,
				null, true);
		assertEquals(10, result.asList().size());
		result = _con.getStatements(null, null,
				null, true);
		int i = 0;
		while (result.hasNext()) {
			Statement statement = result.next();
			if (statement.getPredicate().equals(RDFS.SEEALSO)){
				i++;
			}
				
		}
		assertEquals(6, i);
	}

	
	

}
