package org.jbpm.gd.common.notation;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.geometry.Point;
import org.jbpm.gd.common.model.NamedElement;
import org.jbpm.gd.common.model.SemanticElement;

public class Label extends AbstractNotationElement {
	
	String text = "";
    private Point offset = new Point(5, -10);
    
	public void setText(String text) {
		if (text == null) {
			this.text = "";
		} else {
			this.text = text;
		}
	}
	
	public String getText() {
		return text;
	}
	
	public Point getOffset() {
		return offset;
	}
	
	public void setOffset(Point newOffset) {
		Point oldOffset = offset;
		offset = newOffset;
		firePropertyChange("offset", oldOffset, newOffset);
	}

	public void setSemanticElement(SemanticElement semanticElement) {
		super.setSemanticElement(semanticElement);
		setText(((NamedElement)semanticElement).getName());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("name")) {
			setText((String)evt.getNewValue());
			firePropertyChange("text", evt.getOldValue(), evt.getNewValue());
		}
	}
	
	
}
