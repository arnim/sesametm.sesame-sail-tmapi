/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.simpleinterface;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.Query;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.impl.DatasetImpl;
import org.openrdf.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.SailException;
import org.tmapi.core.Locator;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.sesame.sail.tmapi.TmapiStore;

public class TMConnector {

	/**
	 * @author Arnim Bleier
	 * 
	 */

	private TopicMapSystem tms;
	private SailRepositoryConnection con;
	private ValueFactory valueFactory;

	public TMConnector(TopicMapSystem tms) throws SailException,
			RepositoryException {
		this.tms = tms;
		TmapiStore sail = new TmapiStore(this.tms);
		SailRepository repository = new SailRepository(sail);
		repository.initialize();
		con = repository.getConnection();
		valueFactory = con.getValueFactory();
	}

	private void rdfToString(String baseIRI, String resource,
			HashSet<Statement> rdfWriter) throws RepositoryException,
			RDFHandlerException {

		RepositoryResult<Statement> result2, resultStatementList = con
				.getStatements(valueFactory.createURI(resource), null, null,
						true, valueFactory.createURI(baseIRI));

		Statement s;
		while (resultStatementList.hasNext()) {
			s = resultStatementList.next();
			rdfWriter.add(s);
			try {
				result2 = con.getStatements((Resource) s.getObject(),
						RDFS.SEEALSO, null, true,
						valueFactory.createURI(baseIRI));
				while (result2.hasNext()) {
					rdfWriter.add(result2.next());
				}

				result2 = con.getStatements((Resource) s.getObject(), RDF.TYPE,
						null, true, valueFactory.createURI(baseIRI));
				while (result2.hasNext()) {
					rdfWriter.add(result2.next());
				}

			} catch (ClassCastException e) {
				// The Object is not a Resource
			}

		}

		resultStatementList = con.getStatements(null, null,
				valueFactory.createURI(resource), true,
				valueFactory.createURI(baseIRI));

		while (resultStatementList.hasNext()) {
			rdfWriter.add(resultStatementList.next());
		}

		resultStatementList = con.getStatements(null,
				valueFactory.createURI(resource), null, true,
				valueFactory.createURI(baseIRI));

		while (resultStatementList.hasNext()) {
			rdfWriter.add(resultStatementList.next());
		}

	}

	public void getRDFN3(Locator tmBaseIRI, Locator reference,
			OutputStream out) throws RepositoryException, RDFHandlerException {
		String baseIRI = tmBaseIRI.toExternalForm();
		String resource = reference.toExternalForm();
		RDFWriter rdfWriter = new N3Writer(out);

		HashSet<Statement> resultSet = new HashSet<Statement>();
		rdfToString(baseIRI, resource, resultSet);
		Iterator<Statement> statementIterator = resultSet.iterator();
		rdfWriter.startRDF();
		while (statementIterator.hasNext()) {
			rdfWriter.handleStatement(statementIterator.next());
		}
		rdfWriter.endRDF();
	}

	public void getRDFXML(Locator tmBaseIRI, Locator reference,
			OutputStream out) throws RepositoryException, RDFHandlerException {
		String baseIRI = tmBaseIRI.toExternalForm();
		String resource = reference.toExternalForm();
		RDFWriter rdfWriter = new RDFXMLPrettyWriter(out);

		HashSet<Statement> resultSet = new HashSet<Statement>();
		rdfToString(baseIRI, resource, resultSet);
		Iterator<Statement> statementIterator = resultSet.iterator();
		rdfWriter.startRDF();
		while (statementIterator.hasNext()) {
			rdfWriter.handleStatement(statementIterator.next());
		}
		rdfWriter.endRDF();
	}

	public void executeSPARQL(String baseIRI, String query,
			OutputStream out) {
		 executeSPARQL(baseIRI, query, "xml", out);
	}

	public void executeSPARQL(String baseIRI, String query,
			String demandType, OutputStream out) {
		demandType = demandType.toLowerCase();
		SimpleSparqlResult result = new SimpleSparqlResult();
		Query q = null;

		try {
			q = con.prepareQuery(QueryLanguage.SPARQL, query);

			// assert asure that only the graph baseIRI can be queried
			DatasetImpl dataSet = new DatasetImpl();
			dataSet.addDefaultGraph(con.getValueFactory().createURI(baseIRI));
			q.setDataset(dataSet);

			try {

				if (demandType.equals("xml")) {
					try {
						((TupleQuery) q).evaluate(new SPARQLResultsXMLWriter(
								out));
					} catch (ClassCastException e) {
						((GraphQuery) q)
								.evaluate(new RDFXMLPrettyWriter(out));
					}
				} else if (demandType.equals("html")) {
					HtmlQueryResult htmlResult = new HtmlQueryResult();
					try {
						((TupleQuery) q).evaluate(htmlResult);
						result.setResult(htmlResult.toString());
					} catch (ClassCastException e) {
						((GraphQuery) q).evaluate(new N3Writer(out));
						result.setResult("<pre>"
								+ out.toString().replaceAll("&", "&amp;")
										.replaceAll("\"", "&quot;")
										.replaceAll("<", "&lt;")
										.replaceAll(">", "&gt;") + "</pre>");
					}
				} else if (demandType.equals("json")) {
					try {
						((TupleQuery) q).evaluate(new SPARQLResultsJSONWriter(
								out));
					} catch (ClassCastException e) {
						result.setError(formatMissmatchErrorMessage(demandType,
								"SELECT"));
					}
				} else if (demandType.equals("csv")) {
					try {
						((TupleQuery) q).evaluate(new SPARQLResultsCSVWriter(
								out));
					} catch (ClassCastException e) {
						result.setError(formatMissmatchErrorMessage(demandType,
								"SELECT"));
					}
				} else if (demandType.equals("n3")) {
					try {
						((GraphQuery) q).evaluate(new N3Writer(out));
					} catch (ClassCastException e) {
						result.setError(formatMissmatchErrorMessage(demandType,
								"CONSTRUCT"));
					}
				}

				if (result.getResult() == null)
					result.setResult(out.toString());

			} catch (TupleQueryResultHandlerException e) {
				result.setError(e.getMessage());
			}

		} catch (Exception e) {
			// MalformedQuery
			result.setError(e.getMessage());
		}
	}

	private String formatMissmatchErrorMessage(String format, String style) {
		return "The result format " + format.toUpperCase()
				+ " is only available for " + style.toUpperCase() + " Queries";
	}

}
