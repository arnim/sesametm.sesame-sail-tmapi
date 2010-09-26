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
public class MaianaTest extends TestCase {

	private static TmapiStore _sail;
	private static Repository _tmapiRepository;
	private static RepositoryConnection _con;
	private static TopicMapSystem _tms;

	final static String baseIRI = "http://maiana.topicmapslab.de/u/uta/tm/toytm/";
	final static String wiki = "http://en.wikipedia.org/wiki/";


	@Override
	@Before
	public void setUp() throws Exception {
		_tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		TopicMap tm = _tms.createTopicMap(baseIRI);
		Topic deutschland = tm.createTopicBySubjectIdentifier(tm.createLocator(wiki
				+ "Deutschland"));
		Topic population = tm.createTopicBySubjectIdentifier(tm.createLocator(wiki
				+ "Population"));
		deutschland.createOccurrence(population, "82060000", tm
				.createLocator("http://www.w3.org/2001/XMLSchema#long"));
		
		
		_sail = new TmapiStore(_tms);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
	}

		
	/**
	 * Test.
	 * 
	 */
	@Test
	public void testSxx() throws Exception {
		_sail = new TmapiStore(_tms);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();


		String queryString = "PREFIX wiki: <http://en.wikipedia.org/wiki/>  " +
		" CONSTRUCT { <http://en.wikipedia.org/wiki/Deutschland> ?P ?O }" +
		"WHERE { <http://en.wikipedia.org/wiki/Deutschland> ?P ?O }";		
				
		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
				queryString);
		GraphQueryResult result = query.evaluate();
		assertTrue(result.hasNext());
		Statement ne = result.next();
		System.out.println(ne);
		assertEquals("http://en.wikipedia.org/wiki/Deutschland", ne.getSubject().stringValue());
		assertEquals("http://en.wikipedia.org/wiki/Population", ne.getPredicate().stringValue());
		assertEquals("82060000", ne.getObject().stringValue());
		assertEquals("\"82060000\"^^<http://www.w3.org/2001/XMLSchema#long>", ne.getObject().toString());

		assertFalse(result.hasNext());

	}
	


	
	

}
