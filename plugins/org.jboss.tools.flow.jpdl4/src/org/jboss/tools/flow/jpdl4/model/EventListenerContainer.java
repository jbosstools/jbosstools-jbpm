package org.jboss.tools.flow.jpdl4.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jboss.tools.flow.common.model.DefaultElement;

public class EventListenerContainer extends DefaultElement {
	
	public static final String LISTENERS = "org.jboss.tools.flow.jpdl4.model.eventListenerContainer.listeners";
	public static final String EVENT_TYPE = "org.jboss.tools.flow.jpdl4.model.eventListenerContainer.eventType";
	
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
		
		private IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(EVENT_TYPE, "Event Type") {
					public String getCategory() {
						return "General";
					}
				}
		};

		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return propertyDescriptors;
		}

		public Object getPropertyValue(Object id) {
			if (LISTENERS.equals(id)) {
				return listeners;
			} else if (EVENT_TYPE.equals(id)) {
				return getEventType() != null ? getEventType() : "";
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (LISTENERS.equals(id)) {
				return true;
			} else if (EVENT_TYPE.equals(id)) {
				return getEventType() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (EVENT_TYPE.equals(id)) {
				if (value == null || value instanceof String) {
					setEventType((String)value);
				}
			}
		}
		
	}
}
