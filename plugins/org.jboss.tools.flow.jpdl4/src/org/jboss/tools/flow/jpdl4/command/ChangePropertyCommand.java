package org.jboss.tools.flow.jpdl4.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

public class ChangePropertyCommand extends Command {

    private IPropertySource target;
    private Object newValue;
    private Object oldValue;
    private Object propertyId;
    

    public void execute() {
		if (oldValue == null) {
			oldValue = target.getPropertyValue(propertyId);
		}
		target.setPropertyValue(propertyId, newValue);
    }
    
    public boolean canExecute() {
    	return target != null;
    }
    
    public void setPropertyId(Object propertyId) {
    	this.propertyId = propertyId;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public void setOldValue(Object oldValue) {
       this.oldValue = oldValue;
    }

    public void setTarget(IPropertySource target) {
        this.target = target;
    }

    public void undo() {
    	target.setPropertyValue(propertyId, oldValue);
    }
}
