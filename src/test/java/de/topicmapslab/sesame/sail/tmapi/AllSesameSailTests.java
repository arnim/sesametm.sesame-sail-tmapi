/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail.tmapi;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllSesameSailTests extends TestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite("All Tests for Sesame Sail TMAPI");
    
    suite.addTestSuite(TmapiStoreTest.class);
    
    suite.addTestSuite(TmdmStoreTest.class);
    
    suite.addTestSuite(MaianaTest.class);

    suite.addTestSuite(CRUDTest.class);

    return suite;
  }
  
  
  public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
  }

}
