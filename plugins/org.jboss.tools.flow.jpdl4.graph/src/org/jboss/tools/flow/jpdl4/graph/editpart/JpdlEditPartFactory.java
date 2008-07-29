package org.jboss.tools.flow.jpdl4.graph.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.flow.editor.editpart.ContainerEditPart;
import org.jboss.tools.flow.editor.editpart.RootEditPart;
import org.jboss.tools.flow.jpdl4.graph.wrapper.EndStateWrapper;
import org.jboss.tools.flow.jpdl4.graph.wrapper.ProcessWrapper;
import org.jboss.tools.flow.jpdl4.graph.wrapper.StartStateWrapper;
import org.jboss.tools.flow.jpdl4.graph.wrapper.StateWrapper;
import org.jboss.tools.flow.jpdl4.graph.wrapper.SuperStateWrapper;
import org.jboss.tools.flow.jpdl4.graph.wrapper.TransitionWrapper;

public class JpdlEditPartFactory implements EditPartFactory {
    
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart result = null;
        if (model instanceof ProcessWrapper) {
            result = new RootEditPart();
        } else if (model instanceof SuperStateWrapper) {
        	result = new ContainerEditPart();
        } else if (model instanceof StartStateWrapper) {
            result = new StartStateEditPart();
        } else if (model instanceof StateWrapper) {
            result = new StateEditPart();
        } else if (model instanceof EndStateWrapper) {
            result = new EndStateEditPart();
        } else if (model instanceof TransitionWrapper) {
            result = new TransitionEditPart();
        } else {
            throw new IllegalArgumentException(
                "Unknown model object " + model);
        }
        result.setModel(model);
        return result;
    }

}
