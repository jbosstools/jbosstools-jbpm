package org.jbpm.gd.jpdl.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.Task;
import org.jbpm.gd.jpdl.model.TaskContainer;

public class TaskListOutlineEditPart extends OutlineEditPart {
	
	public TaskListOutlineEditPart(TaskContainer model) {
		super(model);
	}

	protected Image getImage() {
		String iconPath = "icons/full/obj16/tasks_multiple.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jbpm.gd.jpdl").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}

	protected String getText() {
		return "Tasks";
	}
	
	protected List getModelChildren() {
		List result = new ArrayList();
		Task[] tasks = ((TaskContainer)getModel()).getTasks();
		for (int i = 0; i < tasks.length; i++) {
			result.add(tasks[i]);
		}
		return result;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("taskAdd")) {
			handleChildAdd(evt.getNewValue());
		} else if (eventName.equals("taskRemove")) {
			refreshChildren();
			getViewer().select(this);
		} else {
			super.propertyChange(evt);
		}
	}
}
