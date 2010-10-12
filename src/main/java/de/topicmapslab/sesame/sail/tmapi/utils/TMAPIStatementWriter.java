/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.vocabulary.XMLSchema;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

/**
 * @author Arnim Bleier
 * 
 */
public class TMAPIStatementWriter {
	
	private TopicMap tm;
	private Topic subject;
	private Topic predicate;
	private Value object;

	public TMAPIStatementWriter(Resource subject, URI predicate, Value object, TopicMap tm){
		this.tm = tm;
		this.subject = createTopic(tm.createLocator(subject.stringValue()));
		this.predicate = createTopic(tm.createLocator(predicate.stringValue()));
		this.object = object;
	}
	
	public void write(){
		createAsCharacteristic();
		createAsAssociation();
	}
	
	
	private void createAsAssociation() {
		
	}

	private void createAsCharacteristic(){
		try {
			URI dt = ((LiteralImpl) object).getDatatype();
			if (dt != null)
				subject.createOccurrence(predicate, object.stringValue(), tm.createLocator(dt.stringValue()));
			else // default xsd:string as dataType
				subject.createOccurrence(predicate, object.stringValue(), tm.createLocator(XMLSchema.STRING.stringValue()));
		} catch (ClassCastException e) {
			// Has an URI as Object.
			subject.createOccurrence(predicate, object.stringValue(), tm.createLocator(XMLSchema.ANYURI.stringValue()));
		}
	}
	
	
	
	public Topic createTopic(Locator l) {
		Topic t = null;
		t = tm.getTopicBySubjectIdentifier(l);
		if (t != null)
			return t;
		t = tm.getTopicBySubjectLocator(l);
		if (t != null)
			return t;
		try {
			t = (Topic) tm.getConstructByItemIdentifier(l);
		} catch (ClassCastException e) {
			// Found Construct is not a Topic
		}
		return tm.createTopicByItemIdentifier(l);
	}

}
