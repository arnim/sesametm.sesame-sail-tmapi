/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.simpleinterface;

/**
 * A Class to provide a simplified interface <br>
 * to <a href="http://www.openrdf.org/">Sesame</a> for accessing <a href="http://www.topicmaps.org/">Topic Maps</a>.
 * 
 * @see <a href="http://www.openrdf.org/">Sesame</a>
 * @see <a href="http://www.topicmaps.org/">Topic Maps</a>
 * @see <a href="http://www.tmapi.org/2.0/">Topic Maps API (TMAPI) 2.0</a>
 * 
 * @author Arnim Bleier
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.Query;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.impl.DatasetImpl;
import org.openrdf.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailGraphQuery;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.SailException;
import org.tmapi.core.Locator;
import org.tmapi.core.TopicMapSystem;

import de.topicmapslab.sesame.sail.tmapi.TmapiStore;

public class TMConnector {

	private TopicMapSystem tms;
	private SailRepositoryConnection con;
	private ValueFactory valueFactory;

	/**
	 * 
	 * @param tms
	 *            The {@link TopicMapSystem} this should be initialized with.
	 * @throws SailException
	 * @throws RepositoryException
	 */
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

	/**
	 * Serializes a topic to N3
	 * 
	 * @param tmBaseIRI
	 *            The base IRI of the Topic Map
	 * @param reference
	 *            A Locator of the Topic
	 * @param out
	 *            The OutputStream to be written on
	 * @throws RepositoryException
	 * @throws RDFHandlerException
	 */
	public void getRDFN3(Locator tmBaseIRI, Locator reference, OutputStream out)
			throws RepositoryException, RDFHandlerException {
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

	/**
	 * Serializes a topic to RDF/XML
	 * 
	 * @param tmBaseIRI
	 *            The base IRI of the Topic Map
	 * @param reference
	 *            A Locator of the Topic
	 * @param out
	 *            The OutputStream to be written on
	 * @throws RepositoryException
	 * @throws RDFHandlerException
	 */
	public void getRDFXML(Locator tmBaseIRI, Locator reference, OutputStream out)
			throws RepositoryException, RDFHandlerException {
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

	/**
	 * Executes a SPARQL query
	 * 
	 * @param baseIRI
	 *            tmBaseIRI The base IRI of the Topic Map
	 * @param query
	 *            The query String
	 * @param out
	 *            out The OutputStream to be written on
	 * @throws MalformedQueryException
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws RDFHandlerException
	 * @throws TupleQueryResultHandlerException
	 */
	public void executeSPARQL(String baseIRI, String query, OutputStream out)
			throws MalformedQueryException, RepositoryException,
			QueryEvaluationException, RDFHandlerException,
			TupleQueryResultHandlerException {
		executeSPARQL(baseIRI, query, "xml", out);
	}

	/**
	 * Executes a SPARQL query
	 * 
	 * @param baseIRI
	 *            tmBaseIRI The base IRI of the Topic Map
	 * @param query
	 *            The query String
	 * @param demandType
	 *            The required {@link SPARQLResultFormat}
	 * @param out
	 *            out The OutputStream to be written on
	 * @throws MalformedQueryException
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws RDFHandlerException
	 * @throws TupleQueryResultHandlerException
	 */
	public void executeSPARQL(String baseIRI, String query, String demandType,
			OutputStream out) throws MalformedQueryException,
			RepositoryException, QueryEvaluationException, RDFHandlerException,
			TupleQueryResultHandlerException {
		demandType = demandType.toLowerCase();
		Query q = con.prepareQuery(QueryLanguage.SPARQL, query);

		if (baseIRI != null) {
			// assure that only the graph baseIRI can be queried
			DatasetImpl dataSet = new DatasetImpl();
			dataSet.addDefaultGraph(con.getValueFactory().createURI(baseIRI));
			q.setDataset(dataSet);
		}

		if (q.getClass() == SailGraphQuery.class) {
			// No CSV and JSON
			GraphQuery gq = null;
			try {
				gq = (GraphQuery) q;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (demandType.equals("n3"))
				gq.evaluate(new N3Writer(out));
			else if (demandType.equals("xml"))
				gq.evaluate(new RDFXMLWriter(out));
			else if (demandType.equals("html"))
				gq.evaluate(new N3Writer(out));
			else
				throw new FormatException(demandType
						+ " is not allowed in CONSTRUCT");

		} else {
			// No N3
			TupleQuery tq = null;
			try {
				tq = (TupleQuery) q;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (demandType.equals("csv"))
				tq.evaluate(new SPARQLResultsCSVWriter(out));
			else if (demandType.equals("json"))
				tq.evaluate(new SPARQLResultsJSONWriter(out));
			else if (demandType.equals("xml"))
				tq.evaluate(new SPARQLResultsXMLWriter(out));
			else if (demandType.equals("html"))
				tq.evaluate(new SPARQLResultsHTMLTableWriter(out));
			else
				throw new FormatException(demandType
						+ " is not allowed in SELECT");

		}
	}

	/**
	 * Adds RDF data from an InputStream to the Topic Map in the baseIRI.
	 * 
	 * @param baseIRI
	 * @param dataFormat
	 * @param in
	 * @throws RDFParseException
	 * @throws RepositoryException
	 * @throws IOException
	 */
	public void addRDF(String baseIRI, RDFFormat dataFormat, InputStream in)
			throws RDFParseException, RepositoryException, IOException {
		con.add(in, baseIRI, dataFormat, valueFactory.createURI(baseIRI));
	}

}
