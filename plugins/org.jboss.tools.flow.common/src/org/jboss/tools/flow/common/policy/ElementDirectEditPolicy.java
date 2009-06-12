package org.jboss.tools.flow.common.policy;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.command.RenameElementCommand;
import org.jboss.tools.flow.common.figure.ElementFigure;
import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.wrapper.Wrapper;

/**
 * Policy for directly editing elements.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ElementDirectEditPolicy extends DirectEditPolicy {

    protected Command getDirectEditCommand(DirectEditRequest request) {
        RenameElementCommand cmd = new RenameElementCommand();
        cmd.setSource((Wrapper) getHost().getModel());
        IPropertySource propertySource = (IPropertySource)((Wrapper)getHost().getModel()).getAdapter(IPropertySource.class);
        if (propertySource != null) {
        	cmd.setOldName((String)propertySource.getPropertyValue(IPropertyId.NAME));
        }
        cmd.setName((String) request.getCellEditor().getValue());
        return cmd;
    }

    protected void showCurrentEditValue(DirectEditRequest request) {
        String value = (String) request.getCellEditor().getValue();
        IFigure figure = getHostFigure();
        if (figure instanceof Label) {
        	((Label)figure).setText(value);
        } else if (figure instanceof ElementFigure){
        	((ElementFigure)figure).setText(value);
        }
    }

}
