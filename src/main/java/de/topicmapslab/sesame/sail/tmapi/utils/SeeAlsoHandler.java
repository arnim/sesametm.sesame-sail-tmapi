/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import java.util.Iterator;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.RDFS;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;


/**
 * @author Arnim Bleier
 * 
 */
public class SeeAlsoHandler {
	
	private Locator subj;
	private Locator pred;
	private Locator obj;
	private TopicMap tm;
	private Set<Statement> statements;
	private TmapiStatementFactory statementFactory;
	private TmapiStatementIterator<?> other;

	public SeeAlsoHandler(Locator subj, Locator pred, Locator obj, TopicMap tm, TmapiStatementIterator<?> other ){
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
		
		


		
		if (sTopic == null && subj != null || pred != null && (pTopic == null && !RDFS.SEEALSO.toString().equals(pred.toExternalForm()) )
				 || oTopic == null && obj != null) {

				// Q has no match in this tm
			} else {
				
				if (sTopic == null && oTopic == null)
					createSameAsListxPx();
				else if (sTopic != null && oTopic == null )
					createSameAsListSPX(sTopic);
				else if (sTopic == null && oTopic != null)
					createSameAsListXPO(oTopic);
				else if (sTopic != null && oTopic != null)
					createTypeSameAsSPO(sTopic, oTopic);
				else
					System.err
							.println("You should never read this! TmapiStatementIterator:10 ");	
			}

	}
	
	
	
	

	
	
	private void createSameAsListxPx(){
		Iterator<Topic> topicsIterator = tm.getTopics().iterator();
		while (topicsIterator.hasNext()) {		
			createSameAsListSPX(topicsIterator.next());
		}
	}
	
	private void createSameAsListSPX(Topic sTopic){
		statements.add(statementFactory.create(sTopic, RDFS.SEEALSO, tm.getLocator().toExternalForm() + "/t/" + statementFactory.getBestLocator(sTopic).toExternalForm()));
	}
	
	private void createSameAsListXPO(Topic oTopic){
		createSameAsListSPX(oTopic);
	}
	
	private void createTypeSameAsSPO(Topic sTopic, Topic oTopic){
//		System.out.println(statementFactory.getBestLocator(sTopic) + "mit " + statementFactory.getBestLocator(oTopic));
	}
	
	
	private Set<Locator> getOtherLocaters(Topic t){
		Set<Locator> resutl = statementFactory.getAllLocators(t);
		resutl.remove(statementFactory.getBestLocator(t));
		return resutl;
	}
	

}
