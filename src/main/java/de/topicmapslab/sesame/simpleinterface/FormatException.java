/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.simpleinterface;


/**
 * An FormatException that can be thrown by an {@link TMConnector} when 
 * it encounters an invalid combination of Query type and {@link SPARQLResultFormat},
 * or in case a wrong serialization format is given for 
 * {@link TMConnector#addRDF(String, org.openrdf.rio.RDFFormat, java.io.InputStream)}.
 * 
 * @author Arnim Bleier
 * 
 */
public class FormatException extends RuntimeException {

	private static final long serialVersionUID = 5534304917478834240L;

	/**
	 * @param message
	 */
	public FormatException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FormatException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FormatException(String message, Throwable cause) {
		super(message, cause);
	}

}
