package org.jboss.tools.flow.common.figure;

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

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class ElementContainerFigure extends Figure implements ElementFigure {
    
    private IFigure pane;
    private boolean selected = false;
    
    public ElementContainerFigure() {
        setSize(200, 150);
        ScrollPane scrollpane = new ScrollPane();
        pane = new FreeformLayer();
        pane.setLayoutManager(new FreeformLayout());
        setLayoutManager(new StackLayout());
        add(scrollpane);
        scrollpane.setViewport(new FreeformViewport());
        scrollpane.setContents(pane);
        setBorder(new LineBorder(1));
    }
    
    public void setColor(Color color) {
    	setBackgroundColor(color);
    }

    public Label getLabel() {
        return null;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setIcon(Image icon) {
        // Do nothing
    }

    public void setSelected(boolean b) {
        this.selected = b;
        ((LineBorder) getBorder()).setWidth(b ? 3 : 1);
    }

    public void setText(String text) {
        // Do nothing
    }
    
    public IFigure getPane() {
        return pane;
    }

    public ConnectionAnchor getSourceConnectionAnchor() {
        return new ChopboxAnchor(this);
    }

    public ConnectionAnchor getTargetConnectionAnchor() {
        return new ChopboxAnchor(this);
    }

}
