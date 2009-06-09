package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractSemanticElement;

public class Text extends AbstractSemanticElement {
	
	private String text;
	
	public void setText(String newText) {
		String oldText = text;
		text = newText;
		firePropertyChange("text", oldText, newText);
	}
	
	public String getText() {
		return text;
	}

}
