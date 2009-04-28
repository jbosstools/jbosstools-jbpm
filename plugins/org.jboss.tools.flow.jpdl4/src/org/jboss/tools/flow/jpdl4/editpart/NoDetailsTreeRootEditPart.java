package org.jboss.tools.flow.jpdl4.editpart;


public class NoDetailsTreeRootEditPart extends JpdlTreeEditPart {
	
	public NoDetailsTreeRootEditPart() {
		super(null);
	}
	
	protected String getText() {
		return "Details are not available.";
	}
	
}
