package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class ProcessTreeRootEditPart extends JpdlTreeEditPart {
	
	public ProcessTreeRootEditPart(FlowWrapper flowWrapper) {
		super(flowWrapper);
	}
	
	protected void createEditPolicies() {
	}

	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		FlowWrapper flowWrapper = (FlowWrapper)getModel();
		if (flowWrapper == null) return null;
		addSwimlanes(result, flowWrapper);
		addTimers(result, flowWrapper);
		return result;
	}
	
	private void addSwimlanes(List<Object> list, FlowWrapper wrapper) {
		List<Element> swimlanes = wrapper.getChildren("swimlane");
		if (swimlanes != null && !(swimlanes.isEmpty())) {
			list.add(new SwimlaneListTreeEditPart(swimlanes));
		}
	}
	
	private void addTimers(List<Object> list, FlowWrapper wrapper) {
		List<Element> timers = wrapper.getChildren("timer");
		if (timers != null && !(timers.isEmpty())) {
			list.add(new TimerListTreeEditPart(timers));
		}
	}
	
    public void modelChanged(ModelEvent event) {
    	if (event.getChangeType() == Wrapper.ADD_ELEMENT) {
    		refreshChildren();
    		Object object = event.getNewValue();
    		EditPart editPart = (EditPart)getViewer().getEditPartRegistry().get(object);
    		getViewer().select(editPart);
    	} else if (event.getChangeType() == Wrapper.REMOVE_ELEMENT) {
    		refreshChildren();
    	}
    }
    
    public void activate() {
        super.activate();
        ((FlowWrapper)getModel()).addListener(this);
    }

    public void deactivate() {
    	((FlowWrapper)getModel()).removeListener(this);
        super.deactivate();
    }

}
