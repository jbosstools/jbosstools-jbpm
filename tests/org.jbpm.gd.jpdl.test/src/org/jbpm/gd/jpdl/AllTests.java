package org.jbpm.gd.jpdl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(JpdlPerspectiveFactoryTest.class);
		return suite;
	}

}
