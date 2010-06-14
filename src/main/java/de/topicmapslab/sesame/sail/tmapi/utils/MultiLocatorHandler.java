/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import java.util.Iterator;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.TypeInstanceIndex;


/**
 * @author Arnim Bleier
 * 
 */
public class MultiLocatorHandler {
	
	private Locator subj;
	private Locator pred;
	private Locator obj;
	private TopicMap tm;
	private Set<Statement> statements;
	private TmapiStatementFactory statementFactory;
	private TmapiStatementIterator<?> other;

	public MultiLocatorHandler(Locator subj, Locator pred, Locator obj, TopicMap tm, TmapiStatementIterator<?> other ){
		this.subj = subj;
		this.pred = pred;
		this.obj = obj;
		this.tm = tm;
		this.statements = other.getStatements();
		this.statementFactory = other.getStatementFactory();
		this.other = other;
		
		
		
		

	}
	


	public void evaluate(){
		
		
		
		Topic sTopic = null, pTopic = null, oTopic = null;	
		sTopic = other.getTopic(subj, tm);
		pTopic = other.getTopic(pred, tm);
		oTopic = other.getTopic(obj, tm);
		
		if (sTopic == null && subj != null || pred != null && (pTopic == null && !OWL.SAMEAS.toString().equals(pred.toExternalForm()) )
				 || oTopic == null && obj != null) {

				// Q has no match in this tm
			} else {
//				if (pTopic == null && pred != null && OWL.SAMEAS.toString().equals(pred.toExternalForm()))
					createSameAsList(sTopic, oTopic, tm);
//				else if (sTopic == null && pTopic == null && oTopic == null)
//					createListXXX(tm);
//				else if (sTopic != null && pTopic == null && oTopic == null)
//					createListSXX(sTopic, tm);
//				else if (sTopic != null && pTopic != null && oTopic == null)
//					createListSPX(sTopic, pTopic, tm);
//				else if (sTopic == null && pTopic != null && oTopic == null)
//					createListXPX(pTopic, tm);
//				else if (sTopic != null && pTopic != null && oTopic != null)
//					createListSPO(sTopic, pTopic, oTopic, tm);
//				else if (sTopic == null && pTopic == null && oTopic != null)
//					createListXXO(oTopic, tm);
//				else if (sTopic == null && pTopic != null && oTopic != null)
//					createListXPO(pTopic, oTopic, tm);
//				else
//					System.err
//							.println("You should never read this! TmapiStatementIterator:104 ");
			}
		
		
	}
	
	
	
	
	private void createSameAsList(Topic sTopic, Topic oTopic, TopicMap tm){		
		if (sTopic == null && oTopic == null)
			createSameAsListxPx(tm);
//		else if (sTopic != null && oTopic == null )
//			createSameAsListSPX(sTopic, tm);
//		else if (sTopic == null && oTopic != null)
//			createSameAsListXPO(oTopic, tm);
//		else if (sTopic != null && oTopic != null)
//			createTypeSameAsSPO(sTopic, oTopic, tm);
		else
			System.err
					.println("You should never read this! TmapiStatementIterator:104 ");	
	}
	
	
	private void createSameAsListxPx(TopicMap tm){
	
		Iterator<Topic> topicsIterator = tm.getTopics().iterator();
		while (topicsIterator.hasNext()) {
			

					
			createSameAsListSPX(topicsIterator.next(), tm);
		}
	}
	
	private void createSameAsListSPX(Topic sTopic, TopicMap tm){
		Iterator<Locator> otherLotatorsiterator = getOtherLocaters(sTopic).iterator(); 
		
		while (otherLotatorsiterator.hasNext()) {

			statements.add(statementFactory.create(sTopic, OWL.SAMEAS, otherLotatorsiterator.next()));
		}
	}
	
	private void createSameAsListXPO(Topic oTopic, TopicMap tm){
		Iterator<Topic> subjectIterator = tm.getIndex(TypeInstanceIndex.class).getTopics(oTopic).iterator();
		while (subjectIterator.hasNext()) {
			statements.add(statementFactory.create(subjectIterator.next(), RDF.TYPE, oTopic));	
		}
	}
	
	private void createTypeSameAsSPO(Topic sTopic, Topic oTopic, TopicMap tm){
		Topic objectTopic;
		Iterator<Topic> typesIterator = sTopic.getTypes().iterator();
		while (typesIterator.hasNext()) {
			objectTopic =  typesIterator.next();
			if (objectTopic.equals(oTopic))
				statements.add(statementFactory.create(sTopic, RDF.TYPE, objectTopic));
		}
	}
	
	
	private Set<Locator> getOtherLocaters(Topic t){
		Set<Locator> resutl = statementFactory.getAllLocators(t);
		resutl.remove(statementFactory.getBestLocator(t));
		return resutl;
	}
	

}
