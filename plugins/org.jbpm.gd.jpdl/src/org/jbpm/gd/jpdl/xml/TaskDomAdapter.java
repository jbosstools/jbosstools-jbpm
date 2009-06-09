package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Assignment;
import org.jbpm.gd.jpdl.model.BooleanType;
import org.jbpm.gd.jpdl.model.Controller;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.PriorityType;
import org.jbpm.gd.jpdl.model.Reminder;
import org.jbpm.gd.jpdl.model.Task;
import org.jbpm.gd.jpdl.model.Timer;

public class TaskDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "assignment", "controller", "event", "timer", "reminder"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("assignment", "assignment");
			NODE_TYPES.put("controller", "controller");
			NODE_TYPES.put("event", "event");
			NODE_TYPES.put("timer", "timer");
			NODE_TYPES.put("reminder", "reminder");
		}
		return NODE_TYPES;
	}
	
	protected String getDefaultValue(String attributeName) {
		if ("blocking".equals(attributeName)) {
			return BooleanType.FALSE;
		} else if ("signalling".equals(attributeName)) {
			return BooleanType.TRUE;
		} else if ("notify".equals(attributeName)) {
			return BooleanType.FALSE;
		} else if ("priority".equals(attributeName)) {
			return PriorityType.NORMAL;
		} else {
			return super.getDefaultValue(attributeName);
		}
	}
	
	protected void initialize() {
		super.initialize();
		Task task = (Task)getSemanticElement();
		if (task != null) {
			Map properties = task.getProperties();
			Iterator iterator = properties.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String)iterator.next();
				setAttribute(key, (String)properties.get(key));
			}
			setAttribute("name", task.getName());
			setAttribute("blocking", task.getBlocking());
			setAttribute("signalling", task.getSignalling());
			setAttribute("duedate", task.getDueDate());
			setAttribute("swimlane", task.getSwimlane());
			setAttribute("priority", task.getPriority());
			setAttribute("notify", task.getNotify());
			addElement(task.getDescription());
			addElement(task.getAssignment());
			addElement(task.getController());
			addElements(task.getEvents());
			addElements(task.getTimers());
			addElement(task.getReminder());
		}
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Task task = (Task)jpdlElement;
		task.addPropertyChangeListener(this);
		Map map = getAttributes();
		Iterator iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String next = (String)iterator.next();
			if ("name".equals(next)) {
				task.setName(getAttribute("name"));						
			} else if ("blocking".equals(next)) {
				task.setBlocking(getAttribute("blocking"));
			} else if ("signalling".equals(next)) {
				task.setSignalling(getAttribute("signalling"));
			} else if ("duedate".equals(next)) {
				task.setDueDate(getAttribute("duedate"));
			} else if ("swimlane".equals(next)) {
				task.setSwimlane(getAttribute("swimlane"));
			} else if ("priority".equals(next)) {
				task.setPriority(getAttribute("priority"));
			} else if ("notify".equals(next)) {
				task.setNotify(getAttribute("notify"));
			}
			task.setProperty(next, (String)map.get(next));
		}
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("assignment".equals(evt.getPropertyName())) {
			setElement("assignment", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("controller".equals(evt.getPropertyName())) {
			setElement("controller", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
		} else if ("eventAdd".equals(evt.getPropertyName())) {
			addElement((Event)evt.getNewValue());
		} else if ("eventRemove".equals(evt.getPropertyName())) {
			removeElement((Event)evt.getOldValue());
		} else if ("timerAdd".equals(evt.getPropertyName())) {
			addElement((Timer)evt.getNewValue());
		} else if ("timerRemove".equals(evt.getPropertyName())) {
			removeElement((Timer)evt.getOldValue());
		} else if ("reminder".equals(evt.getPropertyName())) {
			setElement("reminder", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		} else if ("blocking".equals(evt.getPropertyName())) {
			setAttribute("blocking", (String)evt.getNewValue());
		} else if ("signalling".equals(evt.getPropertyName())) {
			setAttribute("signalling", (String)evt.getNewValue());
		} else if ("notify".equals(evt.getPropertyName())) {
			setAttribute("notify", (String)evt.getNewValue());
		} else if ("duedate".equals(evt.getPropertyName())) {
			setAttribute("duedate", (String)evt.getNewValue());
		} else if ("swimlane".equals(evt.getPropertyName())) {
			setAttribute("swimlane", (String)evt.getNewValue());
		} else if ("priority".equals(evt.getPropertyName())) {
			setAttribute("priority", (String)evt.getNewValue());
		} else if ("custom".equals(evt.getPropertyName())) {
			String name = ((String[])evt.getNewValue())[0];
			String newValue = ((String[])evt.getNewValue())[1];
			setAttribute(name, newValue);
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Task task = (Task)getSemanticElement();
		if ("name".equals(name)) {
			task.setName(newValue);
		} else if ("blocking".equals(name)) {
			task.setBlocking(newValue);
		} else if ("signalling".equals(name)) {
			task.setSignalling(newValue);
		} else if ("notify".equals(name)) {
			task.setNotify(newValue);
		} else if ("duedate".equals(name)) {
			task.setDueDate(newValue);
		} else if ("swimlane".equals(name)) {
			task.setSwimlane(newValue);
		} else if ("priority".equals(name)) {
			task.setPriority(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Task task = (Task)getSemanticElement();
		if ("assignment".equals(type)) {
			task.setAssignment((Assignment)jpdlElement);
		} else if ("controller".equals(type)) {
			task.setController((Controller)jpdlElement);
		} else if ("event".equals(type)) {
			task.addEvent((Event)jpdlElement);
		} else if ("timer".equals(type)) {
			task.addTimer((Timer)jpdlElement);
		} else if ("description".equals(type)) {
			task.setDescription((Description)jpdlElement);
		} else if ("reminder".equals(type)) {
			task.setReminder((Reminder)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		Task task = (Task)getSemanticElement();
		if ("assignment".equals(type)) {
			task.setAssignment(null);
		} else if ("controller".equals(type)) {
			task.setController(null);
		} else if ("event".equals(type)) {
			task.removeEvent((Event)child.getSemanticElement());
		} else if ("timer".equals(type)) {
			task.removeTimer((Timer)child.getSemanticElement());
		} else if ("description".equals(type)) {
			task.setDescription(null);
		} else if ("reminder".equals(type)) {
			task.setReminder(null);
		}
	}
	
}
