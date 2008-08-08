package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.flow.common.editpart.ConnectionEditPart;
import org.jboss.tools.flow.common.editpart.ContainerEditPart;
import org.jboss.tools.flow.common.editpart.RootEditPart;
import org.jboss.tools.flow.common.wrapper.DefaultConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultContainerWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultFlowWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultNodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.EndState;
import org.jboss.tools.flow.jpdl4.model.StartState;
import org.jboss.tools.flow.jpdl4.model.State;

public class JpdlEditPartFactory implements EditPartFactory {
    
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart result = null;
        if (!(model instanceof Wrapper)) return result;
        Object element = ((Wrapper)model).getElement();
        if (model instanceof DefaultFlowWrapper) {
            result = new RootEditPart();
        } else if (model instanceof DefaultContainerWrapper) {
        	result = new ContainerEditPart();
        } else if (model instanceof DefaultNodeWrapper && element instanceof StartState) {
            result = new StartStateEditPart();
        } else if (model instanceof DefaultNodeWrapper && element instanceof State) {
            result = new StateEditPart();
        } else if (model instanceof DefaultNodeWrapper && element instanceof EndState) {
            result = new EndStateEditPart();
        } else if (model instanceof DefaultConnectionWrapper) {
            result = new ConnectionEditPart();
        } else {
            throw new IllegalArgumentException(
                "Unknown model object " + model);
        }
        result.setModel(model);
        return result;
    }

}
