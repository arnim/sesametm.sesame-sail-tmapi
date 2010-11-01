/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 */

package de.topicmapslab.sesame.sail;

import de.topicmapslab.sesame.sail.tmapi.AllSesameSailTests;
import de.topicmapslab.sesame.simpleinterface.TMConnectorTest;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AllSesameTests extends TestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    
//    suite.addTestSuite(AllSesameSailTests.class);
    
    suite.addTestSuite(TMConnectorTest.class);
    
    
    return suite;
  }
  
  
  public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
  }


}
