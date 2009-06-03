package org.jboss.tools.jbpm.java;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.jbpm.Logger;

public class JavaUtil {

	private static IEditorPart getActiveEditor() {
		return (IEditorPart)PlatformUI
			.getWorkbench()
			.getActiveWorkbenchWindow()
			.getActivePage()
			.getActiveEditor();
	}
	
	private static IFile getCurrentFile() {
		return ((IFileEditorInput)getActiveEditor().getEditorInput()).getFile();
	}
	
	private static IType getClassFor(String className) throws JavaModelException {
		if (className == null || getCurrentProject() == null) {
			return null;
		} else {
			return getCurrentProject().findType(className);
		}
	}
	
	private static IMethod getMethodFor(String className, String methodName) throws JavaModelException {
		IType type = getClassFor(className);
		if (type != null) {
			IMethod[] methods = type.getMethods();
			for (IMethod method : methods) {
				if (method.getElementName().equals(methodName)) return method;
			}
		}
		return null;
	}
	
	public static IJavaProject getCurrentProject() {
		IJavaProject result = null;
		IProject project = getCurrentFile().getProject();
		try {
			result = project.hasNature("org.eclipse.jdt.core.javanature") ? JavaCore.create(project) : null;
		} catch (CoreException e) {
			Logger.logError("Problem while getting current project.", e);
		}
		return result;
	}
	
	public static String[] getFields(String className) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			IType type = getClassFor(className);
			if (type != null ) {
			    IField[] fields = type.getFields();
				for (IField field : fields) {
					list.add(field.getElementName());
				}
			}
		} catch (JavaModelException e) {
			Logger.logError("Error while retrieving fields for " + className, e);
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String[] getArguments(String methodName, String className) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			IMethod method = getMethodFor(className, methodName);
			if (method != null) {
				return method.getParameterNames();
			}
		} catch (JavaModelException e) {
			Logger.logError("Error while retrieving arguments for " + className + "." + methodName, e);
		}
		return list.toArray(new String[list.size()]);
	}
}
