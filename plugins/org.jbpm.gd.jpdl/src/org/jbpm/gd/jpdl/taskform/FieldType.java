package org.jbpm.gd.jpdl.taskform;

public abstract class FieldType {
  
  public static FieldType[] getFieldTypes() {
    return fieldTypes;
  }
  
  public static FieldType[] fieldTypes = new FieldType[]{
    
    new FieldType(){
      public String render(Field field) {
        return "<h:inputText "+getValueAttribute(field)+getReadOnlyAttribute(field)+" />";
      }
      public String getName() {
        return "Text";
      }
    }
    
  };
  
  public String getValueAttribute(Field field) {
    return "value=\"#{var['"+field.getVariableName()+"']}\"";
  }

  public String getReadOnlyAttribute(Field field) {
    return (field.isReadOnly() ? " readonly=\"true\" " : "");
  }

  public String toString() {
    return getName();
  }
  public abstract String getName();
  public abstract String render(Field field);
}
