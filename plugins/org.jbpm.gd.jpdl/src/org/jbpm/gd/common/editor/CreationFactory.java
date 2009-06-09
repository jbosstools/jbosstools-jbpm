package org.jbpm.gd.common.editor;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.model.SemanticElementFactory;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.notation.NotationElementFactory;
import org.jbpm.gd.common.notation.NotationMapping;

public class CreationFactory implements org.eclipse.gef.requests.CreationFactory {	
	
	SemanticElement semanticElement;
	String elementId;
	SemanticElementFactory semanticElementFactory;
	NotationElementFactory notationElementFactory;
	
	/**
	 * Use this constructor when the semantic element does not exist and has to be created
	 */
	public CreationFactory(String elementId, SemanticElementFactory semanticElementFactory, NotationElementFactory notationElementFactory) {
		this.elementId = elementId;
		this.semanticElementFactory = semanticElementFactory;
		this.notationElementFactory = notationElementFactory;
	}
	
	
	/**
	 * Use this constructor when the semantic element exists
	 */
	public CreationFactory(SemanticElement semanticElement, NotationElementFactory notationElementFactory) {
		this(semanticElement.getElementId(), semanticElement.getFactory(), notationElementFactory);
		this.semanticElement = semanticElement;
	}

	public Object getNewObject() {
		String notationElementId = NotationMapping.getNotationElementId(elementId);
		NotationElement notationElement = notationElementFactory.create(notationElementId);
		if (notationElement instanceof Node) {
			Dimension dimension = NotationMapping.getInitialDimension(elementId);
			if (dimension != null) {
				Rectangle constraint = ((Node)notationElement).getConstraint();
				constraint.setSize(new Dimension(dimension));
			}
		}
		SemanticElement element = semanticElement != null ? semanticElement : semanticElementFactory.createById(elementId);
		notationElement.setSemanticElement(element);
		notationElement.register();
		return notationElement;
	}

	public Object getObjectType() {
		return elementId;
	}

}
