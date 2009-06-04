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
	
	public static final String DUE_DATE = "org.jboss.tools.flow.jpdl4.model.eventListenerContainer.duedate";
	public static final String REPEAT = "org.jboss.tools.flow.jpdl4.model.eventListenerContainer.repeat";
	
	private String eventType;
	private String dueDate;
	private String repeat;
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
	
	public String getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getRepeat() {
		return repeat;
	}
	
	public void setRepeat(String repeat) {
		this.repeat = repeat;
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
				return getEventType();
			} else if (DUE_DATE.equals(id)) {
				return getDueDate();
			} else if (REPEAT.equals(id)) {
				return getRepeat();
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (LISTENERS.equals(id)) {
				return true;
			} else if (EVENT_TYPE.equals(id)) {
				return getEventType() != null;
			} else if (DUE_DATE.equals(id)) {
				return getDueDate() != null;
			} else if (REPEAT.equals(id)) {
				return getRepeat() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (EVENT_TYPE.equals(id)) {
				setEventType((String)value);
			} else if (DUE_DATE.equals(id)) {
				setDueDate((String)value);
			} else if (REPEAT.equals(id)) {
				setRepeat((String)value);
			}			
		}
		
	}
}
