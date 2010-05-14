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

		_tm = _sail.getTopicMapSystem().createTopicMap("http://www.base.com/iri/");
		Topic t1 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/Subject"));
		Topic t2 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/assoType"));
		t2.addItemIdentifier(_tm.createLocator("http://www.google.com/predicate-ii"));
		Topic t3 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/Object"));
		Topic rt1 =_tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/SubjectROletype"));
		Topic rt2 =_tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/ObjectROletype"));
		t3.createName(rt2,"object name", rt2);
		t3.createOccurrence(rt2, "object occ", rt2);
		t3.createOccurrence(rt2, _tm.createLocator("http://www.google.com/someURI"), rt2);

		Association asso = _tm.createAssociation(t2,new HashSet<Topic>());
		asso.createRole(rt1, t1);
		asso.createRole(rt2, t3);
		_sail.index();
	}	


	@Test
	public final void testGetContextIDs() throws Exception {
		assertEquals(1,_con.getContextIDs().asList().size());
		assertEquals("http://www.base.com/iri/",_con.getContextIDs().next().stringValue());
	}

	@Test
	public final void testTest() throws Exception {

		RDFHandler rdfWriter = new N3Writer(System.out);
		_con.exportStatements(null, null, null, true, rdfWriter);


	}




}
