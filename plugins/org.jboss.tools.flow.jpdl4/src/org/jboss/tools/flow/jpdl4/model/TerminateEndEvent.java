package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;



public class TerminateEndEvent extends EndEvent {
	
	public final static String ENDS = "ends";
	public final static String STATE = "state";
	
	private String ends;
	private String state;
	
	public TerminateEndEvent() {
		setMetaData("propertySource", new PropertySource());
	}

	public String getEnds() {
		return ends;
	}
	
	public void setEnds(String ends) {
		this.ends = ends;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	private class PropertySource implements IPropertySource {
		
		public IPropertyDescriptor[] getPropertyDescriptors() {
			return new IPropertyDescriptor[0];
		}

		public Object getPropertyValue(Object id) {
			if (ENDS.equals(id)) {
				return getEnds();
			} else if (STATE.equals(id)) {
				return getState();
			} else {
				return null;
			}
		}

		public boolean isPropertySet(Object id) {
			if (ENDS.equals(id)) {
				return getEnds() != null;
			} else if (STATE.equals(id)) {
				return getState() != null;
			} else {
				return false;
			}
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (ENDS.equals(id)) {
				setEnds((String)value);
			} else if (STATE.equals(id)) {
				setState((String)value);
			}
		}

		public Object getEditableValue() {
			return null;
		}
		
	}

}
