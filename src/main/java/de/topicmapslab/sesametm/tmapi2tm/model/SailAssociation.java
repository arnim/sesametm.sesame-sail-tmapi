/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.tmapi2tm.model;

import java.util.Set;

import org.tmapi.core.Association;

/**
 * @author Arnim Bleier
 *
 */
public class SailAssociation {


	private Set<Association> associations;

	public SailAssociation(Set<Association> associations) {
		this.setAssociations(associations);
	}

	public SailAssociation(Association association) {
		this.setAssociations(associations);
	}

	private void setAssociations(Set<Association> associations) {
		this.associations = associations;
	}

	/**
	 * @return the associations
	 */
	public Set<Association> getAssociations() {
		return associations;
	}

}
