package org.jboss.tools.flow.jpdl4.multipage.validator;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.eclipse.wst.validation.ValidatorMessage;
import org.jboss.tools.flow.jpdl4.Activator;
import org.jboss.tools.flow.jpdl4.multipage.Logger;
import org.jboss.tools.jbpm.Constants;
import org.jboss.tools.jbpm.preferences.JbpmInstallation;
import org.jboss.tools.jbpm.preferences.PreferencesManager;

public class Jpdl4Validator extends AbstractValidator {

	public ValidationResult validate(
			IResource resource, 
            int kind,
            ValidationState state,
            IProgressMonitor monitor) {
		clearMarkers((IFile)resource);
		ValidationResult validationResult = super.validate(resource, kind, state, monitor);
		if (!(resource instanceof IFile) || resource == null) return validationResult;
		PreferencesManager manager = PreferencesManager.getInstance();
		if (manager == null) return validationResult;
		String jbpmName = Activator.getDefault().getPluginPreferences().getString(Constants.JBPM_NAME);
		if (jbpmName == null) return validationResult;
		JbpmInstallation jbpmInstallation = manager.getJbpmInstallation(jbpmName);
		if (jbpmInstallation == null) return validationResult;
		String location = jbpmInstallation.location;
		if (location == null) return validationResult;
		File installdir = new File(location);
		if (!installdir.exists() || !installdir.isDirectory()) return validationResult;
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		ArrayList<URL> urlList = getUrls(installdir);
		ClassLoader newClassLoader = new URLClassLoader(urlList.toArray(new URL[urlList.size()]), oldClassLoader);
		Thread.currentThread().setContextClassLoader(newClassLoader);
		List problemList = parse((IFile)resource);
		if (!problemList.isEmpty()) {
			validationResult = addToValidationResult((IFile)resource, problemList, validationResult);
			
		}
		Thread.currentThread().setContextClassLoader(oldClassLoader);
		return validationResult;
	}
	
	private void clearMarkers(IFile file) {
		try {
			IMarker[] markers = file.findMarkers(null, true,
					IResource.DEPTH_ZERO);
			IMarker[] deleteMarkers = new IMarker[markers.length];
			int deleteindex = 0;
			Object owner;
			for (int i = markers.length - 1; i >= 0; i--) {
				IMarker marker = markers[i];
				owner = marker.getAttribute("owner");

				if ("org.jboss.tools.jbpm.jpdl4".equals(owner)) {
					deleteMarkers[deleteindex++] = markers[i];
				}
			}
			if (deleteindex > 0) {
				IMarker[] todelete = new IMarker[deleteindex];
				System.arraycopy(deleteMarkers, 0, todelete, 0, deleteindex);
				file.getWorkspace().deleteMarkers(todelete);
			}
		} catch (CoreException e) {
		}
	}
	
	@SuppressWarnings("unchecked")
	private ValidationResult addToValidationResult(IFile file, List problems, ValidationResult validationResult) {
		if (validationResult == null) {
			validationResult = new ValidationResult();
		}
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Class problemClass = cl.loadClass("org.jbpm.pvm.internal.xml.Problem");
			Method getMsgMethod = problemClass.getMethod("getMsg", new Class[] {});
			Method getLineMethod = problemClass.getMethod("getLine", new Class[] {});
			Method getSeverityMethod = problemClass.getMethod("getSeverity", new Class[] {});
			for (int i = 0; i < problems.size(); i++) {
				Object target = problems.get(i);
				if (target == null || !problemClass.isInstance(target)) continue;
				String msg = (String)getMsgMethod.invoke(target, new Object[] {});
				String severity = (String)getSeverityMethod.invoke(target, new Object[] {});
				Integer line = getLineNumber(getLineMethod, target);
		        String[] attNames = new String[3];
		        Object[] attValues = new Object[3];
		        attNames[0] = "owner";
		        attValues[0] = "org.jboss.tools.jbpm.jpdl4";
		        attNames[1] = IMarker.LINE_NUMBER;
		        attValues[1] = line;
		        attNames[2] = IMarker.SEVERITY;
		        attValues[2] = new Integer("error".equals(severity) ? IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING);
		        ValidatorMessage validatorMessage = ValidatorMessage.create(msg, file);
		        validatorMessage.setType("org.jboss.tools.flow.jpdl4.problem");
		        validatorMessage.setAttributes(attNames, attValues);
		        validationResult.add(validatorMessage);
			}
		} catch (Exception e) {
			Logger.logError("An error occured while creating the problem markers.", e);
		}	
		return validationResult;
	}
	
	Integer getLineNumber(Method method, Object target) throws Exception {
		try {
			return (Integer)method.invoke(target, new Object[] {});
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof NullPointerException) {
				return null;
			} else {
				throw e;
			}
		}
	}

	private ArrayList<URL> getUrls(File installdir) {
		ArrayList<URL> urlList = new ArrayList<URL>();
		File jbpmJar = new File(installdir, "jbpm.jar");
		if (!jbpmJar.exists()) {
			Logger.logInfo("jbpm.jar could not be found in the jBPM 4 runtime.");
		} else {
			try { 
				urlList.add(jbpmJar.toURL());
			} catch (MalformedURLException e) {
				Logger.logError("Error while adding jbpm.jar to classloader.", e);
			}
		}
		File libdir = new File(installdir, "lib");
		if (!libdir.exists() || !libdir.isDirectory()) {
			Logger.logInfo("lib folder could not be found in the jBPM 4 runtime.");
		} else {
			File[] libfiles = libdir.listFiles();
			for (int i = 0; i < libfiles.length; i++) {
				if (libfiles[i].getName().endsWith(".jar")) {
					try {
						urlList.add(libfiles[i].toURL());
					} catch (MalformedURLException e) {
						Logger.logError("Error while adding " + libfiles[i].getName() + " to classloader", e);
					}
				}
			}
		}
		return urlList;
	}
	
	@SuppressWarnings("unchecked")
	private List parse(IFile resource) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try { 
			Class jpdlParserClass = cl.loadClass("org.jbpm.jpdl.internal.xml.JpdlParser");
			Class parseClass = cl.loadClass("org.jbpm.pvm.internal.xml.Parse");
			Object parserObject = jpdlParserClass.newInstance();
			Method createParseMethod = jpdlParserClass.getMethod("createParse", new Class[] {});
			Object parse = createParseMethod.invoke(parserObject, new Object[] {});
			Method setInputStreamMethod = parseClass.getMethod("setInputStream", new Class[] { InputStream.class });
			setInputStreamMethod.invoke(parse, new Object[] { resource.getContents() });
			Method executeMethod = parseClass.getMethod("execute", new Class[] {});
			executeMethod.invoke(parse, new Object[] {});
			Method getProblemsMethod = parseClass.getMethod("getProblems", new Class[] {});
			return  (List)getProblemsMethod.invoke(parse, new Object[] {});
		} catch (Exception e) {
			Logger.logError("An error occurred while attempting to parse the file " + resource.getName() +".", e);
			return Collections.EMPTY_LIST;
		}
	}
	
}
