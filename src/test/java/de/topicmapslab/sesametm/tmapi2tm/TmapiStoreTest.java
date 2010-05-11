/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.memory.MemoryStore;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;

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
		_sail = new TmapiStore();
		_tmapiRepository = new SailRepository(_sail );
		_tmapiRepository.initialize();
		_con = _tmapiRepository.getConnection();
		_tm = _sail.getTmSystem().createTopicMap("ha:lo");
		Topic t1 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("Sub:ject"));
		Topic t2 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("predi:cate"));
		Topic t3 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("ob:ject"));
		Topic rt1 =_tm.createTopicBySubjectIdentifier(_tm.createLocator("Sub:jectROletype"));
		Topic rt2 =_tm.createTopicBySubjectIdentifier(_tm.createLocator("Object:jectROletype"));

		Association asso = _tm.createAssociation(t2,new HashSet<Topic>());
		asso.createRole(rt1, t1);
		asso.createRole(rt2, t3);


		
	}	

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}



	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
//	@Test
//	public final void testGetContextIDs() throws Exception {
//		assertEquals(1,_con.getContextIDs().asList().size());
//		String queryString = "CONSTRUCT   { ?s ?p ?o }  WHERE       { ?s ?p ?o}";
//		GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
//		GraphQueryResult result = query.evaluate();
//	}
//	
//	@Test
//	public final void testTest() {
//		try {
//			System.out.println("huhu");
//			SailRepository sr = new SailRepository(new MemoryStore());
//			sr.initialize();
//			SailRepositoryConnection mc =  sr.getConnection();
//			String queryString = "CONSTRUCT   { ?s ?p ?o }  WHERE       { ?s ?p ?o}";
//			GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
//			GraphQueryResult result = query.evaluate();
//			System.out.println("huhu 2");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		
//	}
	
	
	@Test
	public final void testSsparql() throws Exception {
			String queryString = "CONSTRUCT   { ?s ?p ?o }  WHERE       { ?s ?p ?o}";
			GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
			GraphQueryResult result = query.evaluate();
			System.out.println(1);
			System.out.println(result.hasNext());
			System.out.println(3);
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#shutDownInternal()}.
	 */
	@Test
	public final void testShutDownInternal() {
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#TmapiStore()}.
	 */
	@Test
	public final void testTmapiStore() {
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#TmapiStore(org.tmapi.core.TopicMapSystem)}.
	 */
	@Test
	public final void testTmapiStoreTopicMapSystem() {
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#getConnectionInternal()}.
	 */
	@Test
	public final void testGetConnectionInternal() {
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#getValueFactory()}.
	 */
	@Test
	public final void testGetValueFactory() {
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#isWritable()}.
	 */
	@Test
	public final void testIsWritable() {
	}

	/**
	 * Test method for {@link org.openrdf.sail.helpers.SailBase#getConnection()}.
	 */
	@Test
	public final void testGetConnection() {
	}

}
