/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.simpleinterface;

/**
 * Constants for available SPARQL result formats.
 * 
 * @author Arnim Bleier
 *
 */

public class SPARQLResultFormat {
	
	/**
	 * XML Result format (TupleQuery and GraphQuery)
	 * @see <a href="http://www.w3.org/TR/REC-rdf-syntax/">RDF/XML Syntax Specification</a>
	 * @see <a href="http://www.w3.org/TR/rdf-sparql-XMLres/">SPARQL Query Results XML Format</a>
	 */
	public final static String XML = "xml";
	
	
	/**
	 * JSON Result format (TupleQuery only)
	 * @see <a href="http://www.w3.org/TR/rdf-sparql-json-res/">Serializing SPARQL Query Results in JSON</a>
	 */
	public final static String JSON = "json";
	
	
	/**
	 * Notation3 (N3) Result format (GraphQuery only)
	 * @see <a href="http://www.w3.org/DesignIssues/Notation3">Notation 3</a>
	 */
	public final static String N3 = "n3";
	
	
	/**
	 * HTML Result format (TupleQuery and GraphQuery)
	 * @see <a href="http://www.w3.org/TR/html401/struct/tables.html">HTML table</a>
	 * @see <a href="http://www.w3.org/DesignIssues/Notation3">Notation 3</a>
	 */
	public final static String HTML = "html";
	
	
	/**
	 * CSV Result format
	 * Resulting in a comma-separated values table 
	 * @see <a href="http://tools.ietf.org/html/rfc4180">Comma-Separated Values</a>
	 */
	public final static String CSV = "csv";

}
