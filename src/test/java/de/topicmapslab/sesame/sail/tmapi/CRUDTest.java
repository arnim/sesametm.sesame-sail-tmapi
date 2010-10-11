/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import java.io.InputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;

/**
 * @author Arnim Bleier
 * 
 */
public class CRUDTest extends TestCase {

	private static TmapiStore _sail;
	private static Repository _tmapiRepository;
	private static RepositoryConnection _con;
	private static TopicMapSystem _tms;
	private static FileAccessor accessor;
	private static InputStream is;
	private static final String baseURI = "http://www.openrdf.org/test";
	final static String baseIRI = "http://www.topicmapslab.de/test/base/";
	private static ValueFactory valueFactory;
	private RepositoryResult<Statement> repositoryResult;

	@Before
	public void setUp() throws Exception {
		_tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		_sail = new TmapiStore(_tms);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
		accessor = new FileAccessor();
		valueFactory = _con.getValueFactory();
	}
	
	
	@Test
	public void testSIMPLE() throws Exception {
		is = accessor.convertStringToInputStream("test.n3");
		
		_con.add(is, baseIRI, RDFFormat.N3, valueFactory.createURI(baseURI));

		repositoryResult = _con.getStatements(null, null, null, true);
		
		while (repositoryResult.hasNext()) {
			System.out.println(repositoryResult.next());
		}

	}

}