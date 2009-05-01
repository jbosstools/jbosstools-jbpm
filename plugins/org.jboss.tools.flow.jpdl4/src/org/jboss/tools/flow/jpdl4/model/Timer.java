package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jboss.tools.flow.common.model.DefaultElement;
import org.jboss.tools.flow.common.properties.IPropertyId;

public class Timer extends DefaultElement {
	
	public static final String DUE_DATE = "org.jboss.tools.flow.jpdl4.model.timer.dueDate";
	public static final String REPEAT = "org.jboss.tools.flow.jpdl4.model.timer.repeat";
	public static final String DUE_DATETIME = "org.jboss.tools.flow.jpdl4.model.timer.dueDateTime";
	
	private String dueDate;
	private String repeat;
	private String dueDateTime;
	
	public Timer() {
		setMetaData("propertySource", new PropertySource());
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

	public String getDueDateTime() {
		return dueDateTime;
	}

	public void setDueDateTime(String dueDateTime) {
		this.dueDateTime = dueDateTime;
	}



	private class PropertySource implements IPropertySource, IPropertyId {
		
		private IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(DUE_DATE, "Due Date") {
					public String getCategory() {
						return "General";
					}
				},
				new TextPropertyDescriptor(REPEAT, "Repeat") {
					public String getCategory() {
						return "General";
					}
				},
				new TextPropertyDescriptor(DUE_DATETIME, "Due Date-time") {
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
			if (DUE_DATE.equals(id)) {
				return getDueDate() != null ? getDueDate() : "";
			} else if (REPEAT.equals(id)) {
				return getRepeat() != null ? getRepeat() : "";
			} else if (DUE_DATETIME.equals(id)) {
				return getDueDateTime() != null ? getDueDateTime() : "";
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (DUE_DATE.equals(id)) {
				return getDueDate() != null;
			} else if (REPEAT.equals(id)) {
				return getRepeat() != null;
			} else if (DUE_DATETIME.equals(id)) {
				return getDueDateTime() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (DUE_DATE.equals(id)) {
				if (value == null || value instanceof String) {
					setDueDate((String)value);
				}
			} else if (REPEAT.equals(id)) {
				if (value == null || value instanceof String) {
					setRepeat((String)value);
				}
			} else if (DUE_DATETIME.equals(id)) {
				if (value == null || value instanceof String) {
					setDueDateTime((String)value);
				}
			}
		}
		
	}
}
