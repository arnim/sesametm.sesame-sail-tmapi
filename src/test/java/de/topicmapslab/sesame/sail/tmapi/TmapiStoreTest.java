/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;


import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
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
@SuppressWarnings("unused")
public class TmapiStoreTest {


	private static TmapiStore _sail;
	private static Repository _tmapiRepository;
	private static RepositoryConnection _con;
	private static TopicMap _tm;
	private static TopicMapSystem _tms;
	
	final static String baseIRI = "http://www.topicmapslab.de/test/base/";


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		_tm = _tms.createTopicMap(baseIRI);
		

		Topic alf = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "alf"));
		Topic bert = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "bert"));
		Topic xyz = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "xyz"));
		Topic worksFor = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "worksFor"));
		Topic employer = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "employer"));
		Topic employee = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "employee"));
		Topic hourlyWage = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "hourlyWage"));

		alf.createOccurrence(hourlyWage, "14.50",
				_tm.createLocator(XMLSchema.FLOAT.stringValue()));
		bert.createOccurrence(hourlyWage, "25.40",
				_tm.createLocator(XMLSchema.FLOAT.stringValue()));

		Association awf = _tm.createAssociation(worksFor);
		Association bwf = _tm.createAssociation(worksFor);

		awf.createRole(employee, alf);
		awf.createRole(employer, xyz);
		
		bwf.createRole(employee, bert);
		bwf.createRole(employer, xyz);
	}	


	
	protected void _testSsparqlConstruct() throws Exception {
        String queryString = "CONSTRUCT   { ?s ?p ?o . }  WHERE   { ?s ?p ?o . ?s <http://www.topicmapslab.de/test/base/hourlyWage> ?o . }";
        GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
        GraphQueryResult result = query.evaluate();
        while (result.hasNext()) {
        	System.out.println(result.next());
		}
	}
	
	protected void _testGetPredicate() throws RepositoryException{
		RepositoryResult<Statement> result = _con.getStatements(null, _con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employer"), null, true);
		System.out.println(result.asList());
	}
	
	protected void _testGetGetObject() throws RepositoryException{
		RepositoryResult<Statement> result = _con.getStatements(null, null, _con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bert"),  true);
		System.out.println(result.asList());
	}
	
	protected void _testGetGetSubjectObject() throws RepositoryException{
		RepositoryResult<Statement> result = _con.getStatements(null, 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employee"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bert"),  true);
	}
	


	
	protected void _testTEST() throws Exception {
		RDFHandler rdfWriter = new N3Writer(System.out);
		_con.exportStatements(null, null, null, true, rdfWriter);
	}
	
	
	
	protected void _testGetContextIDs() throws Exception {
		assertEquals(1,_con.getContextIDs().asList().size());
		assertEquals(baseIRI,_con.getContextIDs().next().stringValue());
	}

	protected void _testSELECT() throws Exception {
		
		OutputStream output = new OutputStream()
	    {
	        private StringBuilder string = new StringBuilder();
	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }
	        public String toString(){
	            return this.string.toString();
	        }
	    };

		SPARQLResultsXMLWriter sparqlWriter = new SPARQLResultsXMLWriter(output);
		String queryString = "PREFIX base:    <" + baseIRI + "> " +
				"SELECT ?person ?employer ?wage " +
				"WHERE  { ?person base:employer ?employer . " +
				"?person base:hourlyWage ?wage . " +
				"FILTER (?wage < 20) }";
		TupleQuery query = _con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
		query.evaluate(sparqlWriter);
		assertTrue(output.toString().contains("14.50"));
	}
	
	
	protected void _testSPO() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bert"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employer"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/xyz"), 
				true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert", statement.getSubject().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/employer", statement.getPredicate().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement.getObject().stringValue());
		assertFalse(result.hasNext());
		
		result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/wrong"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employer"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/xyz"), 
				true);
		assertFalse(result.hasNext());
		
		result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bert"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/wrong"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/xyz"), 
				true);
		assertFalse(result.hasNext());
		
		result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bert"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employer"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/wrong"), 
				true);
		assertFalse(result.hasNext());
	}
	
	protected void _testSxx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bert"), 
				null, 
				null, 
				true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert", statement.getSubject().stringValue());
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert", statement.getSubject().stringValue());
		assertFalse(result.hasNext());
		result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/wrong"), 
				null, 
				null, 
				true);
		assertFalse(result.hasNext());
	}
	
	protected void _testSPx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bert"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/hourlyWage"), 
				null, 
				true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/hourlyWage", statement.getPredicate().stringValue());
		assertEquals("25.40", statement.getObject().stringValue());
		assertEquals("\"25.40\"^^<http://www.w3.org/2001/XMLSchema#float>", statement.getObject().toString());
		assertFalse(result.hasNext());
		
		result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bert"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employer"), 
				null, 
				true);
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement.getObject().stringValue());
		assertFalse(result.hasNext());
				
		result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/wrong"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employer"), 
				null, 
				true);
		assertFalse(result.hasNext());
		
		result = _con.getStatements(
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/bert"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/wrong"), 
				null, 
				true);
		assertFalse(result.hasNext());

	}
	
	protected void _testxPx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(
				null, 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/hourlyWage"), 
				null, 
				true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/hourlyWage", statement.getPredicate().stringValue());
		String subject =  statement.getSubject().stringValue();
		if (subject.equals("http://www.topicmapslab.de/test/base/bert")) {
			// everything is fine
		} else if (subject.equals("http://www.topicmapslab.de/test/base/alf")) {
			// everything is fine
		} else {
			fail("no propper Subject");
		}
		assertEquals("http://www.topicmapslab.de/test/base/hourlyWage", statement.getPredicate().stringValue());
	
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/hourlyWage", statement.getPredicate().stringValue());
		String subject2 =  statement.getSubject().stringValue();
		if (subject.equals("http://www.topicmapslab.de/test/base/bert")) {
			// everything is fine
		} else if (subject.equals("http://www.topicmapslab.de/test/base/alf")) {
			// everything is fine
		} else {
			fail("no propper Subject");
		}
		assertFalse(subject.equals(subject2));
		assertFalse(result.hasNext());	
		
		result = _con.getStatements(
				null, 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/alf"), 
				null, 
				true);
		assertFalse(result.hasNext());	
		
		result = _con.getStatements(
				null, 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/worksFor"), 
				null, 
				true);
		assertFalse(result.hasNext());	
	}

	protected void _testxPO() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(
				null, 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employer"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/xyz"), 
				true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/employer", statement.getPredicate().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement.getObject().stringValue());

		
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/employer", statement.getPredicate().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement.getObject().stringValue());
		assertFalse(result.hasNext());

		result = _con.getStatements(
				null, 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/wrong"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/xyz"), 
				true);
		assertFalse(result.hasNext());
		
		result = _con.getStatements(
				null, 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employer"), 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/wrong"), 
				true);
		assertFalse(result.hasNext());
	}
	
	protected void _testxxO() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(
				null, 
				null, 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/alf"), 
				true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement.getSubject().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/employee", statement.getPredicate().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/alf", statement.getObject().stringValue());
		assertFalse(result.hasNext());
		
		result = _con.getStatements(
				null, 
				null, 
				_con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/wrong"), 
				true);
		assertFalse(result.hasNext());
	}
	
    /**
     * Tests against an indexed store.
     * @throws Exception 
     */
	@Test
    public void testLive() throws Exception {
		_sail = new TmapiStore(_tms, CONFIG.LIVE);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
		_testGetContextIDs();
		_testSPO();
		_testSxx();
		_testSPx();
		_testxPx();
		_testxPO();
		_testxxO();
		_testSELECT();

//		_testTest();
//		_testGetGetObject();
//		_testGetPredicate();
//		_testGetSubject();

//		_testGetGetSubjectObject();
//		_testSELECT();
//		_testSsparqlConstruct();
    }
	
	
    /**
     * Tests against an indexed store.
     * @throws Exception 
     */
	@Test
    public void testIndexed() throws Exception {
		_sail = new TmapiStore(_tms, CONFIG.INDEXED);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
		_testGetContextIDs();
		_testSPO();
		_testSxx();
		_testSPx();
		_testxPx();
		_testxPO();
		_testxxO();
		_testSELECT();
		
//		_testGetGetObject();
//		_testTEST();
//		_testSsparqlConstruct();
    }
	
	
    /**
     * Tests against the TMQL engine.
     * @throws Exception 
     */
	@Test
    public void testTMQL() throws Exception {
		_sail = new TmapiStore(_tms, CONFIG.TMQL);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
		_testGetContextIDs();

//		_testSPO();
//		_testSxx();
//		_testSPx();
//		_testxPx();
//		_testxPO();
//		_testxxO();
//		_testSELECT();
		
//		_testGetGetObject();
//		_testTEST();
//		_testSsparqlConstruct();
    }


	

}
