/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.n3.N3Writer;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;


/**
 * @author Arnim Bleier
 *
 */
public class TmapiStoreTest {


	private static TmapiStore _sail;
	private static Repository _tmapiRepository;
	private static RepositoryConnection _con;
	private static TopicMap _tm;
	
	final static String baseIRI = "http://www.topicmapslab.de/test/base/";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TopicMapSystem _tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		_sail = new TmapiStore(_tms);
		_tmapiRepository = new SailRepository(_sail );
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();

		_tm = _sail.getTopicMapSystem().createTopicMap(baseIRI);

		Topic alf = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "alf"));
		Topic bert = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "bert"));
		Topic xyz = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "xyz"));
		Topic worksFor = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "worksFor"));
		Topic employer = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "employer"));
		Topic employee = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "employee"));
		Topic hourlyWage = _tm.createTopicBySubjectIdentifier(_tm.createLocator(baseIRI + "hourlyWage"));


		alf.createOccurrence(hourlyWage, "14,50",
				_tm.createLocator("http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#integer"));
		alf.createOccurrence(hourlyWage, "25,40",
				_tm.createLocator("http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#integer"));

		Association awf = _tm.createAssociation(worksFor);
		Association bwf = _tm.createAssociation(worksFor);
		
		awf.createRole(employee, alf);
		awf.createRole(employer, xyz);
		
		bwf.createRole(employee, bert);
		bwf.createRole(employer, xyz);


		_sail.index();
	}	


	@Test
	public final void testGetContextIDs() throws Exception {
		assertEquals(1,_con.getContextIDs().asList().size());
		assertEquals(baseIRI,_con.getContextIDs().next().stringValue());
	}

	@Test
	public final void testTest() throws Exception {

		RDFHandler rdfWriter = new N3Writer(System.out);
		_con.exportStatements(null, null, null, true, rdfWriter);


	}




}
