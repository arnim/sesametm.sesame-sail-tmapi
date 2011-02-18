/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.simpleinterface;


/**
 * An SailTmapiException that can be thrown by an {@link TMConnector}.
 * 
 * @author Arnim Bleier
 * 
 */


public class SailTmapiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SailTmapiException(String message) {
		super(message);
	}

	public SailTmapiException(Throwable cause) {
		super(cause);
	}

	public SailTmapiException(String message, Throwable cause) {
		super(message, cause);
	}



}
