package org.jbpm.gd.pf.model;


public class Transition extends AbstractNamedElement {
		
	private String to;
	private NodeElement source;

	public void setTo(String newTo) {
		String oldTo = to;
		to = newTo;
		firePropertyChange("to", oldTo, newTo);
	}
	
	public String getTo() {
		return to;
	}
	
	public void setSource(NodeElement newSource) {
		this.source = newSource;
	}
	
	public NodeElement getSource() {
		return source;
	}
	
	public boolean isNameMandatory() {
		return false;
	}

}
