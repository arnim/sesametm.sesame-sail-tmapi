/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.simpleinterface;

public class SparqlResultError {
	
	private String msg;
	private int code;

	public SparqlResultError(String msg, int code){
		this.setMessage(msg);
		this.setCode(code);
	}

	/**
	 * @param msg the msg to set
	 */
	private void setMessage(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the msg
	 */
	public String getMessage() {
		return msg;
	}

	/**
	 * @param code the code to set
	 */
	private void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

}
