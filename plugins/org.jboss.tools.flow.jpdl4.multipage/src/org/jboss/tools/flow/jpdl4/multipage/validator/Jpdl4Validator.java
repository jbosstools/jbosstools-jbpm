package org.jboss.tools.flow.jpdl4.multipage.validator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;

public class Jpdl4Validator extends AbstractValidator {

	public ValidationResult validate(
			IResource resource, 
            int kind,
            ValidationState state,
            IProgressMonitor monitor) {
		System.out.println("validating...");
		return super.validate(resource, kind, state, monitor);
	}

}
