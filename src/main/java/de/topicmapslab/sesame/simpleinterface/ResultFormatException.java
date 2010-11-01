/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.simpleinterface;


/**
 * An RuntimeException that can be thrown by an {@link TMConnector} when 
 * it encounters an invalid combination of Query type and {@link SPARQLResultFormat}.
 * 
 * @author Arnim Bleier
 * 
 */
public class ResultFormatException extends RuntimeException {

	private static final long serialVersionUID = 5534304917478834240L;

	/**
	 * @param message
	 */
	public ResultFormatException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ResultFormatException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ResultFormatException(String message, Throwable cause) {
		super(message, cause);
	}

}
