package org.jboss.tools.jbpm;

import org.jboss.tools.jbpm.java.JavaUtilTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(JavaUtilTest.class);
		return suite;
	}

}
