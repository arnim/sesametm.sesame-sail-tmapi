/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi.utils;

import java.util.Iterator;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.sail.SailException;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

/**
 * @author Arnim Bleier
 *
 */
public class MapSpider implements Runnable {

	private Set<Statement> statements;
	private TopicMap tm;
	private Topic sTopic;
	private Topic pTopic;
	private Topic oTopic;
	private TmapiStatementFactory statementFactory;

	/**
	 * 
	 */
	public MapSpider(Set<Statement> statements, TmapiStatementFactory statementFactory, TopicMap tm, Topic sTopic, Topic pTopic, Topic oTopic) {
		this.statements = statements;
		this.statementFactory = statementFactory;
		this.tm = tm;
		this.sTopic = sTopic;
		this.pTopic = pTopic;
		this.oTopic = oTopic;
	}


	public void run() {
		try {
			select();
		} catch (SailException e) {
			e.printStackTrace();
		}

	}

	private void select() throws SailException {
		if (sTopic == null && pTopic == null && oTopic == null)
			createListXXX(tm);
		else if (sTopic != null && pTopic == null && oTopic == null)
			createListSXX(sTopic, tm);
		else if (sTopic != null && pTopic != null && oTopic == null)
			createListSPX(sTopic, pTopic, tm);
		else if (sTopic == null && pTopic != null && oTopic == null)
			createListXPX(pTopic, tm);
		else if (sTopic != null && pTopic != null && oTopic != null)
			createListSPO(sTopic, pTopic, oTopic, tm);
		else if (sTopic == null && pTopic == null && oTopic != null)
			createListXXO(oTopic, tm);
		else if (sTopic == null && pTopic != null && oTopic != null)
			createListXPO(pTopic, oTopic, tm);
		else
			System.err
					.println("You should never read this! TmapiStatementIterator:104 ");

		
	}
	
	
	private void createListXXX(TopicMap tm) throws SailException {
		Iterator<Topic> tIterator = tm.getTopics().iterator();
		while (tIterator.hasNext()) {
			createListSXX(tIterator.next(), tm);
		}
	}

	private void createListSXX(Topic subj, TopicMap tm) throws SailException {
		addCharacteristics(subj);
		Role thisRole, otherRole;
		Iterator<Role> thisRolesIterator = subj.getRolesPlayed().iterator(), otherRolesIterator;
		while (thisRolesIterator.hasNext()) {
			thisRole = thisRolesIterator.next();
			otherRolesIterator = thisRole.getParent().getRoles().iterator();
			while (otherRolesIterator.hasNext()) {
				otherRole = otherRolesIterator.next();
				if (!thisRole.getType().equals(otherRole.getType())) {
					statements.add(statementFactory.create(subj, otherRole
							.getType(), otherRole.getPlayer()));
				}
			}
		}

	}

	private void createListSPX(Topic subj, Topic pred, TopicMap tm)
			throws SailException {
		addCharacteristics(subj, pred);
		Topic thisRoleType;
		Role thisRole, otherRole;
		Iterator<Role> thisRolesIterator = subj.getRolesPlayed().iterator(), otherRolesIterator;
		while (thisRolesIterator.hasNext()) {
			thisRole = thisRolesIterator.next();
			thisRoleType = thisRole.getType();
			otherRolesIterator = thisRole.getParent().getRoles().iterator();
			while (otherRolesIterator.hasNext()) {
				otherRole = otherRolesIterator.next();
				if (!thisRoleType.equals(otherRole.getType())
						&& otherRole.getType().equals(pred)) {
					statements.add(statementFactory.create(subj, otherRole
							.getType(), otherRole.getPlayer()));
				}
			}
		}
	}

	private void createListXPX(Topic pred, TopicMap tm) throws SailException {
		Iterator<Topic> tIterator = tm.getTopics().iterator();
		while (tIterator.hasNext()) {
			createListSPX(tIterator.next(), pred, tm);
		}
	}

	private void createListSPO(Topic subj, Topic pred, Topic obj, TopicMap tm)
			throws SailException {

		Role subjectRole, objectRole;
		Iterator<Role> subjectRolesIterator, objectRolesIterator = obj.getRolesPlayed().iterator();
		while (objectRolesIterator.hasNext()) {
			objectRole = objectRolesIterator.next();
			if (objectRole.getType().equals(pred)){
				subjectRolesIterator = objectRole.getParent().getRoles().iterator();
				while (subjectRolesIterator.hasNext()) {
					subjectRole = subjectRolesIterator.next();
					if (subjectRole.getPlayer().equals(subj))
						
						
						statements.add(statementFactory.create(subj, pred, obj));
				}
			}
			
			
		}
		
		
		
		
	}

	private void createListXXO(Topic obj, TopicMap tm) throws SailException {
		Role objectRole, subjectRole;
		Topic objectRoleType;
		Iterator<Role> subjectRoleIterator;
		Iterator<Role> objectRolesIterator = obj.getRolesPlayed().iterator();
		while (objectRolesIterator.hasNext()) {
			objectRole = objectRolesIterator.next();
			objectRoleType = objectRole.getType();

			subjectRoleIterator = objectRole.getParent().getRoles().iterator();
			while (subjectRoleIterator.hasNext()) {
				subjectRole = subjectRoleIterator.next();
				if (!subjectRole.getType().equals(objectRoleType)) {
					statements.add(statementFactory.create(subjectRole
							.getPlayer(), objectRoleType, obj));
				}
			}

		}
	}

	private void createListXPO(Topic pred, Topic obj, TopicMap tm)
			throws SailException {
		Iterator<Role> objectRolesIterator = obj.getRolesPlayed(pred)
				.iterator(), sbjectRolesIterator;
		Role objectRole, subjectRole;
		while (objectRolesIterator.hasNext()) {
			objectRole = objectRolesIterator.next();
			sbjectRolesIterator = objectRole.getParent().getRoles().iterator();
			while (sbjectRolesIterator.hasNext()) {
				subjectRole = sbjectRolesIterator.next();
				if (!subjectRole.getType().equals(pred))
					statements.add(statementFactory.create(subjectRole
							.getPlayer(), pred, obj));
			}

		}
	}

	private void addCharacteristics(Topic t) {
		Iterator<Name> names = t.getNames().iterator();
		while (names.hasNext()) {
			statements.add(statementFactory.create(names.next()));
		}
		Iterator<Occurrence> occurrences = t.getOccurrences().iterator();
		while (occurrences.hasNext()) {
			statements.add(statementFactory.create(occurrences.next()));
		}
	}

	private void addCharacteristics(Topic t, Topic type) {
		Iterator<Name> names = t.getNames(type).iterator();
		while (names.hasNext()) {
			statements.add(statementFactory.create(names.next()));
		}
		Iterator<Occurrence> occurrences = t.getOccurrences(type).iterator();
		while (occurrences.hasNext()) {
			statements.add(statementFactory.create(occurrences.next()));
		}
	}

	
	

}
