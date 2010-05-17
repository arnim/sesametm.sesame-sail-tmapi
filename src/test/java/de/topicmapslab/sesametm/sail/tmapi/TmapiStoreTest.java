/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.sail.tmapi;

import static org.junit.Assert.assertEquals;

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
public class TmapiStoreTest {


	private static TmapiStore _sail;
	private static Repository _tmapiRepository;
	private static RepositoryConnection _con;
	private static TopicMap _tm;
	private static TopicMapSystem _tms;
	
	final static String baseIRI = "http://www.topicmapslab.de/test/base/";

	/**
	 * @throws java.lang.Exception
	 */
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


	protected void _testGetContextIDs() throws Exception {
		assertEquals(1,_con.getContextIDs().asList().size());
		assertEquals(baseIRI,_con.getContextIDs().next().stringValue());
	}

	
	

	protected void _testSELECT() throws Exception {
		SPARQLResultsXMLWriter sparqlWriter = new SPARQLResultsXMLWriter(System.out);
		String queryString = "PREFIX base:    <" + baseIRI + "> " +
				"SELECT ?person ?employer ?wage " +
				"WHERE  { ?person base:employer ?employer . " +
				"?person base:hourlyWage ?wage . " +
				"FILTER (?wage < 20) }";
		TupleQuery query = _con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
		query.evaluate(sparqlWriter);
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
		System.out.println(result.asList());
	}
	


	
	protected void _testTest() throws Exception {
		RDFHandler rdfWriter = new N3Writer(System.out);
		_con.exportStatements(null, _con.getValueFactory().createURI("http://www.topicmapslab.de/test/base/employer"), null, true, rdfWriter);
//		RepositoryResult<Statement> r = _con.getStatements(null, null, null, true);
//		System.out.println(r.next());
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
//		_testGetContextIDs();
//		_testTest();
//		_testGetGetObject();
//		_testGetPredicate();
//		_testGetSubject();

		_testGetGetSubjectObject();
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
//		_testSELECT();
//		_testGetGetObject();
//		_testTest();
//		_testSsparqlConstruct();
    }

	

}
