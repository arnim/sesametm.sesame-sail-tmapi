/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
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
	private static ValueFactory vf;

	@Before
	public void setUp() throws Exception {
		_tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
		_sail = new TmapiStore(_tms);
		_tmapiRepository = new SailRepository(_sail);
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
		accessor = new FileAccessor();
		vf = _con.getValueFactory();
	}
	
	
//	@Test
//	public void testSIMPLE() throws Exception {
//		is = accessor.convertStringToInputStream("test.n3");
//		
//		_con.add(is, baseIRI, RDFFormat.N3, valueFactory.createURI(baseURI));
//
//		repositoryResult = _con.getStatements(null, null, null, true);
//		
//
//		
//
//		
//		_con.export( new N3Writer(System.out), valueFactory.createURI(baseURI));
//
//	}
//	
	
	
	
	@Test
	public void test2Occurences() throws Exception {
		is = accessor.convertStringToInputStream("2Occurences.n3");
		_con.add(is, baseIRI, RDFFormat.N3, vf.createURI(baseURI));
		assertEquals(5, _con.getStatements(null, null, null, true).asList().size());
		assertEquals(5, _con.getStatements(null, null, null, true, vf.createURI(baseURI)).asList().size());
		assertEquals(1, _con.getContextIDs().asList().size());
	}
	
	
	@Test
	public void test2Association() throws Exception {
		is = accessor.convertStringToInputStream("2Association.n3");
		_con.add(is, baseIRI, RDFFormat.N3, vf.createURI(baseURI));
		TopicMap tm = _tms.getTopicMap(baseURI);
		assertEquals(1, tm.getAssociations().size());
		assertEquals(7, tm.getTopics().size());
		assertEquals(3, _con.getStatements(null, null, null, true).asList().size());
		assertEquals(3, _con.getStatements(null, null, null, true, vf.createURI(baseURI)).asList().size());	
		assertEquals(1, _con.getContextIDs().asList().size());
	
//		_con.export( new N3Writer(System.out), valueFactory.createURI(baseURI));
	}
	
	@Test
	public void testTypeInstance() throws Exception {
		is = accessor.convertStringToInputStream("typeInstance.n3");
		_con.add(is, baseIRI, RDFFormat.N3, vf.createURI(baseURI));
		assertEquals(1, _con.getStatements(null, null, null, true).asList().size());
		assertEquals(vf.createURI("http://www.ex.org/xyz#ObjectNotKnown_2"), _con.getStatements(null, null, null, true).next().getSubject());
		assertEquals(RDF.TYPE, _con.getStatements(null, null, null, true).next().getPredicate());
		assertEquals(vf.createURI("http://www.ex.org/xyz#ObjectNotKnown_2_type"), _con.getStatements(null, null, null, true).next().getObject());

	}
	
	@Test
	public void testSameAs() throws Exception {
		assertFalse(_tms.getLocators().iterator().hasNext());
		_con.add(vf.createURI("http://www.ex.org/s"), vf.createURI("http://www.ex.org/p"), vf.createURI("http://www.ex.org/o"), vf.createURI(baseIRI));
		assertTrue(_tms.getLocators().iterator().hasNext());
		assertEquals(_tms.getLocators().iterator().next().toExternalForm(), baseIRI);
		TopicMap tm = _tms.getTopicMap(baseIRI);
		assertEquals(2, tm.getTopics().size());
		Iterator<Topic> ti = tm.getTopics().iterator();
		while (ti.hasNext()) {
			Topic t = ti.next();
			assertEquals(1, t.getSubjectIdentifiers().size());
		}
		assertEquals(1, tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/s")).getSubjectIdentifiers().size());
		_con.add(vf.createURI("http://www.ex.org/s"), OWL.SAMEAS, vf.createURI("http://www.ex.org/s1"), vf.createURI(baseIRI));
		assertEquals(2, tm.getTopics().size());
		assertEquals(2, tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/s")).getSubjectIdentifiers().size());
		assertTrue(tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/s")).getSubjectIdentifiers().contains(tm.createLocator("http://www.ex.org/s")));
		assertTrue(tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/s")).getSubjectIdentifiers().contains(tm.createLocator("http://www.ex.org/s1")));

		_con.add(vf.createURI("http://www.ex.org/s2"), OWL.SAMEAS, vf.createURI("http://www.ex.org/s1"), vf.createURI(baseIRI));
		assertEquals(2, tm.getTopics().size());
		assertEquals(3, tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/s")).getSubjectIdentifiers().size());
		assertTrue(tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/s")).getSubjectIdentifiers().contains(tm.createLocator("http://www.ex.org/s")));
		assertTrue(tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/s")).getSubjectIdentifiers().contains(tm.createLocator("http://www.ex.org/s1")));
		assertTrue(tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/s")).getSubjectIdentifiers().contains(tm.createLocator("http://www.ex.org/s2")));

	}
	
	
	@Test
	public void testOccurenceName() throws Exception {
		_con.add(vf.createURI("http://www.ex.org/s"), vf.createURI("http://www.ex.org/p"), vf.createLiteral("hoho"), vf.createURI(baseIRI));
		TopicMap tm = _tms.getTopicMap(baseIRI);
		assertEquals(2, tm.getTopics().size());
		Set<Occurrence> occs = tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/s")).getOccurrences();
		assertEquals(1, occs.size());
		assertEquals(XMLSchema.STRING.stringValue(), occs.iterator().next().getDatatype().toExternalForm());
		assertEquals("hoho", occs.iterator().next().getValue());

		_con.add(vf.createURI("http://www.ex.org/so"), vf.createURI("http://www.ex.org/name"), vf.createLiteral("A Name"), vf.createURI(baseIRI));
		assertEquals(4, tm.getTopics().size());
		Set<Name> names = tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/so")).getNames();
		assertEquals(0, tm.getTopicBySubjectIdentifier(tm.createLocator("http://www.ex.org/so")).getOccurrences().size());

		assertEquals(1, names.size());
		assertEquals("A Name", names.iterator().next().getValue());
	}

}
