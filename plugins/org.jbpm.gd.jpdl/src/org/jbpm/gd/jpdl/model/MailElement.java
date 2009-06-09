package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.NamedElement;

public interface MailElement extends NamedElement {

	void setTemplate(String newTemplate);
	String getTemplate();
	void setActors(String newActors);
	String getActors();
	void setTo(String newTo);
	String getTo();
	void setSubject(Subject newSubject);
	Subject getSubject();
	void setText(Text newText);
	Text getText();

}
