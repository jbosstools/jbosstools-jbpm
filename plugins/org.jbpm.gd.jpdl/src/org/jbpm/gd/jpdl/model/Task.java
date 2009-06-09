package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task extends Swimlane implements EventContainer, TimerContainer, DescribableElement {
	
	private Controller controller;
	private Reminder reminder;
	private List events = new ArrayList();
	private List timers = new ArrayList();
	private String blocking = "false";
	private String signalling = "true";
	private String notify = "false";
	private Description description;
	private String dueDate;
	private String swimlane;
	private String priority = "normal";
	private HashMap customProperties = new HashMap();
	
	public void setProperty(String name, String newValue) {
		String oldValue = (String)customProperties.get(name);
		customProperties.put(name, newValue);
		firePropertyChange("custom", new String[] { name, oldValue }, new String[] {name, newValue});
	}
	
	public String getProperty(String name) {
		return (String)customProperties.get(name);
	}
	
	public Map getProperties() {
		return new HashMap(customProperties);
	}
	
	
	public void setReminder(Reminder newReminder) {
		Reminder oldReminder = reminder;
		reminder = newReminder;
		firePropertyChange("reminder", oldReminder, newReminder);
	}
	
	public Reminder getReminder() {
		return reminder;
	}
	
	public void setController(Controller newController) {
		Controller oldController = controller;
		controller = newController;
		firePropertyChange("controller", oldController, newController);
	}
	
	public Controller getController() {
		return controller;
	}
	
	public void addEvent(Event event) {
		events.add(event);
		firePropertyChange("eventAdd", null, event);
	}
	
	public void removeEvent(Event event) {
		events.remove(event);
		firePropertyChange("eventRemove", event, null);
	}
	
	public Event[] getEvents() {
		return (Event[])events.toArray(new Event[events.size()]);
	}
	
	public void addTimer(Timer timer) {
		timers.add(timer);
		firePropertyChange("timerAdd", null, timer);
	}
	
	public void removeTimer(Timer timer) {
		timers.remove(timer);
		firePropertyChange("timerRemove", timer, null);
	}
	
	public Timer[] getTimers() {
		return (Timer[])timers.toArray(new Timer[timers.size()]);
	}
	
	public void setBlocking(String newBlocking) {
		String oldBlocking = blocking;
		blocking = newBlocking;
		firePropertyChange("blocking", oldBlocking, newBlocking);
	}
	
	public String getBlocking() {
		return blocking;
	}
	
	public void setSignalling(String newSignalling) {
		String oldSignalling = signalling;
		signalling = newSignalling;
		firePropertyChange("signalling", oldSignalling, newSignalling);
	}
	
	public String getSignalling() {
		return signalling;
	}
	
	public void setNotify(String newNotify) {
		String oldNotify = notify;
		notify = newNotify;
		firePropertyChange("notify", oldNotify, newNotify);
	}
	
	public String getNotify() {
		return notify;
	}
	
	public void setDescription(Description newDescription) {
		Description oldDescription = description;
		description = newDescription;
		firePropertyChange("description", oldDescription, newDescription);
	}
	
	public Description getDescription() {
		return description;
	}
	
	public void setDueDate(String newDueDate) {
		String oldDueDate = dueDate;
		dueDate = newDueDate;
		firePropertyChange("duedate", oldDueDate, newDueDate);
	}
	
	public String getDueDate() {
		return dueDate;
	}
	
	public void setSwimlane(String newSwimlane) {
		String oldSwimlane = swimlane;
		swimlane = newSwimlane;
		firePropertyChange("swimlane", oldSwimlane, newSwimlane);
	}
	
	public String getSwimlane() {
		return swimlane;
	}
	
	public void setPriority(String newPriority) {
		String oldPriority = priority;
		priority = newPriority;
		firePropertyChange("priority", oldPriority, newPriority);
	}
	
	public String getPriority() {
		return priority;
	}
	
	public boolean isNameMandatory() {
		return false;
	}

}
