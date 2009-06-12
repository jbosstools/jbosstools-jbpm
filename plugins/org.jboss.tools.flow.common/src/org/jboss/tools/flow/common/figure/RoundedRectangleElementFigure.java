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

import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class RoundedRectangleElementFigure extends AbstractElementFigure {

    protected RoundedRectangle rectangle;

    protected void customizeFigure() {
        rectangle = new RoundedRectangle();
        rectangle.setCornerDimensions(new Dimension(25, 25));
        add(rectangle, 0);
        setSize(92, 52);
        setSelected(false);
    }
    
    public void setColor(Color color) {
        rectangle.setBackgroundColor(color);
    }
    
    public void setBounds(Rectangle rectangle) {
        super.setBounds(rectangle);
        Rectangle bounds = rectangle.getCopy();
        this.rectangle.setBounds(bounds.translate(6, 6).resize(-12, -12));
    }
    
    public void setSelected(boolean b) {
        super.setSelected(b);
        rectangle.setLineWidth(b ? 3 : 1);
        repaint();
    }

}
