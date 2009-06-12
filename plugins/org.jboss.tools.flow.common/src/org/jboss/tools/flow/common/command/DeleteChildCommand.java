package org.jboss.tools.flow.common.command;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class DeleteChildCommand extends Command {

    private Wrapper child;
    private Wrapper parent;
    private Object type;

    public void execute() {
        parent.removeChild(type, child);
    }

    public void setChild(Wrapper newChild) {
        child = newChild;
    }
    
    public void setType(Object newType) {
    	type = newType;
    }

    public void setParent(Wrapper newParent) {
        parent = newParent;
    }

    public void undo() {
        parent.addChild(type, child);
    }
    
    public boolean canExecute() {
    	return child != null && parent != null && type != null;
    }
    
}
