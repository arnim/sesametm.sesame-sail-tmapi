/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.tmapi2tm.model;


/**
 * @author Arnim Bleier
 *
 */
public class TmapiStatement  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2849087786790890830L;
	private SailTopic subj, predi, obj;


	/**
	 * 
	 */
	public TmapiStatement(SailTopic subj, SailTopic predi, SailTopic obj) {
		this.subj = subj;
		this.predi = predi;
		this.obj = obj;
	}


	public SailTopic getObject() {
		return subj;
	}


	public SailTopic getPredicate() {
		return predi;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.model.Statement#getSubject()
	 */
	public SailTopic getSubject() {
		return obj;
	}

}
