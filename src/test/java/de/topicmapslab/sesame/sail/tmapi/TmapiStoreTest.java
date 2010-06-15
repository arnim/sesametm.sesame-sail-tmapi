/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.junit.Before;
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
import org.tmapi.core.TopicMapExistsException;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

/**
 * @author Arnim Bleier
 * 
 */
public class TmapiStoreTest extends TestCase {

	private static TmapiStore _sail;
	private static Repository _tmapiRepository;
	private static RepositoryConnection _con;
	private static TopicMapSystem _tms;

	final static String baseIRI = "http://www.topicmapslab.de/test/base/";

	@Before
	public void setUp() throws Exception {
		_tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		populateMap(0, 1);
	}

	private void populateMap(int count, int individuals)
			throws TopicMapExistsException {
		TopicMap tm = _tms.createTopicMap(baseIRI + "_" + count);
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

		for (int i = 0; i < individuals; i++) {
			Topic alf = tm.createTopicBySubjectIdentifier(tm
					.createLocator(baseIRI + "alf_" + count + "-" + i));
			Topic bert = tm.createTopicBySubjectIdentifier(tm
					.createLocator(baseIRI + "bert_" + (count + "-" + i)));
			alf.createOccurrence(hourlyWage, "14.50", tm
					.createLocator(XMLSchema.FLOAT.stringValue()));
			bert.createOccurrence(hourlyWage, "25.40", tm
					.createLocator(XMLSchema.FLOAT.stringValue()));

			Association awf = tm.createAssociation(worksFor);
			Association bwf = tm.createAssociation(worksFor);

			awf.createRole(employee, alf);
			awf.createRole(employer, xyz);

			bwf.createRole(employee, bert);
			bwf.createRole(employer, xyz);
		}

	}

	protected void _toN3() throws Exception {
		RDFHandler rdfWriter = new N3Writer(System.out);
		_con.exportStatements(null, null, null, true, rdfWriter);
	}

	protected void _testGetContextIDs() throws Exception {
		assertEquals(1, _con.getContextIDs().asList().size());
		assertEquals(baseIRI + "_0", _con.getContextIDs().next().stringValue());
	}

	protected void _testSsparqlConstruct() throws Exception {
		String queryString = "CONSTRUCT   { <http://www.topicmapslab.de/test/base/subject> <http://www.topicmapslab.de/test/base/predicate> ?o . }  WHERE   { <http://www.topicmapslab.de/test/base/alf_0-0> <http://www.topicmapslab.de/test/base/employer> ?o . ?s ?p ?o . }";
		// String queryString =
		// "CONSTRUCT   { <http://www.topicmapslab.de/test/base/subject> <http://www.topicmapslab.de/test/base/predicate> ?o . }  WHERE   { <http://www.topicmapslab.de/test/base/alf_0> <http://www.topicmapslab.de/test/base/hourlyWage> ?o . ?s ?p ?o . }";
		// TODO see SPARQL specks!
		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL,
				queryString);
		GraphQueryResult result = query.evaluate();
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/subject", statement
				.getSubject().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/predicate",
				statement.getPredicate().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement
				.getObject().stringValue());
		assertFalse(result.hasNext());
	}

	protected void _testSELECT() throws Exception {

		OutputStream output = new OutputStream() {
			private StringBuilder string = new StringBuilder();

			@Override
			public void write(int b) throws IOException {
				this.string.append((char) b);
			}

			public String toString() {
				return this.string.toString();
			}
		};

		SPARQLResultsXMLWriter sparqlWriter = new SPARQLResultsXMLWriter(output);
		String queryString = "PREFIX base:    <" + baseIRI + "> "
				+ "SELECT ?person ?employer ?wage "
				+ "WHERE  { ?person base:employer ?employer . "
				+ "?person base:hourlyWage ?wage . " + "FILTER (?wage < 20) }";
		TupleQuery query = _con.prepareTupleQuery(QueryLanguage.SPARQL,
				queryString);
		query.evaluate(sparqlWriter);
		assertTrue(output.toString().contains("14.50"));
	}

	protected void _testSPO() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bert_0-0"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/employer"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/xyz"), true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert_0-0", statement
				.getSubject().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/employer", statement
				.getPredicate().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement
				.getObject().stringValue());
		assertFalse(result.hasNext());

		result = _con.getStatements(_con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/wrong"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/employer"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/xyz"), true);
		assertFalse(result.hasNext());

		result = _con.getStatements(_con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/bert_0-0"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/wrong"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/xyz"), true);
		assertFalse(result.hasNext());

		result = _con.getStatements(_con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/bert_0"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/employer"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/wrong"), true);
		assertFalse(result.hasNext());
	}

	protected void _testSxx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bert_0-0"), null,
				null, true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert_0-0", statement
				.getSubject().stringValue());
		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/bert_0-0", statement
				.getSubject().stringValue());
		assertFalse(result.hasNext());
		result = _con
				.getStatements(_con.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/wrong"), null,
						null, true);
		assertFalse(result.hasNext());
		
//		
//		result = _con
//		.getStatements(_con.getValueFactory().createURI(
//				"http://www.topicmapslab.de/test/base/xyz"), null,
//				null, true);
//		
//		System.out.println(result.asList());
		
	}

	protected void _testSPx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(_con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/bert_0-0"), _con
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
				"http://www.topicmapslab.de/test/base/bert_0-0"), _con
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
				"http://www.topicmapslab.de/test/base/bert_0-0"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/wrong"), null,
				true);
		assertFalse(result.hasNext());

	}

	protected void _testxPx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(null, _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/hourlyWage"),
				null, true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/hourlyWage",
				statement.getPredicate().stringValue());
		String subject = statement.getSubject().stringValue();
		if (subject.equals("http://www.topicmapslab.de/test/base/bert_0-0")) {
			// everything is fine
		} else if (subject
				.equals("http://www.topicmapslab.de/test/base/alf_0-0")) {
			// everything is fine
		} else {
			fail("no propper Subject");
		}
		assertEquals("http://www.topicmapslab.de/test/base/hourlyWage",
				statement.getPredicate().stringValue());

		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/hourlyWage",
				statement.getPredicate().stringValue());
		String subject2 = statement.getSubject().stringValue();
		if (subject.equals("http://www.topicmapslab.de/test/base/bert_0-0")) {
			// everything is fine
		} else if (subject
				.equals("http://www.topicmapslab.de/test/base/alf_0-0")) {
			// everything is fine
		} else {
			fail("no propper Subject");
		}
		assertFalse(subject.equals(subject2));
		assertFalse(result.hasNext());

		result = _con.getStatements(null, _con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/alf_0-0"), null, true);
		assertFalse(result.hasNext());

		result = _con.getStatements(null, _con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/worksFor"), null, true);
		assertFalse(result.hasNext());
	}

	protected void _testxPO() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(null, _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/employer"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/xyz"), true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/employer", statement
				.getPredicate().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement
				.getObject().stringValue());

		assertTrue(result.hasNext());
		statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/employer", statement
				.getPredicate().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement
				.getObject().stringValue());
		assertFalse(result.hasNext());

		result = _con.getStatements(null, _con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/wrong"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/xyz"), true);
		assertFalse(result.hasNext());

		result = _con.getStatements(null, _con.getValueFactory().createURI(
				"http://www.topicmapslab.de/test/base/employer"), _con
				.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/wrong"), true);
		assertFalse(result.hasNext());
	}

	protected void _testxxO() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(null, null,
				_con.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/alf_0-0"), true);
		assertTrue(result.hasNext());
		Statement statement = result.next();
		assertEquals("http://www.topicmapslab.de/test/base/xyz", statement
				.getSubject().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/employee", statement
				.getPredicate().stringValue());
		assertEquals("http://www.topicmapslab.de/test/base/alf_0-0", statement
				.getObject().stringValue());
		assertFalse(result.hasNext());

		result = _con.getStatements(null, null, _con.getValueFactory()
				.createURI("http://www.topicmapslab.de/test/base/wrong"), true);
		assertFalse(result.hasNext());
	}

	protected void _testxxx() throws Exception {
		RepositoryResult<Statement> result = _con.getStatements(null, null,
				null, true);
		assertEquals(6, result.asList().size());
	}

	protected void _testPerformance() throws Exception {
		for (int i = 1; i < 100; i++) {
			populateMap(i, 500);

		}
		System.out
				.println("Query performance on 100 Topic Maps with 500 indufidual sets "
						+ "conforming to the schema of Kal Ahmed "
						+ "and a "
						+ _sail.getConfiguration() + " configuration:");
		if (_sail.getConfiguration() == CONFIG.INDEXED)
			_sail.index();
		System.out.println("Test getStatements(null, null, null)");
		Statement statement;
		long start = System.currentTimeMillis();
		RepositoryResult<Statement> result = _con.getStatements(null, null,
				null, true);
		long repositoryResultTime = System.currentTimeMillis();
		System.out.println("Generating RepositoryResult Iterable: "
				+ (System.currentTimeMillis() - start) + "ms");
		while (result.hasNext()) {
			statement = result.next();
		}
		System.out.println("Iterating Result: "
				+ (System.currentTimeMillis() - repositoryResultTime) + "ms");
		System.out.println("Total time elapsed: "
				+ (System.currentTimeMillis() - start) + "ms");

		System.out.println("======================================");
		System.out.println();

	}

	protected void _testContextDependency() throws RepositoryException {
		assertEquals(
				_con.getStatements(null, null, null, true).asList().size(),
				_con.getStatements(
						null,
						null,
						null,
						true,
						_con.getValueFactory().createURI(
								"http://www.topicmapslab.de/test/base/_0"))
						.asList().size());
		assertEquals(0, _con.getStatements(
				null,
				null,
				null,
				true,
				_con.getValueFactory().createURI(
						"http://www.topicmapslab.de/test/base/_wrong"))
				.asList().size());
	}

	/**
	 * Tests against an indexed store.
	 * 
	 */
	@Test
	public void testLive() throws Exception {
		_sail = new TmapiStore(_tms, CONFIG.LIVE);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
		_testGetContextIDs();
		_testContextDependency();
		_testSPO();
		_testSxx();
		_testSPx();
		_testxPx();
		_testxPO();
		_testxxO();
		_testxxx();
		_testSELECT();
		_testSsparqlConstruct();
//		_testPerformance(); //not resource friendly
	}

	/**
	 * Tests against an indexed store.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIndexed() throws Exception {
		_sail = new TmapiStore(_tms, CONFIG.INDEXED);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
		_testGetContextIDs();
		_testContextDependency();
		_testSPO();
		_testSxx();
		_testSPx();
		_testxPx();
		_testxPO();
		_testxxO();
		_testxxx();
		_testSELECT();
		_testSsparqlConstruct();
//		_testPerformance(); // not resource friendly
	}

//	/**
//	 * Tests against the TMQL engine.
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	public void testTMQL() throws Exception {
//		_sail = new TmapiStore(_tms, CONFIG.TMQL);
//		_tmapiRepository = new SailRepository(_sail);
//		_tmapiRepository.initialize();
//		_con = _tmapiRepository.getConnection();
//		 _testGetContextIDs();
//		 _testContextDependency();
//		 _testSPO();
//		 _testSxx();
//		 _testSPx();
//		 _testxPx();
//		 _testxPO();
//		 _testxxO();
//		 _testxxx();
//		 _testSELECT();
//		 _testSsparqlConstruct();
////		 _testPerformance(); //not resource friendly
//	}

}
