package org.jbpm.gd.jpdl.taskform;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class GenerationTest extends TestCase {
  
  public void testOne() {
    List fields = new ArrayList();
    
    Field field = new Field();
    field.setFieldType(FieldType.getFieldTypes()[0]);
    field.setLabel("Due date");
    field.setVariableName("dueDate");
    fields.add(field);

    field = new Field();
    field.setFieldType(FieldType.getFieldTypes()[0]);
    field.setLabel("Percentage");
    field.setVariableName("percentage");
    fields.add(field);
    
    List buttons = new ArrayList();
    buttons.add(Button.BUTTON_SAVE);
    buttons.add(Button.BUTTON_CANCEL);
    buttons.add(Button.createTransitionButton("left"));
    buttons.add(Button.createTransitionButton("right"));

    System.out.println(FormGenerator.getForm(fields, buttons));
  }

}
