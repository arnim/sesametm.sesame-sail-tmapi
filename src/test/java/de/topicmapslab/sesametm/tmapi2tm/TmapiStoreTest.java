/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.tmapi2tm;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.sail.SailException;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

import de.topicmapslab.sesametm.tmapi2tm.model.SailTopic;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStoreTest {


	private static TmapiStore _sail;
	private static Repository _tmapiRepository;
	private static RepositoryConnection _con;
	private static TopicMap _tm;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_sail = new TmapiStore();
		_tmapiRepository = new SailRepository(_sail );
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
		_tm = _sail.getTmSystem().createTopicMap("http://www.base.com/iri/");
		Topic t1 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/Subject"));
		Topic t2 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/assoType"));
		t2.addItemIdentifier(_tm.createLocator("http://www.google.com/predicate-ii"));
		Topic t3 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/Object"));
		Topic rt1 =_tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/SubjectROletype"));
		Topic rt2 =_tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/ObjectROletype"));
		t3.createName(rt2,"object name", rt2);
		t3.createOccurrence(rt2, "object occ", rt2);
		t3.createOccurrence(rt2, _tm.createLocator("http://www.google.com/someURI"), rt2);

		Association asso = _tm.createAssociation(t2,new HashSet<Topic>());
		asso.createRole(rt1, t1);
		asso.createRole(rt2, t3);
	}	


	@Test
	public final void testGetContextIDs() throws Exception {
		assertEquals(1,_con.getContextIDs().asList().size());
		assertEquals("http://www.base.com/iri/",_con.getContextIDs().next().stringValue());
	}


	@Test
	public final void testSsparqlGraph() throws Exception {
		String queryString = "CONSTRUCT   { ?s <http://www.google.com/Subject> <http://www.google.com/Object>. }  WHERE   { ?s <http://www.google.com/Subject> <http://www.google.com/Object> . }";
		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
		GraphQueryResult result = query.evaluate();
//		System.out.println(result.hasNext());
//		System.out.println(result.next());
	}
	
//	
//	@Test
//	public final void testSsparqlSelect() throws Exception {
//		SPARQLResultsXMLWriter sparqlWriter = new SPARQLResultsXMLWriter(System.out);
//		String queryString = "PREFIX foaf:    <http://www.google.com/> " +
//				"SELECT ?name ?mbox2 " +
//				"WHERE  { ?name foaf:ObjectROletype ?mbox . ?name2 foaf:assoType ?mbox2 .}";
//		TupleQuery query = _con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
//		query.evaluate(sparqlWriter);
//	}
//
//
//
//
//	@Test
//	public final void testExport() throws Exception {
//		RDFHandler rdfWriter = new N3Writer(System.out);
//		_con.exportStatements(null, _con.getValueFactory().createURI("http://www.google.com/assoType"), null, true, rdfWriter);
//	}
//
//
//	@Test
//	public final void testNothing2BFound() throws Exception {
//		RepositoryResult<Statement> r = _con.getStatements(null, _con.getValueFactory().createURI("http://www.google.com/predicateo"), null, true);
//		assertFalse(r.hasNext());
//		r = _con.getStatements(null, _con.getValueFactory().createURI("http://www.google.com/assoType"), null, true);
//		assertTrue(r.hasNext());
//	}
//	
//
//	@Test
//	public final void testGetSPO() throws Exception {
//		RepositoryResult<Statement> r = _con.getStatements(null, _con.getValueFactory().createURI("http://www.google.com/assoType"), null, true);
//		System.out.println(r.asList());
//	}
	

	




}
