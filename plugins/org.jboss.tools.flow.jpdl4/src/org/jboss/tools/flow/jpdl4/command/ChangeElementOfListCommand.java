package org.jboss.tools.flow.jpdl4.command;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

public class ChangeElementOfListCommand extends Command {

    private IPropertySource target;
    
    private Object listId;
    private int index;
    private Object newValue;
    private Object oldValue;
    
    @SuppressWarnings("unchecked")
	public void execute() {
		Object object = target.getPropertyValue(listId);
		if (! (object instanceof List)) return;
		if (oldValue == null) {
			oldValue = ((List)object).get(index);
		}
		((List)object).set(index, newValue);
    }
    
    public boolean canExecute() {
    	return target != null;
    }
    
    public void setListId(Object listId) {
    	this.listId = listId;
    }

    public void setIndex(int index) {
    	this.index = index;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public void setTarget(IPropertySource target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
	public void undo() {
		Object object = target.getPropertyValue(listId);
		if (! (object instanceof List)) return;
		((List)object).set(index, oldValue);
    }
}
