/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.RDFS;
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


	
	@Test
	public void testxxO() throws Exception {
		int i = 0;
		RepositoryResult<Statement> result;
		
		
		result = _con.getStatements( null,
				null, 
				_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/t/http://www.topicmapslab.de/test/base/bertsi1"),
						true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		if (statement.getPredicate().equals(RDFS.SEEALSO))
			i++;
		assertEquals("http://www.topicmapslab.de/test/base/bertsi1", statement
				.getSubject().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/t/http://www.topicmapslab.de/test/base/bertsi1", statement
				.getObject().stringValue());
		assertFalse(result.hasNext());
		assertEquals(1, i);
		
		result = _con.getStatements( null,
				null, 
				_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/t/http://www.topicmapslab.de/test/base/bertsi_wrong"),
						true);
		assertFalse(result.hasNext());
		
		result = _con.getStatements( null,
				null, 
				_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test_wrong/base/t/http://www.topicmapslab.de/test/base/bertsi"),
						true);
		assertFalse(result.hasNext());
		
		
		result = _con.getStatements( null,
				null, 
				_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bertsi1"),
						true);
		assertEquals(1, result.asList().size());

	}
	
	
	
	
	@Test
	public void testxPx() throws Exception {
		RepositoryResult<Statement> result;
		
		
		result = _con.getStatements(null, RDFS.SEEALSO,
				null, true);
		assertTrue(result.hasNext());
		assertEquals(RDFS.SEEALSO, result.next().getPredicate());
		
		assertTrue(result.hasNext());
		assertEquals(RDFS.SEEALSO, result.next().getPredicate());
		
		assertTrue(result.hasNext());
		assertEquals(RDFS.SEEALSO, result.next().getPredicate());
		
		assertTrue(result.hasNext());
		assertEquals(RDFS.SEEALSO, result.next().getPredicate());
		
		assertTrue(result.hasNext());
		assertEquals(RDFS.SEEALSO, result.next().getPredicate());
		
		assertTrue(result.hasNext());
		assertEquals(RDFS.SEEALSO, result.next().getPredicate());

		assertFalse(result.hasNext());
		
		result = _con.getStatements(null, RDFS.LABEL,
				null, true);
		assertFalse(result.hasNext());
		
		
		result = _con.getStatements(null, _con.getValueFactory().createURI(baseIRI + "employee"),
				null, true);
		
		assertEquals(2, result.asList().size());

	}
	
	
	
	@Test
	public void testSxx() throws Exception {
		int i = 0;
		RepositoryResult<Statement> result = _con.getStatements(_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bertsi1"), null,
				null, true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		if (statement.getPredicate().equals(RDFS.SEEALSO)){
			assertEquals("http://www.topicmapslab.de/test/base/t/http://www.topicmapslab.de/test/base/bertsi1", statement
					.getObject().stringValue());
			i++;
			
		}

		assertEquals("http://www.topicmapslab.de/test/base/bertsi1", statement
				.getSubject().stringValue());

		assertTrue(result.hasNext());
		statement = result.next();
		if (statement.getPredicate().equals(RDFS.SEEALSO))
			i++;
		assertEquals("http://www.topicmapslab.de/test/base/bertsi1", statement
				.getSubject().stringValue());
		assertFalse(result.hasNext());
		assertEquals(1, i);
		
		result = _con.getStatements(_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bertsi_wrong"), null,
				null, true);
		assertFalse(result.hasNext());

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
