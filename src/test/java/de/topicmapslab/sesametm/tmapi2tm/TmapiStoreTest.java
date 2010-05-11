/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
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
	
	
	
	
	@Test
	public final void testSsparql() throws Exception {
			
			
			System.out.println(_con.toString());
			URIImpl s = new URIImpl("http://www.openrdf.org/ddd#s");
			URIImpl p = new URIImpl("http://www.openrdf.org/ddd#p");
			URIImpl o = new URIImpl("http://www.openrdf.org/ddd#o");
			URIImpl c = new URIImpl("http://www.openrdf.org/ddd#c");

			
//			_con.add(new StatementImpl(s, p, o), c);
			try {
				System.out.println(_con.getContextIDs().asList());
			} catch (Exception e) {
				e.printStackTrace();
			}

//			String queryString = "SELECT x, y FROM {x} p {y}";
//			TupleQuery tupleQuery = _con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
//		    TupleQueryResult result = tupleQuery.evaluate();
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
