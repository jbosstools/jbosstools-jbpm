package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jboss.tools.flow.common.properties.IPropertyId;


public class SubprocessTask extends Task {
	
	public final static String ID = "org.jboss.tools.flow.jpdl4.model.subprocess.id";
	public final static String KEY = "org.jboss.tools.flow.jpdl4.model.subprocess.key";
	public final static String OUTCOME = "org.jboss.tools.flow.jpdl4.model.subprocess.outcome";
	
	private String subprocessId;
	private String key;
	private String outcome;
	
	public SubprocessTask() {
		setMetaData("propertySource", new PropertySource());
	}
	
	public String getSubprocessId() {
		return subprocessId;
	}

	public void setSubprocessId(String id) {
		this.subprocessId = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	protected boolean isPropagationExclusive() {
		return true;
	}
	
	private class PropertySource implements IPropertySource, IPropertyId {
		
		private IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(ID, "Id") {
					public String getCategory() {
						return "General";
					}
				},
				new TextPropertyDescriptor(KEY, "Key") {
					public String getCategory() {
						return "General";
					}
				},
				new TextPropertyDescriptor(OUTCOME, "Outcome") {
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
			if (ID.equals(id)) {
				return getSubprocessId() != null ? getSubprocessId() : "";
			} else if (KEY.equals(id)) {
				return getKey() != null ? getKey() : "";
			} else if (OUTCOME.equals(id)) {
				return getOutcome() != null ? getOutcome() : "";
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (ID.equals(id)) {
				return getSubprocessId() != null;
			} else if (KEY.equals(id)) {
				return getKey() != null;
			} else if (OUTCOME.equals(id)) {
				return getOutcome() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (ID.equals(id)) {
				if (value == null || value instanceof String) {
					setSubprocessId((String)value);
				}
			} else if (KEY.equals(id)) {
				if (value == null || value instanceof String) {
					setKey((String)value);
				}
			} else if (OUTCOME.equals(id)) {
				if (value == null || value instanceof String) {
					setOutcome((String)value);
				}
			}
		}
		
	}
}
