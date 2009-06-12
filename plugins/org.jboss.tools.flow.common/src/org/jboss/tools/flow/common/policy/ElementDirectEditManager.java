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

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.wrapper.Wrapper;

/**
 * Manager for directly editing elements.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ElementDirectEditManager extends DirectEditManager {

    private IPropertySource propertySource;

    public ElementDirectEditManager(GraphicalEditPart source, CellEditorLocator locator) {
        super(source, TextCellEditor.class, locator);
        Object object = source.getModel();
        if (object instanceof Wrapper) {
        	propertySource = (IPropertySource)((Wrapper)object).getAdapter(IPropertySource.class);
        }
    }

    protected void initCellEditor() {
    	String initialValue = null;
    	if (propertySource != null) {
    		initialValue = (String)propertySource.getPropertyValue(IPropertyId.NAME);
    	}
        getCellEditor().setValue(initialValue == null ? "" : initialValue);
        Text text = (Text) getCellEditor().getControl();
        text.selectAll();
    }
}
