/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.simpleinterface;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;

public class HtmlTableResultWriter implements TupleQueryResultHandler {

	
	private final String NEWLINE =  System.getProperty("line.separator");
	private final String TAB = "  ";
	List<String> vars = new ArrayList<String>();
	private Writer out;



	public HtmlTableResultWriter(OutputStream out) {
		this.out = new OutputStreamWriter(out, Charset.defaultCharset());
	}

	public void endQueryResult() throws TupleQueryResultHandlerException {
		try {
			out.write("</table>");
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void handleSolution(BindingSet list)
			throws TupleQueryResultHandlerException {
		try {
			out.write(TAB + "<tr>"+ NEWLINE);
		Iterator<String> varsIterator = vars.iterator();
		while (varsIterator.hasNext()) {
			String string = varsIterator.next();
			try {
				out.write(TAB + TAB + "<td>" + list.getBinding(string).getValue().stringValue() + "</td>"+ NEWLINE);			
			} catch (NullPointerException e) {
				throw new TupleQueryResultHandlerException("Binding for variable \"?" + string + "\" not found.", e.getCause());
			}
		}

		out.write(TAB + "</tr>" + NEWLINE);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
	}

	public void startQueryResult(List<String> list)
			throws TupleQueryResultHandlerException {
		try {
		out.write("<table class=\"sparql\" border=\"1\">" + NEWLINE);
		out.write(TAB + "<tr>" + NEWLINE);
		
		Iterator<String> listIterator = list.iterator();
		while (listIterator.hasNext()) {
			String ne = listIterator.next();
			vars.add(ne);
			out.write(TAB + TAB + "<th>" + ne + "</th>" + NEWLINE);
			
		}
		
		out.write(TAB + "</tr>" + NEWLINE);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
	}
	
	

}
