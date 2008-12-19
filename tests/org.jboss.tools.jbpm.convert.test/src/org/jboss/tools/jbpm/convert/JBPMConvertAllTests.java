package org.jboss.tools.jbpm.convert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.jbpm.convert.test.BpmnConvertTest;


public class JBPMConvertAllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(BpmnConvertTest.class);
		return suite;
	}
}
