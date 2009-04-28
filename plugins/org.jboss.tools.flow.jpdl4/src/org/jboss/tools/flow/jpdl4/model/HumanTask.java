package org.jboss.tools.flow.jpdl4.model;

public class HumanTask extends Task implements Assignment {
	
	public HumanTask() {
		setMetaData("propertySource", new AssignmentPropertySource());		
	}
		
}
