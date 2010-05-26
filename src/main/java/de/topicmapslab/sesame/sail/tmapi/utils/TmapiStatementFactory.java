/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.XMLSchema;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;

/**
 * @author Arnim Bleier
 * 
 */
public class TmapiStatementFactory {

	private final ValueFactory valueFactory;

	public TmapiStatementFactory(ValueFactory valueFactory) {
		this.valueFactory = valueFactory;
	}

	public Statement create(Topic subject, Topic predicate, Topic object) {
		return create(getBestLocator(subject), getBestLocator(predicate),
				getBestLocator(object));
	}

	public Statement create(Topic subject, Topic predicate, Object object) {
		if (object instanceof Name) {
			return create(getBestLocator(subject), getBestLocator(predicate),
					(Name) object);
		} else if (object instanceof Occurrence) {
			return create(getBestLocator(subject), getBestLocator(predicate),
					(Occurrence) object);
		} else if (object instanceof Topic) {
			return create(getBestLocator(subject), getBestLocator(predicate),
					getBestLocator((Topic) object));
		}
		throw new RuntimeException();
	}

	public Statement create(Name name) {
		return create(getBestLocator(name.getParent()), getBestLocator(name
				.getType()), name);
	}

	public Statement create(Occurrence occurrence) {
		return create(getBestLocator(occurrence.getParent()),
				getBestLocator(occurrence.getType()), occurrence);
	}

	private Statement create(Locator subject, Locator predicate, Locator object) {
		return create(subject, predicate, locator2URI(object));
	}

	private Statement create(Locator subject, Locator predicate, Name object) {
		return create(subject, predicate, valueFactory.createLiteral(object
				.getValue(), valueFactory.createURI(XMLSchema.STRING
				.stringValue())));
	}

	private Statement create(Locator subject, Locator predicate,
			Occurrence object) {
		return create(subject, predicate, valueFactory.createLiteral(object
				.getValue(), locator2URI(object.getDatatype())));
	}

	private Statement create(Locator subject, Locator predicate, Value object) {
		return valueFactory.createStatement(locator2URI(subject),
				locator2URI(predicate), object);
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
