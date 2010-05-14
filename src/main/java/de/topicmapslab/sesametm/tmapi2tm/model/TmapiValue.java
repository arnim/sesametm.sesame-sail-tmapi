/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.tmapi2tm.model;

import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;

/**
 * @author Arnim Bleier
 * 
 */
public class TmapiValue {

	private String value;
	private Locator type;

	/**
	 * 
	 */
	public TmapiValue(Locator value, Locator type) {
		this.setValue(value.toExternalForm());
		this.setType(type);
	}

	public TmapiValue(String value, Locator type) {
		this.setValue(value);
		this.setType(type);
	}

	public TmapiValue(Name name) {
		this.setValue(name.getValue());
		this.setType(name.getParent().getParent().createLocator(
				"http://www.w3.org/2001/XMLSchema#string"));
	}
	
	
	public TmapiValue(Occurrence occurrence) {
		this.setValue(occurrence.getValue());
		this.setType(occurrence.getDatatype());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		TmapiValue other = (TmapiValue) obj;
		if (!type.equals(other.type))
			return false;
		if (!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	private void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	private void setType(Locator type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Locator getType() {
		return type;
	}

	@Override
	public String toString() {
		return "TmapiValue [type=" + type.toExternalForm() + ", value=" + value
				+ "]";
	}

}
