package org.jboss.tools.flow.jpdl4.multipage.validator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.jboss.tools.flow.jpdl4.Activator;
import org.jboss.tools.jbpm.Constants;
import org.jboss.tools.jbpm.preferences.JbpmInstallation;
import org.jboss.tools.jbpm.preferences.PreferencesManager;

public class Jpdl4Validator extends AbstractValidator {

	public ValidationResult validate(
			IResource resource, 
            int kind,
            ValidationState state,
            IProgressMonitor monitor) {
		System.out.println("validating...");
		PreferencesManager manager = PreferencesManager.getPreferencesManager(Activator.getDefault());
		if (manager == null) return super.validate(resource, kind, state, monitor);
		String jbpmName = Activator.getDefault().getPluginPreferences().getString(Constants.JBPM_NAME);
		if (jbpmName == null) return super.validate(resource, kind, state, monitor);
		JbpmInstallation jbpmInstallation = manager.getJbpmInstallation(jbpmName);
		if (jbpmInstallation == null) return super.validate(resource, kind, state, monitor);
		String location = jbpmInstallation.location;
		if (location == null) return super.validate(resource, kind, state, monitor);
		System.out.println("location : " + location);
//		Thread.currentThread().getContextClassLoader().
		return super.validate(resource, kind, state, monitor);
	}

}
