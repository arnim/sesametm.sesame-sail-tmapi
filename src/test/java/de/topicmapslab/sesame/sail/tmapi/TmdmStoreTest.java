/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

/**
 * @author Arnim Bleier
 * 
 */
public class TmdmStoreTest extends TestCase {

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
		Topic hourlyWage = tm.createTopicBySubjectIdentifier(tm
				.createLocator(baseIRI + "hourlyWage"));
		Topic alf = tm.createTopicBySubjectIdentifier(tm.createLocator(baseIRI
				+ "alf"));
		Topic bert = tm.createTopicBySubjectIdentifier(tm.createLocator(baseIRI
				+ "bert"));
		alf.createOccurrence(hourlyWage, "14.50", tm
				.createLocator(XMLSchema.FLOAT.stringValue()));
		bert.createOccurrence(hourlyWage, "25.40", tm
				.createLocator(XMLSchema.FLOAT.stringValue()));
		
		Topic person = tm.createTopicBySubjectIdentifier(tm.createLocator("http://xmlns.com/foaf/0.1/Person"));
		alf.addType(person);
		bert.addType(person);


		Association awf = tm.createAssociation(worksFor);
		Association bwf = tm.createAssociation(worksFor);

		awf.createRole(employee, alf);
		awf.createRole(employer, xyz);

		bwf.createRole(employee, bert);
		bwf.createRole(employer, xyz);
		
		_sail = new TmapiStore(_tms);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
	}

	
	
	
//	/**
//	 * Test.
//	 * 
//	 */
//	@Test
//	public void testConstruct() throws Exception {
//		_sail = new TmapiStore(_tms, CONFIG.LIVE);
//		_tmapiRepository = new SailRepository(_sail);
//		_tmapiRepository.initialize();
//		_con = _tmapiRepository.getConnection();
////		RDFHandler rdfWriter = new N3Writer(System.out);
//
//		String queryString = "CONSTRUCT   { <http://www.topicmapslab.de/test/base/bert> ?p <http://xmlns.com/foaf/0.1/Person> . } " +
//				" WHERE   {  <http://www.topicmapslab.de/test/base/bert> ?p <http://xmlns.com/foaf/0.1/Person> .  }";
//	
//		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
//				queryString);
//		GraphQueryResult result = query.evaluate();
//		while (result.hasNext()) {
//			System.out.println( result.next());;
//			
//		}
//
//
//	}
	
	
	
	/**
	 * Test.
	 * 
	 */
	@Test
	public void testSxO() throws Exception {
		_sail = new TmapiStore(_tms);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();

		String queryString = "CONSTRUCT   { <http://www.topicmapslab.de/test/base/bert> ?p <http://xmlns.com/foaf/0.1/Person> . } " +
				" WHERE   {  <http://www.topicmapslab.de/test/base/bert> ?p <http://xmlns.com/foaf/0.1/Person> .  }";
	
		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
				queryString);
		GraphQueryResult result = query.evaluate();
		assertTrue(result.hasNext());
		Statement ne = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert", ne.getSubject().stringValue());
		assertEquals(RDF.TYPE, ne.getPredicate());
		assertEquals("http://xmlns.com/foaf/0.1/Person", ne.getObject().stringValue());

		assertFalse(result.hasNext());


	}
	
	
	
	
	
	
	/**
	 * Test.
	 * 
	 */
	@Test
	public void testSTO() throws Exception {
		String queryString = "CONSTRUCT   { <http://www.topicmapslab.de/test/base/alf> a <http://xmlns.com/foaf/0.1/Person> .  }  WHERE   { <http://www.topicmapslab.de/test/base/alf> a <http://xmlns.com/foaf/0.1/Person> . }";
		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
				queryString);
		GraphQueryResult result = query.evaluate();
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals(RDF.TYPE, statement.getPredicate());
		assertEquals(_con.getValueFactory().createURI("http://xmlns.com/foaf/0.1/Person"), statement.getObject());
		assertEquals(_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/alf"), statement.getSubject());
		assertFalse(result.hasNext());
	}
	
	
	
	
	/**
	 * Test.
	 * 
	 */
	@Test
	public void testSTx() throws Exception {
		String queryString = "CONSTRUCT   { ?s a ?o . }  WHERE   { ?s a ?o . }";
		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
				queryString);
		GraphQueryResult result = query.evaluate();
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals(RDF.TYPE, statement.getPredicate());
		assertEquals(_con.getValueFactory().createURI("http://xmlns.com/foaf/0.1/Person"), statement.getObject());
		Value firstObject = statement.getObject();
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals(RDF.TYPE, statement.getPredicate());
		assertEquals(_con.getValueFactory().createURI("http://xmlns.com/foaf/0.1/Person"), statement.getObject());
		assertFalse(firstObject.equals(statement.getSubject()));
		assertFalse(result.hasNext());
	}
	
	
	/**
	 * Test.
	 * 
	 */
	@Test
	public void testxTx() throws Exception {
		String queryString = "CONSTRUCT   { <http://www.topicmapslab.de/test/base/alf> a ?o . }  WHERE   { <http://www.topicmapslab.de/test/base/alf> a ?o . }";
		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
				queryString);
		GraphQueryResult result = query.evaluate();
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals(RDF.TYPE, statement.getPredicate());
		assertEquals(_con.getValueFactory().createURI("http://xmlns.com/foaf/0.1/Person"), statement.getObject());
		assertEquals(_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/alf"), statement.getSubject());
		assertFalse(result.hasNext());
	}
	
	
	/**
	 * Test.
	 * 
	 */
	@Test
	public void testxTO() throws Exception {
		String queryString = "CONSTRUCT   { ?s a <http://xmlns.com/foaf/0.1/Person> . }  WHERE   { ?s a <http://xmlns.com/foaf/0.1/Person> . }";
		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
				queryString);
		GraphQueryResult result = query.evaluate();
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals(RDF.TYPE, statement.getPredicate());
		assertEquals(_con.getValueFactory().createURI("http://xmlns.com/foaf/0.1/Person"), statement.getObject());
		Value firstObject = statement.getObject();
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals(RDF.TYPE, statement.getPredicate());
		assertEquals(_con.getValueFactory().createURI("http://xmlns.com/foaf/0.1/Person"), statement.getObject());
		assertFalse(firstObject.equals(statement.getSubject()));
		assertFalse(result.hasNext());
	}
	

	

	@Test
	public void testSxx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bert"), null,
				null, true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert", statement
				.getSubject().stringValue());
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert", statement
				.getSubject().stringValue());
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert", statement
				.getSubject().stringValue());
		assertFalse(result.hasNext());
		result = _con
				.getStatements(_con.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/wrong"), null,
						null, true);
		assertFalse(result.hasNext());
	}

	@Test
	public void testSPx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bert"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/hourlyWage"),
				null, true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/hourlyWage",
				statement.getPredicate().stringValue());
		assertEquals("25.40", statement.getObject().stringValue());
		assertEquals("\"25.40\"^^<http://www.w3.org/2001/XMLSchema#float>",
				statement.getObject().toString());
		assertFalse(result.hasNext());

		result = _con.getStatements(_con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/bert"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/employer"), null,
				true);
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement
				.getObject().stringValue());
		assertFalse(result.hasNext());

		result = _con.getStatements(_con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/wrong"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/employer"), null,
				true);
		assertFalse(result.hasNext());

		result = _con.getStatements(_con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/bert"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/wrong"), null,
				true);
		assertFalse(result.hasNext());

	}



	@Test
	public void testxxO() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(null, null,
				_con.getValueFactory().createURI(
						"http://xmlns.com/foaf/0.1/Person"), true);
		
		
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://xmlns.com/foaf/0.1/Person", statement
				.getObject().stringValue());
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://xmlns.com/foaf/0.1/Person", statement
				.getObject().stringValue());
		assertFalse(result.hasNext());

		result = _con.getStatements(null, null, _con.getValueFactory()
				.createURI("http://www.topicmapslab.de/test/base/wrong"), true);
		assertFalse(result.hasNext());
	}


	@Test
	public void testxxx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(null, null,
				null, true);
		assertEquals(8, result.asList().size());
	}

	
	

}
