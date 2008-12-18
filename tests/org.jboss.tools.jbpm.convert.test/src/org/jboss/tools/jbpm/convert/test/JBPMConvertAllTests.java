package org.jboss.tools.jbpm.convert.test;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.jmx.core.tests.DefaultMBeanServerConnectionFactoryTest;
import org.jboss.tools.jmx.core.tests.DefaultProviderTest;
import org.jboss.tools.jmx.core.tests.NodeBuilderTestCase;


public class JBPMConvertAllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(BpmnConvertTest.class);
		return suite;
	}
}
