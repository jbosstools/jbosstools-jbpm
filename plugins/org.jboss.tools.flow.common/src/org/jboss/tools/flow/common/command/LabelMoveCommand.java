package org.jboss.tools.flow.common.command;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.jboss.tools.flow.common.wrapper.LabelWrapper;


public class LabelMoveCommand extends Command {
	
	LabelWrapper labelWrapper = null;
	Point location = null;
	IFigure parent = null;
	Point oldOffset = null;
	Point newOffset = null;
	
	public LabelMoveCommand(LabelWrapper labelWrapper, IFigure parent, Point location) {
		this.labelWrapper = labelWrapper;
		this.parent = parent;
		this.location = location;
	}
	
	public void execute() {
		if (oldOffset == null) {
			oldOffset = labelWrapper.getLocation();
		}
		if (newOffset == null) {
			newOffset = labelWrapper.getLocation().getCopy();
			parent.translateToAbsolute(newOffset);
			newOffset.translate(location);
			parent.translateToRelative(newOffset);
		}
		labelWrapper.setLocation(newOffset);
	}
	
	public void undo() {
		labelWrapper.setLocation(oldOffset);
	}

}
