/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.simpleinterface;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;

import au.com.bytecode.opencsv.CSVWriter;

public class SPARQLResultsCSVWriter implements TupleQueryResultHandler {

	private CSVWriter writer;
	private String[] bindingNames;

	SPARQLResultsCSVWriter(OutputStream output) {
		char  seperator =  ';';		
		writer = new CSVWriter(new OutputStreamWriter(output), seperator);
	}

	public void endQueryResult() throws TupleQueryResultHandlerException {
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleSolution(BindingSet list)
			throws TupleQueryResultHandlerException {
		List<String> bindings = new ArrayList<String>();
		for (int i = 0; i < bindingNames.length; i++) {
			bindings.add(list.getBinding(bindingNames[i]).getValue()
					.stringValue());
		}
		writer.writeNext((String[]) bindings.toArray(new String[0]));
	}

	public void startQueryResult(List<String> bindingNames)
			throws TupleQueryResultHandlerException {
		this.bindingNames = (String[]) bindingNames.toArray(new String[0]);
		writer.writeNext(this.bindingNames);
	}

}
