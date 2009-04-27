package org.jboss.tools.flow.jpdl4.command;

import org.jboss.tools.flow.common.command.AddNodeCommand;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.jpdl4.util.Jpdl4Helper;

public class AddProcessNodeCommand extends AddNodeCommand {

    public void execute() {
    	if (getChild().getName() == null) {
    		initializeChildName();
    	}
        getParent().addElement(getChild());
        getChild().setParent(getParent());
    }
    
    private void initializeChildName() {
    	if (getChild().getElement() instanceof Node && getParent().getElement() instanceof Flow) {
    		getChild().setName(Jpdl4Helper.getLabel((Node)getChild().getElement(), (Flow)getParent().getElement()));
    	} else {
    		String name = getChild().getElement().getClass().getName();
    		name = name.substring(name.lastIndexOf('.') + 1);
    		getChild().setName(name);
    	}
    }

 }
