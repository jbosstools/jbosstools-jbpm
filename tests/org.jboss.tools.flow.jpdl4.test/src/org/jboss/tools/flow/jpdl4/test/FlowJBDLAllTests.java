package org.jboss.tools.flow.jpdl4.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class FlowJBDLAllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(FlowJPDLPluginLoadTest.class);
		return suite;
	}
}
