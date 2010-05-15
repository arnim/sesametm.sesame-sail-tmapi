/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import java.util.Set;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;

/**
 * @author Arnim Bleier
 * 
 */
public class TmapiStatementHandler {

	private ValueFactory valueFactory;
	SailConnection con;
	private URI context;

	public TmapiStatementHandler(ValueFactory valueFactory, SailConnection con,
			URI baseIRI) throws SailException {
		this.valueFactory = valueFactory;
		this.con = con;
		this.context = baseIRI;
	}
	
	public void add(Topic subject, Topic predicate, Topic object){
		add(getBestLocator(subject), getBestLocator(predicate),
				getBestLocator(object));
	}

	public void add(Name name) {
		add(getBestLocator(name.getParent()), getBestLocator(name.getType()),
				name);
	}

	public void add(Occurrence occurrence) {
		add(getBestLocator(occurrence.getParent()), getBestLocator(occurrence
				.getType()), occurrence);
	}

	public void add(Locator subject, Locator predicate, Locator object) {
		add(subject, predicate, locator2URI(object));
	}

	public void add(Locator subject, Locator predicate, Name object) {
		add(subject, predicate, valueFactory.createLiteral(object.getValue(),
			valueFactory.createURI(XMLSchema.STRING.stringValue())));
	}

	public void add(Locator subject, Locator predicate, Occurrence object) {
		add(subject, predicate, valueFactory.createLiteral(object.getValue(),
				locator2URI(object.getDatatype())));
	}

	private void add(Locator subject, Locator predicate, Value object) {
		add(locator2URI(subject), locator2URI(predicate), object);
	}

	private void add(Resource s, URI p, Value o) {
		try {
			con.addStatement(s, p, o, context);
		} catch (SailException e) {
			e.printStackTrace();
		}
	}

	private URI locator2URI(Locator l) {
		return valueFactory.createURI(l.toExternalForm());
	}

	private Locator getBestLocator(Topic t) {
		Set<Locator> l;
		l = t.getItemIdentifiers();
		if (!l.isEmpty())
			return l.iterator().next();
		l = t.getSubjectIdentifiers();
		if (!l.isEmpty())
			return l.iterator().next();
		l = t.getSubjectLocators();
		return l.iterator().next();
	}

}
