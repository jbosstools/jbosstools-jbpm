package org.jbpm.gd.jpdl.taskform;

public class Field {
  
  String label;
  String variableName;
  FieldType fieldType;
  boolean isReadOnly;
  
  public FieldType getFieldType() {
    return fieldType;
  }
  
  public void setFieldType(FieldType fieldType) {
    this.fieldType = fieldType;
  }
  
  public boolean isReadOnly() {
    return isReadOnly;
  }
  
  public void setReadOnly(boolean isReadOnly) {
    this.isReadOnly = isReadOnly;
  }
  
  public String getLabel() {
    return label;
  }
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public String getVariableName() {
    return variableName;
  }
  
  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

}