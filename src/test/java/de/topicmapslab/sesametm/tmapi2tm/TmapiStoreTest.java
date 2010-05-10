/**
 * 
 */
package de.topicmapslab.sesametm.tmapi2tm;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Arnim Bleier
 *
 */
public class TmapiStoreTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TmapiStore repository = new TmapiStore();
		System.out.println(repository.getTmSystem().createTopicMap("ha:lo"));
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

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#shutDownInternal()}.
	 */
	@Test
	public final void testShutDownInternal() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#TmapiStore()}.
	 */
	@Test
	public final void testTmapiStore() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#TmapiStore(org.tmapi.core.TopicMapSystem)}.
	 */
	@Test
	public final void testTmapiStoreTopicMapSystem() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#getConnectionInternal()}.
	 */
	@Test
	public final void testGetConnectionInternal() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#getValueFactory()}.
	 */
	@Test
	public final void testGetValueFactory() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link de.topicmapslab.sesametm.tmapi2tm.TmapiStore#isWritable()}.
	 */
	@Test
	public final void testIsWritable() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.openrdf.sail.helpers.SailBase#getConnection()}.
	 */
	@Test
	public final void testGetConnection() {
		fail("Not yet implemented"); // TODO
	}

}
