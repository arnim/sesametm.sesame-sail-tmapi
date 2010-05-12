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
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.n3.N3Writer;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
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
		_tm = _sail.getTmSystem().createTopicMap("http://www.base.com/iri/");
		Topic t1 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("Sub:ject"));
		Topic t2 = _tm.createTopicBySubjectIdentifier(_tm.createLocator("http://www.google.com/predicate"));
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
	public final void testGetContextIDs() throws Exception {
		assertEquals(1,_con.getContextIDs().asList().size());
		assertEquals("http://www.base.com/iri/",_con.getContextIDs().next().stringValue());
	}
	
	
	@Test
	public final void testSsparql() throws Exception {
			String queryString = "CONSTRUCT   { <http://www.google.com/predicate2> <http://www.google.com/predicate> ?o }  WHERE   { <http://www.google.com/rightSub> <http://www.google.com/predicate> ?o}";
			GraphQuery query = _con.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
			GraphQueryResult result = query.evaluate();
			assertTrue(result.hasNext());
			System.out.println("Return of graph Q" + result.next());
	}
	
	
	
	
	@Test
	public final void testExport() throws Exception {
			try {
				RDFHandler rdfWriter = new N3Writer(System.out);
				_con.exportStatements(null, _con.getValueFactory().createURI("http://www.google.com/predicate"), null, true, rdfWriter);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	
	@Test
	public final void testGetSPO() throws Exception {
			try {
				RepositoryResult<Statement> r = _con.getStatements(null, _con.getValueFactory().createURI("http://www.google.com/predicate"), null, true);
				System.out.println(r.asList());
			} catch (Exception e) {
				e.printStackTrace();
			}
	}



}
