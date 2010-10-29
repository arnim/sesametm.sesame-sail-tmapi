/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.simpleinterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;

public class HtmlQueryResult implements TupleQueryResultHandler {

	
	StringBuilder resultHTML = new StringBuilder();
	private final String NEWLINE =  System.getProperty("line.separator");
	private final String TAB = "  ";
	List<String> vars = new ArrayList<String>();



	public void endQueryResult() throws TupleQueryResultHandlerException {
		resultHTML.append("</table>");
	}

	public void handleSolution(BindingSet list)
			throws TupleQueryResultHandlerException {
		resultHTML.append(TAB + "<tr>"+ NEWLINE);
		Iterator<String> varsIterator = vars.iterator();
		while (varsIterator.hasNext()) {
			String string = varsIterator.next();
			try {
				resultHTML.append(TAB + TAB + "<td>" + list.getBinding(string).getValue().stringValue() + "</td>"+ NEWLINE);			
			} catch (NullPointerException e) {
				throw new TupleQueryResultHandlerException("Binding for variable \"?" + string + "\" not found.", e.getCause());
			}
		}

		resultHTML.append(TAB + "</tr>" + NEWLINE);
	}

	public void startQueryResult(List<String> list)
			throws TupleQueryResultHandlerException {
		resultHTML.append("<table class=\"sparql\" border=\"1\">" + NEWLINE);
		resultHTML.append(TAB + "<tr>" + NEWLINE);
		
		Iterator<String> listIterator = list.iterator();
		while (listIterator.hasNext()) {
			String ne = listIterator.next();
			vars.add(ne);
			resultHTML.append(TAB + TAB + "<th>" + ne + "</th>" + NEWLINE);
			
		}
		
		resultHTML.append(TAB + "</tr>" + NEWLINE);
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return resultHTML.toString();
	}

}
