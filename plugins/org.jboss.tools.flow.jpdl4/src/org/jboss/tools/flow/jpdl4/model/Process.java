 package org.jboss.tools.flow.jpdl4.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.DefaultFlow;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.jpdl4.util.Jpdl4Helper;

public class Process extends DefaultFlow {
	
	public static final String KEY = "key";
	public static final String VERSION = "version";
	public static final String DESCRIPTION = "description";
	
	private Node initial = null;
	private String key;
	private String version;
	private String description;
	
	private List<Swimlane> swimlanes = new ArrayList<Swimlane>();
	private List<Timer> timers = new ArrayList<Timer>();
	private List<EventListenerContainer> eventlisteners = new ArrayList<EventListenerContainer>();
	
	public Process() {
		setMetaData("propertySource", new PropertySource());
	}
	
	public StartEvent getStartState() {
		for (Iterator<Node> iterator = getNodes().iterator(); iterator.hasNext(); ) {
			Node node = iterator.next();
			if (node instanceof StartEvent) {
				return (StartEvent)node;
			}
		}
		return null;
	}
	
	public Node getInitial() {
		return initial;
	}
	
	public void setInitial(Node node) {
		initial = node;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void removeNode(Node node) {
		Jpdl4Helper.mergeLeadingNodes(node);
		super.removeNode(node);
	}
		
	private class PropertySource implements IPropertySource {
		
		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return new IPropertyDescriptor[0];
		}

		public Object getPropertyValue(Object id) {
			if ("swimlane".equals(id)) {
				return swimlanes;
			} else if ("timer".equals(id)) {
				return timers;
			} else if ("eventListener".equals(id)) {
				return eventlisteners;
			} else if (KEY.equals(id)) {
				return getKey();
			} else if (VERSION.equals(id)) {
				return getVersion();
			} else if (DESCRIPTION.equals(id)) {
				return getDescription();
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if ("swimlane".equals(id)) {
				return true;
			} else if ("timer".equals(id)) {
				return true;
			} else if ("eventListener".equals(id)) {
				return true;
			} else if (KEY.equals(id)) {
				return getKey() != null;
			} else if (VERSION.equals(id)) {
				return getVersion() != null;
			} else if (DESCRIPTION.equals(id)) {
				return getDescription() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (KEY.equals(id)) {
				setKey((String)value);
			} else if (VERSION.equals(id)) {
				setVersion((String)value);
			} else if (DESCRIPTION.equals(id)) {
				setDescription((String)value);
			}
		}
		
	}
}
