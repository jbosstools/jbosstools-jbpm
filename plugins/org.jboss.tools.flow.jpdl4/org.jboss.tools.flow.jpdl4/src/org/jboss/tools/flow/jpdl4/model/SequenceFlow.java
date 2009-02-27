package org.jboss.tools.flow.jpdl4.model;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.DefaultConnection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.properties.IPropertyId;

public class SequenceFlow extends DefaultConnection {
	
	private String name;
	private boolean conditional = false;

	public SequenceFlow() {
		this(null, null);
	}
	
	public SequenceFlow(Node from, Node to) {
		super(from, to);
		setMetaData("propertySource", new PropertySource());
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setConditional(boolean conditional) {
		this.conditional = conditional;
	}
	
	public boolean isConditional() {
		return conditional;
	}
	
	public boolean isDefault() {
		Node from = getFrom();
		if (from != null) {
			List<Connection> outgoingConnections = from.getOutgoingConnections("");
			if (outgoingConnections != null) {
				return outgoingConnections.size() > 1 && outgoingConnections.get(0) == this;
			}
		}
		return false;
	}
	
	private class PropertySource implements IPropertySource, IPropertyId {
		
		private IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(NAME, "Name") {}
		};

		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return propertyDescriptors;
		}

		public Object getPropertyValue(Object id) {
			if (NAME.equals(id) || LABEL.equals(id)) {
				return getName() != null ? getName() : "";
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (NAME.equals(id) || LABEL.equals(id)) {
				return getName() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (NAME.equals(id) || LABEL.equals(id)) {
				if (value instanceof String) {
					setName((String)value);
				}
			} 
		}
		
	}
		
}
