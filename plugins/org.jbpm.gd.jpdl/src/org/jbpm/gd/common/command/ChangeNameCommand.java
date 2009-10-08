
package org.jbpm.gd.common.command;

import org.eclipse.gef.commands.Command;
import org.jbpm.gd.common.model.NamedElement;


public class ChangeNameCommand extends Command {
	
	private String oldName;
	private String newName;
	private NamedElement namedElement;
	
	public void setName(String name) {
		newName = name;
	}
	
	public void setNamedElement(NamedElement namedElement) {
		this.namedElement = namedElement;
	}
	
	public void execute() {
		oldName = namedElement.getName();
		if ("".equals(newName) && !namedElement.isNameMandatory()) {
			newName = null;
		}
		namedElement.setName(newName.trim());
	}
	
	public void undo() {
		namedElement.setName(oldName);
	}
	
}
