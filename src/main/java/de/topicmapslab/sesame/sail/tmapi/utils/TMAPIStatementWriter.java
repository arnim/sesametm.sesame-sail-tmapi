/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import java.util.Collection;
import java.util.Iterator;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.XMLSchema;
import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.LiteralIndex;

/**
 * @author Arnim Bleier
 * 
 */
public class TMAPIStatementWriter {
	
	private TopicMap tm;
	private Topic subject;
	private Topic predicate;
	private Value object;
	private LiteralIndex index;
	private boolean subjectIsNew = false;
	private String subjectValue;
	public final static String subjectRoleTypeString = "http://www.tmapi.org/2.0/api#SubjectRoleType";
	private Topic subjectRoleType;
	

	public TMAPIStatementWriter(Resource subject, URI predicate, Value object, TopicMap tm){
		this.tm = tm;
		this.subject = createTopic(tm.createLocator(subject.stringValue()));
		this.subjectValue = subject.stringValue();
		if (!predicate.stringValue().equals(RDF.TYPE.stringValue()))
			this.predicate = createTopic(tm.createLocator(predicate.stringValue()));
		this.object = object;
		index = tm.getIndex(LiteralIndex.class);
		if (!index.isOpen())
			index.open();
		else if (!index.isAutoUpdated())
			index.reindex();
	}
	
	public void write(){	
		if (subjectIsNew){
			associateOccurences(index.getOccurrences(tm.createLocator(subjectValue)));
		}
		if (this.predicate != null) 
			createAsCharacteristic();
		else
			createAsTypeInstance();
	}
	
	
	private void createAsTypeInstance() {
		subject.addType(createTopic(tm.createLocator(object.stringValue())));		
	}

	private void associateOccurences(Collection<Occurrence> occurences) {
		Iterator<Occurrence> it = occurences.iterator();
		Occurrence o;
		while (it.hasNext()) {
			o =  it.next();
			this.subjectRoleType =  createTopic(tm.createLocator("http://www.tmapi.org/2.0/api#SubjectRoleType"));
			Association a = tm.createAssociation(o.getType());
			a.createRole(subjectRoleType, o.getParent());
			a.createRole(o.getType(), createTopic(tm.createLocator(o.getValue())));
			o.remove();
		}
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
			if (t != null)
				return t;
		} catch (ClassCastException e) {
			// Found Construct is not a Topic
		}
		this.subjectIsNew  = true;
		return tm.createTopicBySubjectIdentifier(l);
	}

}
