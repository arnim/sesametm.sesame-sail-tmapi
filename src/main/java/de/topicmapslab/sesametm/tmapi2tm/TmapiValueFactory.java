/*
 * Copyright Aduna (http://www.aduna-software.com/) (c) 1997-2010.
 *
 * Licensed under the Aduna BSD-style license.
 */
package de.topicmapslab.sesametm.tmapi2tm;

import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryBase;
import org.openrdf.sail.memory.model.MemValueFactory;

/**
 * A factory for TmapiValue
 * 
 * @author Arnim Bleier
 */
public class TmapiValueFactory extends ValueFactoryBase {
	
	private ValueFactory vf = new MemValueFactory();

	public BNode createBNode(String arg0) {
		return vf.createBNode(arg0);
	}

	public Literal createLiteral(String arg0) {
		return vf.createLiteral(arg0);
	}

	public Literal createLiteral(String arg0, String arg1) {
		return vf.createLiteral(arg0, arg1);
	}

	public Literal createLiteral(String arg0, URI arg1) {
		return vf.createLiteral(arg0, arg1);
	}

	public Statement createStatement(Resource arg0, URI arg1, Value arg2) {
		return vf.createStatement(arg0, arg1, arg2);
	}

	public Statement createStatement(Resource arg0, URI arg1, Value arg2,
			Resource arg3) {
		return vf.createStatement(arg0, arg1, arg2, arg3);
	}

	public URI createURI(String arg0) {
		return vf.createURI(arg0);
	}

	public URI createURI(String arg0, String arg1) {
		return vf.createURI(arg0, arg1);
	}

}
