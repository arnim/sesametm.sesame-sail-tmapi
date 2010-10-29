/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.simpleinterface;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

public class TMConnectorTest {
	
	
	
	private class StringOutputStream extends OutputStream {
		private StringBuilder string = new StringBuilder();
		private String s;

		@Override
		public void write(int b) throws IOException {
			this.string.append((char) b);
		}

		/*
		 * Is changing the Stream!
		 */
		@Override
		public String toString() {
			s = this.string.toString();
			string =  new StringBuilder();
			return s;
		}
	};
	

	private TMConnector _sesameConnector;
	private TopicMap _tm;
	private Topic _mary;
	OutputStream _out; 


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
		_out = new StringOutputStream();
	}

	@Test
	public void testGetRDFXML() throws Exception {

		_sesameConnector.getRDFXML(_tm.createLocator("http://www.example.com/tm"),
				_tm.createLocator("http://www.example.com/tm#John"), _out);
		assertEquals(482, _out.toString().length());

	}

	@Test
	public void testGetRDFN3() throws Exception {

		_sesameConnector.getRDFN3(_tm.createLocator("http://www.example.com/tm"),
				_tm.createLocator("http://www.example.com/tm#John"), _out);

		assertEquals(208, _out.toString().length());


		 _sesameConnector.getRDFN3(_tm.createLocator("http://www.example.com/tm"),
				_tm.createLocator("http://www.example.com/tm#Beloved"), _out);
		assertEquals(105, _out.toString().length());

	}

	
// if Failing WHY
//	@Test
//	public void testSparqlXML() throws Exception {
//
//		
//		String queryString = "SELECT * WHERE  { ?s ?p ?o }";
//		_sesameConnector.executeSPARQL(
//				"http://www.example.com/tm", queryString, _out);
//		String result = _out.toString();
//		
//		System.out.println(result);
//
//		assertTrue(result.contains("<uri>http"));
//		assertTrue(result.length() > 500);
//		assertTrue(result == null);
//	}

	@Test
	public void testSparqlN3() throws Exception {


		String queryString = "CONSTRUCT {?s ?p ?o . } WHERE  { ?s ?p ?o }";
		_sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, _out);
		String result = _out.toString();
		
		System.out.println(result);
		
		assertTrue(result.contains("<rdf:Description rdf:about="));
		assertTrue(result.length() > 100);
		assertTrue(result == null);

	}

	@Test
	public void testSparqlN3HTML() throws Exception {

		String queryString = "CONSTRUCT {?s ?p ?o . } WHERE  { ?s ?p ?o }";
		_sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "html", _out);
		String result = _out.toString();
		assertTrue(result.length() > 100);
		assertTrue(result.contains("<pre>"));
		assertTrue(result.contains("&lt;http:"));
	}

	@Test
	public void testSparqlFALTY() throws Exception {
		try {
			String queryString = "CONSTRUCT {?s ?p ?o  WHERE  { ?s ?p ?o }";
			_sesameConnector.executeSPARQL(
					"http://www.example.com/tm", queryString, _out);
			fail("Syntax Error not raised!");
		} catch (Exception e) {
		}
		
	}

	@Test
	public void testSparqlIsLive() throws Exception {
		_mary.createName("nachträglich");
		String queryString = "SELECT ?s ?p ?o WHERE  { ?s ?p ?o }";
		_sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "html", _out);	
		String result = _out.toString();
		assertTrue(result.length() > 500);
		assertTrue(result.contains("nachträglich"));
		assertTrue(result.contains("<td>http"));
	}

	@Test
	public void testSparqlNonMatchingVar() throws Exception {
		String queryString = "SELECT ?wo ?name WHERE  { ?s ?p ?o }";
		try {
			_sesameConnector.executeSPARQL(
					"http://www.example.com/tm", queryString, "html", _out);
			fail("Unbound variables ?wo ?name");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@Test
	public void testSparqlNonMatchingVarXML() throws Exception {
		String queryString = "SELECT ?wo ?name WHERE  { ?s ?p ?o }";
		_sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "xml", _out);

		assertTrue(_out.toString().contains("wo"));
	}

	@Test
	public void testSparqlSxO() throws Exception {

		Topic prob2 = _tm.createTopicBySubjectIdentifier(_tm
				.createLocator("http://en.wikipedia.org/wiki/Saxony"));
		prob2.addType(_tm
						.createTopicBySubjectIdentifier(_tm
								.createLocator("http://en.wikipedia.org/wiki/State_(administrative_division)")));

		String queryString = "CONSTRUCT {<http://en.wikipedia.org/wiki/Saxony> a <http://en.wikipedia.org/wiki/State_(administrative_division)>} "
				+ " WHERE  { <http://en.wikipedia.org/wiki/Saxony> a <http://en.wikipedia.org/wiki/State_(administrative_division)> }";
		_sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, _out);
		assertTrue(_out.toString().contains(
						"State_(administrative_division)"));

	}

	@Test
	public void testSparqlSeeAlso() throws Exception {
		String queryString = ""
				+ "CONSTRUCT {?s  <http://www.w3.org/2000/01/rdf-schema#seeAlso>  ?o } "
				+ "WHERE {?s  <http://www.w3.org/2000/01/rdf-schema#seeAlso>  ?o }";
		_sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, _out);
		assertTrue(_out.toString().length() > 100);
	}
	
	
	@Test
	public void testSparqlBadConstrct() throws Exception {
		String queryString = ""
				+ "CONSTRUCT {?s  <http://www.w3.org/2000/01/rdf-schema#seeAlso>  ?M } "
				+ "WHERE {?s  <http://www.w3.org/2000/01/rdf-schema#seeAlso>  ?o }";
		_sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, _out);		
		assertTrue(_out.toString().length() < 200);
	}
	
	
	
	
	@Test
	public void testSparqlJSONSelect() throws Exception {
		String queryString = "SELECT ?s ?p ?o WHERE  { ?s ?p ?o }";
		_sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "json", _out);
		assertTrue(_out.toString().length() > 500);
	}
	

	
	
	@Test
	public void testSparqlJSONConstruct() throws Exception {
		String queryString = "CONSTRUCT { ?s ?p ?o } WHERE  { ?s ?p ?o }";
		
		try {
			_sesameConnector.executeSPARQL(
					"http://www.example.com/tm", queryString, "json", _out);
			fail("It schould not be possible to CONSTRUCT JSON");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@Test
	public void testSparqlCSVConstruct() throws Exception {
		String queryString = "CONSTRUCT { ?s ?p ?o } WHERE  { ?s ?p ?o }";
		
		try {
			_sesameConnector.executeSPARQL(
					"http://www.example.com/tm", queryString, "csv", _out);
			fail("It schould not be possible to CONSTRUCT CSV");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	
	@Test
	public void testSparql2CSV() throws Exception {
		String queryString = "SELECT * WHERE  { ?instance ?p ?type }";
		_sesameConnector.executeSPARQL(
				"http://www.example.com/tm", queryString, "csv", _out);
		assertTrue(_out.toString().contains("oved\";\"http://"));
	}

}
