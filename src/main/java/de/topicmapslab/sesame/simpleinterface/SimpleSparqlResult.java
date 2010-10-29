/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.simpleinterface;

public class SimpleSparqlResult {
	
	public final static String XML = "xml";
	public final static String JSON = "json";
	public final static String CSV = "csv";
	public final static String N3 = "n3";
	public final static String TEXT = "text";
	public final static String HTML = "html";


	private SparqlResultError err = null;
	private String result = null;

	
	public SparqlResultError getError(){
		return err;
	}
	
	public void setError(String e){
		this.err = new SparqlResultError(e, 400);
	}
	
	public String getResult(){
		return result;
	}
	
	public void setResult(String r){
	this.result = r;
	}
	

}
