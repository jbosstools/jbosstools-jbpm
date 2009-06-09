package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.SemanticElement;

public interface ExceptionHandlerContainer extends SemanticElement {

	void addExceptionHandler(ExceptionHandler exceptionHandler);
	void removeExceptionHandler(ExceptionHandler exceptionHandler);
	ExceptionHandler[] getExceptionHandlers();
	
}
