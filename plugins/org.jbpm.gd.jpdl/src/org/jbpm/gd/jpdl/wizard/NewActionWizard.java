package org.jbpm.gd.jpdl.wizard;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jbpm.gd.jpdl.Logger;

public class NewActionWizard extends NewClassWizard implements INewWizard {
	static String ACTION_HANDLER_CLASS = "org.jbpm.graph.def.ActionHandler";
	boolean openCreatedType = false;

	public NewActionWizard() {
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New jBPM Action");
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWCLASS);
	}

	public void addPages() {
		super.addPages();
		mainPage.setTitle("jBPM Action");
	}

	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		mainPage.createType(monitor);
		if(mainPage.getCreatedType() != null) {
			modifyJavaSource();
		}
	}

	@Override
	public boolean performFinish() {
		boolean b = super.performFinish();
		if(b) {
			if(openCreatedType) {
				Display.getDefault().asyncExec(new Runnable(){
					public void run() {
						try {
							JavaUI.openInEditor(mainPage.getCreatedType());
						} catch (CoreException e) {
							Logger.logError(e);
						}
					}
				});
			}
		}
		return b;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		IProject p = getProject(selection);
		adapter = new NewTypeWizardAdapter(p);
		adapter.setRawSuperClassName(ACTION_HANDLER_CLASS);
		IPackageFragment f = getPackageFragment(selection);
		if(f != null) {
			String name = "";
			IPackageFragment cf = f;
			while(cf != null) {
				if(name.length() == 0) {
					name = cf.getElementName();
				} else {
					name = cf.getElementName() + "." + name;
				}
				cf = (cf.getParent() instanceof IPackageFragment) ? (IPackageFragment)cf.getParent() : null;
			}
			adapter.setRawPackageName(name);
		}
		adapter.setRawClassName("");
		openCreatedType = true;
	}

	IProject getProject(IStructuredSelection selection) {
		if(selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		Object o = ((IStructuredSelection)selection).getFirstElement();
		if(o instanceof IProject) {
			return (IProject)o;
		} else if(o instanceof IJavaElement) {
			IJavaElement e = (IJavaElement)o;
			return e.getJavaProject().getProject();
		} else if(o instanceof IAdaptable) {
			IResource r = (IResource)((IAdaptable)o).getAdapter(IResource.class);
			return r != null ? r.getProject() : null;
		}
		return null;
	}

	IPackageFragment getPackageFragment(IStructuredSelection selection) {
		if(selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		Object o = ((IStructuredSelection)selection).getFirstElement();
		if(o instanceof IPackageFragment) {
			return (IPackageFragment)o;
		} else if(o instanceof IFolder) {
			IFolder f = (IFolder)o;
			IJavaElement jp = JavaCore.create(f);
			if(jp instanceof IPackageFragment) {
				return (IPackageFragment)jp;
			}
		}
		return null;
	}

	void modifyJavaSource() {
//		String newValue = getQualifiedClassName();
		try {
			IType type = mainPage.getCreatedType();
			if(type == null) {
				return;
			}
			String name = type.getElementName();
			String sc = type.getSuperclassTypeSignature();

			ICompilationUnit w = type.getCompilationUnit().getWorkingCopy(new NullProgressMonitor());
			IBuffer b = w.getBuffer();
			String s = b.getContents();
			String lineDelimiter = "\r\n";
				
			String IMPORT = "import " + ACTION_HANDLER_CLASS + ";";
			int i1 = s.indexOf(IMPORT);
			if(i1 >= 0) {
				if(i1 >= 0) {
					String content = "";
					String[] imports = {
							"import org.jbpm.graph.exe.ExecutionContext;",
					};
					for (String is: imports) {
						if(s.indexOf(is) < 0) {
							content += lineDelimiter + is;
						}
					}
					if(content.length() > 0) {
						b.replace(i1 + IMPORT.length(), 0, content);
					}
				}
				
				s = b.getContents();
				
				int i = s.indexOf('{');
				int j = s.lastIndexOf('}');
				
				if(i > 0 && j > i) {
					String tab = "\t";
					String content = lineDelimiter 
						+ lineDelimiter
						+ tab + "private static final long serialVersionUID = 1L;" + lineDelimiter
						+ lineDelimiter
						+ tab + "/**" + lineDelimiter
						+ tab + "* The message member gets its value from the configuration in the" + lineDelimiter
						+ tab + "* processdefinition. The value is injected directly by the engine." + lineDelimiter
						+ tab + "*/" + lineDelimiter
						+ tab + "String message;" + lineDelimiter
						+ lineDelimiter
						+ tab + "/**" + lineDelimiter
						+ tab + "* A message process variable is assigned the value of the message" + lineDelimiter
						+ tab + "* member. The process variable is created if it doesn't exist yet." + lineDelimiter
						+ tab + "*/" + lineDelimiter
						+ tab + "public void execute(ExecutionContext context) throws Exception {" + lineDelimiter
						+ tab + tab + "//ADD CUSTOM ACTION CODE HERE" + lineDelimiter
						+ tab + tab + "//context.getContextInstance().setVariable(\"message\", message);" + lineDelimiter
						+ tab + "}" + lineDelimiter
						+ lineDelimiter;
					b.replace(i + 1, j - i - 1, content);
					w.commitWorkingCopy(true, new NullProgressMonitor());
				}
			}
		} catch (CoreException e) {
			Logger.logError(e);
		}
	}

}
