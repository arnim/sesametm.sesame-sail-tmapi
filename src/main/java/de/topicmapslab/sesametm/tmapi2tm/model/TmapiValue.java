/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesametm.tmapi2tm.model;

import org.tmapi.core.Locator;

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

	/**
	 * @param value the value to set
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
	 * @param type the type to set
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
		return "TmapiValue [type=" + type.toExternalForm() + ", value=" + value + "]";
	}

}
