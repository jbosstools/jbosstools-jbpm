package org.jboss.tools.flow.jpdl4.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.DefaultElement;

public class EventListenerContainer extends DefaultElement {
	
	private String eventType;
	private List<EventListener> listeners = new ArrayList<EventListener>();
	
	public EventListenerContainer() {
		setMetaData("propertySource", new PropertySource());
	}
	
	public String getEventType() {
		return eventType;
	}
	
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public List<EventListener> getListeners() {
		return listeners;
	}
	
	public void setListeners(List<EventListener> listeners) {
		this.listeners = listeners;
	}
	
	private class PropertySource implements IPropertySource {
		
		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return new IPropertyDescriptor[0];
		}

		public Object getPropertyValue(Object id) {
			if ("listener".equals(id)) {
				return listeners;
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if ("listener".equals(id)) {
				return true;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
		}
		
	}
}
