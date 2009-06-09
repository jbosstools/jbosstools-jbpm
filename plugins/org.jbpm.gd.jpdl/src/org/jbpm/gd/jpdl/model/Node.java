package org.jbpm.gd.jpdl.model;



public class Node extends AbstractAsyncableTimerNode {

	private ActionElement actionElement;

	public void setAction(Action newActionElement) {
		ActionElement oldActionElement = actionElement;
		actionElement = newActionElement;
		firePropertyChange("action", oldActionElement, newActionElement);
	}
	
	public Action getAction() {
		if (actionElement instanceof Action) {
			return (Action)actionElement;
		}
		return null;
	}
	
	public void setScript(Script newActionElement) {
		ActionElement oldActionElement = actionElement;
		actionElement = newActionElement;
		firePropertyChange("script", oldActionElement, newActionElement);
	}
	
	public Script getScript() {
		if (actionElement instanceof Script) {
			return (Script)actionElement;
		}
		return null;
	}

	public void setCreateTimer(CreateTimer newActionElement) {
		ActionElement oldActionElement = actionElement;
		actionElement = newActionElement;
		firePropertyChange("createTimer", oldActionElement, newActionElement);
	}
	
	public CreateTimer getCreateTimer() {
		if (actionElement instanceof CreateTimer) {
			return (CreateTimer)actionElement;
		}
		return null;
	}

	public void setCancelTimer(CancelTimer newActionElement) {
		ActionElement oldActionElement = actionElement;
		actionElement = newActionElement;
		firePropertyChange("cancelTimer", oldActionElement, newActionElement);
	}
	
	public CancelTimer getCancelTimer() {
		if (actionElement instanceof CancelTimer) {
			return (CancelTimer)actionElement;
		}
		return null;
	}

	public void setMail(MailAction newActionElement) {
		ActionElement oldActionElement = actionElement;
		actionElement = newActionElement;
		firePropertyChange("mail", oldActionElement, newActionElement);
	}
	
	public MailAction getMail() {
		if (actionElement instanceof MailAction) {
			return (MailAction)actionElement;
		}
		return null;
	}
	
	public boolean isActionElementConfigurable() {
		return true;
	}

}
