package org.jbpm.gd.jpdl.util;

import java.util.HashSet;
import java.util.Set;

import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.ActionElement;
import org.jbpm.gd.jpdl.model.CreateTimer;
import org.jbpm.gd.jpdl.model.Delegation;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.NodeElement;
import org.jbpm.gd.jpdl.model.ProcessDefinition;
import org.jbpm.gd.jpdl.model.StartState;
import org.jbpm.gd.jpdl.model.SuperState;
import org.jbpm.gd.jpdl.model.Swimlane;
import org.jbpm.gd.jpdl.model.Task;
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.TimerContainer;

public class JavaClassNameCollector {
	
	public static Set getJavaClassNames(ProcessDefinition processDefinition) {
		HashSet result = new HashSet();
		addJavaClassNames(result, processDefinition.getActionElements());
		addJavaClassNames(result, processDefinition.getEvents());
		addJavaClassNames(result, processDefinition.getExceptionHandlers());
		addJavaClassNames(result, processDefinition.getNodeElements());
		addJavaClassNames(result, processDefinition.getStartState());
		addJavaClassNames(result, processDefinition.getSwimlanes());
		addJavaClassNames(result, processDefinition.getTasks());
		return result;
	}
	
	private static void addJavaClassNames(Set result, ActionElement[] actionElements) {
		for (int i = 0; i < actionElements.length; i++) {
			if (actionElements[i] instanceof Action) {
				Action action = (Action)actionElements[i];
				if (action.getClassName() != null) {
					result.add(action.getClassName());
				}
			}
			if (actionElements[i] instanceof CreateTimer) {
				Action action = ((CreateTimer)actionElements[i]).getAction();
				if (action != null && action.getClassName() != null) {
					result.add(action.getClassName());
				}
			}
		}
	}
	
	private static void addJavaClassNames(Set result, Event[] events) {
		for (int i = 0; i < events.length; i++) {
			addJavaClassNames(result, events[i].getActionElements());
		}
	}
	
	private static void addJavaClassNames(Set result, ExceptionHandler[] exceptionHandlers) {
		for (int i = 0; i < exceptionHandlers.length; i++) {
			addJavaClassNames(result, exceptionHandlers[i].getActionElements());
		}
	}
	
	private static void addJavaClassNames(Set result, NodeElement[] nodeElements) {
		for (int i = 0; i < nodeElements.length; i++) {
			addJavaClassNames(result, nodeElements[i].getEvents());
			addJavaClassNames(result, nodeElements[i].getExceptionHandlers());
			addJavaClassNames(result, nodeElements[i].getTransitions());
			if (nodeElements[i] instanceof TimerContainer) {
				addJavaClassNames(result, ((TimerContainer)nodeElements[i]).getTimers());
			}
			if (nodeElements[i] instanceof SuperState) {
				SuperState superState = (SuperState)nodeElements[i];
				addJavaClassNames(result, superState.getNodeElements());
			}
		}
	}
	
	private static void addJavaClassNames(Set result, StartState startState) {
		if (startState == null) return;
		addJavaClassNames(result, startState.getEvents());
		addJavaClassNames(result, startState.getExceptionHandlers());
		addJavaClassNames(result, startState.getTransitions());
	}
	
	private static void addJavaClassNames(Set result, Swimlane[] swimlanes) {
		for (int i = 0; i <swimlanes.length; i++) {
			addJavaClassName(result, swimlanes[i].getAssignment());
		}
	}
	
	private static void addJavaClassNames(Set result, Task[] tasks) {
		for (int i = 0; i < tasks.length; i++) {
			addJavaClassName(result, tasks[i].getAssignment());
			addJavaClassNames(result, tasks[i].getEvents());
			addJavaClassNames(result, tasks[i].getTimers());
			addJavaClassName(result, tasks[i].getController());
		}
	}
	
	private static void addJavaClassNames(Set result, Timer[] timers) {
		for (int i = 0; i < timers.length; i++) {
			if (timers[i].getAction() != null) {
				Action action = timers[i].getAction();
				if (action.getClassName() != null) {
					result.add(action.getClassName());
				}
			}
		}
	}
	
	private static void addJavaClassName(Set result, Delegation delegation) {
		if (delegation != null && delegation.getClassName() != null) {
			result.add(delegation.getClassName());
		}
	}
	
}
