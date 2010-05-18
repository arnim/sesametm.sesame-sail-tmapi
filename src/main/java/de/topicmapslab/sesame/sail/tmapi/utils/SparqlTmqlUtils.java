/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */
package de.topicmapslab.sesame.sail.tmapi.utils;

import org.tmapi.core.Locator;

/**
 * Utility class to convert a triple to a TMQL query.
 * 
 * @author Sven Krosse
 * 
 */
public class SparqlTmqlUtils {

	private static final String template = "SELECT %subj / tm:name, %pred / tm:name, %obj / tm:name "
			+ "WHERE tm:subject ( tm:subject : %subj , %pred : %obj , ...) ";

	public static String toPredicateQuery(Locator subj, Locator pred,
			Locator obj) {
		String t = new String(template);

		if (subj != null) {
			t = t.replaceAll("%subj", subj.getReference());
		} else {
			t = t.replaceAll("%subj", "\\$subj");
		}
		if (pred != null) {
			t = t.replaceAll("%pred", pred.getReference());
		} else {
			t = t.replaceAll("%pred", "\\$pred");
		}
		if (obj != null) {
			t = t.replaceAll("%obj", obj.getReference());
		} else {
			t = t.replaceAll("%obj", "\\$obj");
		}
		return t;
	}

	private static final String template2 = "FOR $sub IN %subj " + " RETURN {"
			+ " FOR $pred IN %pred " + " RETURN { "
			+ " FOR $obj IN $sub >> characteristics $pred %obj"
			+ " RETURN $sub, $pred, $obj" + " }" + " }";

	public static String toCharacteristicsQuery(Locator subj, Locator pred,
			Locator obj) {

		String t_ = new String(template2);
		if (subj != null) {
			t_ = t_.replaceAll("%subj", subj.getReference());
		} else {
			t_ = t_.replaceAll("%subj", "// tm:subject ");
		}
		if (pred != null) {
			t_ = t_.replaceAll("%pred", pred.getReference());
		} else {
			t_ = t_.replaceAll("%pred", " \\$subj >> characteristics >> types");
		}
		if (obj != null) {
			t_ = t_.replaceAll("%obj", "[ . >> atomify == \""
					+ obj.getReference() + "\" ]");
		} else {
			t_ = t_.replaceAll("%obj", "");
		}

		return t_;
	}

}
