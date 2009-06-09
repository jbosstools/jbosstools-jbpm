package org.jboss.tools.flow.jpdl4.multipage;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.tools.flow.jpdl4.multipage.validator.Jpdl4ValidatorTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(Jpdl4ValidatorTest.class);
		return suite;
	}

}
