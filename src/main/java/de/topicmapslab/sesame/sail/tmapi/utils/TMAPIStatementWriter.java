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
import org.openrdf.model.vocabulary.OWL;
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
	private Value object;
	private LiteralIndex index;
	private String subjectValue;
	public final static String subjectRoleTypeString = "http://www.tmapi.org/2.0/api#SubjectRoleType";
	private Topic subjectRoleType;
	private String predicateValue;
	

	public TMAPIStatementWriter(Resource subject, URI predicate, Value object, TopicMap tm){
		this.tm = tm;
		this.subjectValue = subject.stringValue();
		this.predicateValue = predicate.stringValue();
		this.object = object;
		
		// handle the index
		index = tm.getIndex(LiteralIndex.class);
		if (!index.isOpen())
			index.open();
		else if (!index.isAutoUpdated())
			index.reindex();
	}
	
	public void write(){	
		if (getTopic(l(this.subjectValue)) == null){ 
			associateOccurences(index.getOccurrences(tm.createLocator(subjectValue)));
		}
		if (this.predicateValue.equals(RDF.TYPE.stringValue()))
			createAsTypeInstance();
		else if (this.predicateValue.equals(OWL.SAMEAS.stringValue())) 
			addLocator2Topic();
		else
			createAsCharacteristic();
	}
	
	private void addLocator2Topic(){
		Topic t = createTopic(l(this.subjectValue));
		try {

			t.addSubjectIdentifier(tm.createLocator(this.object.stringValue()));
		} catch (Exception e) {
			// Object is not a Locator
		}
	}
	
	
	private void createAsTypeInstance() {
		createTopic(l(subjectValue)).addType(createTopic(tm.createLocator(object.stringValue())));		
	}

	
	/**
	 * Turns all IRI occurrences into Associations
	 * 
	 * @param occurences
	 */
	private void associateOccurences(Collection<Occurrence> occurences) {
		Iterator<Occurrence> it = occurences.iterator();
		Occurrence o;
		while (it.hasNext()) {
			o =  it.next();
			this.subjectRoleType =  createTopic(tm.createLocator(subjectRoleTypeString));
			Association a = tm.createAssociation(o.getType());
			a.createRole(subjectRoleType, o.getParent());
			a.createRole(o.getType(), createTopic(tm.createLocator(o.getValue())));
			o.remove();
		}
	}

	
	private void createAsCharacteristic(){
		URI dt = null;
		try {
			dt = ((LiteralImpl) object).getDatatype();
		} catch (Exception e) {
			dt = XMLSchema.ANYURI;
		}
		if (dt == null)
			dt = XMLSchema.STRING;
		if (predicateValue.toLowerCase().contains("name") &&
				dt == XMLSchema.STRING)
			createAsName(l(dt.stringValue()));
		else
			createAsOccurrence(l(dt.stringValue()));
	}
	
	private void createAsOccurrence(Locator dt){
		Topic subject = createTopic(l(subjectValue));
		Topic predicate = createTopic(l(predicateValue));
		subject.createOccurrence(predicate, object.stringValue(), dt);
	}
	
	private void createAsName(Locator dt){
		Topic subject = createTopic(l(subjectValue));
		Topic predicate = createTopic(l(predicateValue));
		subject.createName(predicate, object.stringValue());
	}
	
	private Locator l(String s){
		return tm.createLocator(s);
	}
	
	
	public Topic getTopic(Locator l) {
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
		return t;
	}
	
	
	public Topic createTopic(Locator l) {
		Topic t = getTopic(l);
		if (t != null)
			return t;
		return tm.createTopicBySubjectIdentifier(l);
	}

}
