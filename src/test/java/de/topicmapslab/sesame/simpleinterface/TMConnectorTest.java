/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.simpleinterface;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

import de.topicmapslab.sesame.simpleinterface.SimpleSparqlResult;

public class TMConnectorTest {

	private TMConnector _sesameConnector;
	private TopicMap _tm;
	private Topic _mary;

	@Before
	public void setUp() throws Exception {
		TopicMapSystem testTMS = TopicMapSystemFactory.newInstance()
				.newTopicMapSystem();
		_tm = testTMS.createTopicMap("http://www.example.com/tm");
		Topic john = _tm.createTopicByItemIdentifier(_tm
				.createLocator("http://www.example.com/tm#John"));
		_mary = _tm.createTopicByItemIdentifier(_tm
				.createLocator("http://www.example.com/tm#Mary"));
		Association love = _tm.createAssociation(_tm
				.createTopicByItemIdentifier(_tm
						.createLocator("http://www.example.com/tm#Love")));
		love.createRole(_tm.createTopicByItemIdentifier(_tm
				.createLocator("http://www.example.com/tm#Lover")), john);
		love.createRole(_tm.createTopicByItemIdentifier(_tm
				.createLocator("http://www.example.com/tm#Beloved")), _mary);
		_sesameConnector = new TMConnector(testTMS);
	}

	@Test
	public void testGetRDFXML() throws Exception {

		String result = _sesameConnector.getRDFXML(_tm.createLocator("http://www.example.com/tm"),
				_tm.createLocator("http://www.example.com/tm#John"));
		assertEquals(482, result.length());

	}

	@Test
	public void testGetRDFN3() throws Exception {

		String result = _sesameConnector.getRDFN3(_tm.createLocator("http://www.example.com/tm"),
				_tm.createLocator("http://www.example.com/tm#John"));

		assertEquals(208, result.length());

		result = _sesameConnector.getRDFN3(_tm.createLocator("http://www.example.com/tm"),
				_tm.createLocator("http://www.example.com/tm#Beloved"));
		assertEquals(105, result.length());

	}

	@Test
	public void testSparqlXML() throws Exception {
		String queryString = "SELECT * WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString);
				assertTrue(result.getResult().contains("<uri>http"));
		assertTrue(result.getResult().length() > 500);
		assertTrue(result.getError() == null);
	}

	@Test
	public void testSparqlN3() throws Exception {

		String queryString = "CONSTRUCT {?s ?p ?o . } WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString);
		assertTrue(result.getResult().contains("<rdf:Description rdf:about="));
		assertTrue(result.getResult().length() > 100);
		assertTrue(result.getError() == null);

	}

	@Test
	public void testSparqlN3HTML() throws Exception {

		String queryString = "CONSTRUCT {?s ?p ?o . } WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "html");
		assertTrue(result.getResult().length() > 100);
		assertTrue(result.getError() == null);
		assertTrue(result.getResult().contains("<pre>"));
		assertTrue(result.getResult().contains("&lt;http:"));
	}

	@Test
	public void testSparqlFALTY() throws Exception {
		String queryString = "CONSTRUCT {?s ?p ?o  WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString);
		assertTrue(result.getError() != null);
		assertTrue(result.getResult() == null);
	}

	@Test
	public void testSparqlIsLive() throws Exception {
		_mary.createName("nachträglich");
		String queryString = "SELECT ?s ?p ?o WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "html");		
		assertTrue(result.getResult().length() > 500);
		assertTrue(result.getResult().contains("nachträglich"));
		assertTrue(result.getResult().contains("<td>http"));
		assertTrue(result.getError() == null);
	}

	@Test
	public void testSparqlNonMatchingVar() throws Exception {
		String queryString = "SELECT ?wo ?name WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "html");
		assertTrue(result.getError() != null);
		assertTrue(result.getResult() == null);
		assertTrue(result.getError().getMessage().contains("\"?wo\""));
	}
	
	@Test
	public void testSparqlNonMatchingVarXML() throws Exception {
		String queryString = "SELECT ?wo ?name WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "xml");
		assertTrue(result.getError() == null);
		assertTrue(result.getResult() != null);
		assertTrue(result.getResult().contains("wo"));
	}

	@Test
	public void testSparqlSxO() throws Exception {

		Topic prob2 = _tm.createTopicBySubjectIdentifier(_tm
				.createLocator("http://en.wikipedia.org/wiki/Saxony"));
		prob2
				.addType(_tm
						.createTopicBySubjectIdentifier(_tm
								.createLocator("http://en.wikipedia.org/wiki/State_(administrative_division)")));

		String queryString = "CONSTRUCT {<http://en.wikipedia.org/wiki/Saxony> a <http://en.wikipedia.org/wiki/State_(administrative_division)>} "
				+ " WHERE  { <http://en.wikipedia.org/wiki/Saxony> a <http://en.wikipedia.org/wiki/State_(administrative_division)> }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString);
		assertTrue(result.getError() == null);
		assertTrue(result
				.getResult()
				.contains(
						"State_(administrative_division)"));

	}

	@Test
	public void testSparqlSeeAlso() throws Exception {
		String queryString = ""
				+ "CONSTRUCT {?s  <http://www.w3.org/2000/01/rdf-schema#seeAlso>  ?o } "
				+ "WHERE {?s  <http://www.w3.org/2000/01/rdf-schema#seeAlso>  ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString);
		assertTrue(result.getError() == null);
		assertTrue(result.getResult().length() > 100);
	}
	
	
	@Test
	public void testSparqlBadConstrct() throws Exception {
		String queryString = ""
				+ "CONSTRUCT {?s  <http://www.w3.org/2000/01/rdf-schema#seeAlso>  ?M } "
				+ "WHERE {?s  <http://www.w3.org/2000/01/rdf-schema#seeAlso>  ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString);		
		assertTrue(result.getError() == null);
		assertTrue(result.getResult().length() < 200);
	}
	
	
	
	
	@Test
	public void testSparqlJSONSelect() throws Exception {
		String queryString = "SELECT ?s ?p ?o WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "json");
		assertTrue(result.getResult().length() > 500);
		assertTrue(result.getError() == null);
	}
	
	// Wrong
	@Test
	public void testSparqlN3Select() throws Exception {
		String queryString = "SELECT ?s ?p ?o WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "n3");
		assertTrue(result.getResult().length() == 0);
//		System.out.println(result.getError());
//		assertTrue(result.getError() != null);
	}
	
	
	@Test
	public void testSparqlJSONConstruct() throws Exception {
		String queryString = "CONSTRUCT { ?s ?p ?o } WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "json");
		assertTrue(result.getResult().length() == 0);
		assertTrue(result.getError() != null);
		assertTrue(result.getError().getMessage().contains("e result format JSON is only availab"));
	}
	
	@Test
	public void testSparqlCSVConstruct() throws Exception {
		String queryString = "CONSTRUCT { ?s ?p ?o } WHERE  { ?s ?p ?o }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "csv");
		assertTrue(result.getResult().length() == 0);
		assertTrue(result.getError() != null);
		assertEquals(400, result.getError().getCode());

		assertTrue(result.getError().getMessage().contains("ormat CSV is only available for SELEC"));
	}
	
	
	@Test
	public void testSparql2CSV() throws Exception {
		String queryString = "SELECT * WHERE  { ?instance ?p ?type }";
		SimpleSparqlResult result = _sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "csv");
		assertTrue(result.getError() == null);
		assertTrue(result.getResult().contains("oved\";\"http://"));
	}

}
