package org.jbpm.gd.jpdl.taskform;

public class Button {
  
  public static final Button BUTTON_SAVE = new Button("<tf:saveButton value=\"Save\"/>");  
  public static final Button BUTTON_CANCEL = new Button("<tf:cancelButton value=\"Cancel\"/>");

  public static Button createTransitionButton(String transitionName) {
	  return createTransitionButton(transitionName, getSaveAndClose(transitionName));
  }

  public static Button createTransitionButton(String transitionName, String transitionLabel) {
	    return new Button("<tf:transitionButton" + getTo(transitionName) + " value=\"" + transitionLabel + "\"/>");
  }

  public static String getTo(String transitionName) {
	  return transitionName != null ? " transition=\"" + transitionName + "\"" : ""; 
  }
  
  public static String getSaveAndClose(String transitionName) {
	  return transitionName != null ? transitionName : "Save and Close";
  }
  
  String xhtml;
  
  public Button(String xhtml) {
    this.xhtml = xhtml; 
  }
  
  public String getXhtml() {
    return xhtml;
  }
  
  public void setXhtml(String xhtml) {
    this.xhtml = xhtml;
  }
}
