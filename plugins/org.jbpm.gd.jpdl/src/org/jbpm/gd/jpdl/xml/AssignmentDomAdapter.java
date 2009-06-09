package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Assignment;
import org.jbpm.gd.jpdl.model.ConfigType;

public class AssignmentDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("genericElement", "genericElement");
		}
		return NODE_TYPES;
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Assignment assignment = (Assignment)jpdlElement;
		assignment.setConfigInfo(getTextContent());
		assignment.setClassName(getAttribute("class"));
		assignment.setConfigType(getAttribute("config-type"));
		assignment.setExpression(getAttribute("expression"));
		assignment.setActorId(getAttribute("actor-id"));
		assignment.setPooledActors(getAttribute("pooled-actors"));
		assignment.addPropertyChangeListener(this);
	}
	
	protected void initialize() {
		super.initialize();
		Assignment assignment = (Assignment)getSemanticElement();
		if (assignment != null) {
			GenericElement[] genericElements = assignment.getGenericElements();
			for (int i = 0; i < genericElements.length; i++) {
				addElement(genericElements[i]);
			}
			setTextContent(assignment.getConfigInfo());
			setAttribute("class", assignment.getClassName());
			setAttribute("config-type", assignment.getConfigType());
			setAttribute("expression", assignment.getExpression());
			setAttribute("actor-id", assignment.getActorId());
			setAttribute("pooled-actors", assignment.getPooledActors());
		}
	}
	
	protected String getDefaultValue(String attributeName) {
		if ("config-type".equals(attributeName)) {
			return ConfigType.FIELD;
		} else {
			return super.getDefaultValue(attributeName);
		}
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("configInfo".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		} else if ("className".equals(evt.getPropertyName())) {
			setAttribute("class", (String)evt.getNewValue());
		} else if ("configType".equals(evt.getPropertyName())) {
			setAttribute("config-type", (String)evt.getNewValue());
		} else if ("expression".equals(evt.getPropertyName())) {
			setAttribute("expression", (String)evt.getNewValue());
		} else if ("actorId".equals(evt.getPropertyName())) {
			setAttribute("actor-id", (String)evt.getNewValue());
		} else if ("pooledActors".equals(evt.getPropertyName())) {
			setAttribute("pooled-actors", (String)evt.getNewValue());
		} else if ("genericElementAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("genericElementRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		}
	}
	
	private boolean isDifferent(String left, String right) {
		if (left == null) return right == null;
		return left.equals(right);
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Assignment assignment = (Assignment)getSemanticElement();
		if ("#text".equals(name) && assignment.getGenericElements().length == 0) {
			if (isDifferent(assignment.getConfigInfo(), getTextContent())) {
				assignment.setConfigInfo(newValue);
			}
		} else if ("class".equals(name)) {
			assignment.setClassName(newValue);
		} else if ("config-type".equals(name)) {
			assignment.setConfigType(newValue);
		} else if ("expression".equals(name)) {
			assignment.setExpression(newValue);
		} else if ("actor-id".equals(name)) {
			assignment.setActorId(newValue);
		} else if ("pooled-actors".equals(name) ) {
			assignment.setPooledActors(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Assignment assignment = (Assignment)getSemanticElement();
		if ("genericElement".equals(type)) {
			assignment.addGenericElement((GenericElement)jpdlElement);
		}
	}
	
	protected SemanticElement createSemanticElementFor(XmlAdapter child) {
		if ("genericElement".equals(child.getElementType())) {
			return getSemanticElementFactory().createById("org.jbpm.gd.jpdl.genericElement");
		} else {
			return super.createSemanticElementFor(child);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// an assignment cannot have any child nodes
	}
	
}
