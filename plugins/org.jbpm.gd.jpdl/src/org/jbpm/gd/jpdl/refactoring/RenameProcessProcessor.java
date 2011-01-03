package org.jbpm.gd.jpdl.refactoring;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.IResourceChangeDescriptionFactory;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;
import org.eclipse.ltk.core.refactoring.participants.RenameProcessor;
import org.eclipse.ltk.core.refactoring.participants.ResourceChangeChecker;
import org.eclipse.ltk.core.refactoring.participants.SharableParticipants;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceChange;

public class RenameProcessProcessor extends RenameProcessor {

	private IResource jpdlResource;
	private IResource gpdResource;
	private IResource jpgResource;
	private String newProcessName;
	private boolean updateReferences;

	public RenameProcessProcessor(IResource resource) {
		if (resource == null || !resource.exists()) {
			throw new IllegalArgumentException("resource must not be null and must exist"); 
		}
		
		if (isJpdlFile(resource)) {
			jpdlResource = resource;
			gpdResource = getGpdResource(resource);
		} else if (isGpdFile(resource)) {
			jpdlResource = getJpdlResource(resource);
			gpdResource = resource;
		}
		
		if (jpdlResource != null && jpdlResource.exists() && gpdResource != null && gpdResource.exists()) {

			jpgResource = getJpgResource(jpdlResource);
			
			updateReferences= true;
			
			String newName = jpdlResource.getName();
			int i = newName.indexOf(".jpdl.xml");
			if (i != -1) {
				newName = newName.substring(0, i);
			}
			setNewProcessName(newName); 
			
		} else {
			throw new IllegalArgumentException("both jpdlFile and gpdFile must not be null and must exist");
		}
	}
	
	private String getProcessName(IResource resource) {
		String name = resource.getName();
		if (name.startsWith(".") && name.endsWith(".gpd.xml")) {
			return name.substring(1, name.indexOf(".gpd.xml"));
		} else if (name.endsWith(".jpdl.xml")) {
			return name.substring(0, name.indexOf(".jpdl.xml"));
		} else {
			throw new IllegalArgumentException("resource must be either a jpdlFile or gpdFile");
		}
	}
	
	private boolean isJpdlFile(IResource resource) {
		return resource.getName().endsWith(".jpdl.xml");
	}
	
	private boolean isGpdFile(IResource resource) {
		String name = resource.getName();
		return name.startsWith(".") && name.endsWith(".gpd.xml");
	}
	
	private IResource getGpdResource(IResource resource) {
		if (!resource.getName().endsWith(".jpdl.xml")) {
			throw new IllegalArgumentException("gpd resource can only be obtained for a jpdl file");
		}
		return resource.getParent().getFile(new Path("." + getProcessName(resource) + ".gpd.xml"));
	}
	
	private IResource getJpdlResource(IResource resource) {
		if (!resource.getName().endsWith(".gpd.xml")) {
			throw new IllegalArgumentException("jpdl resource can only be obtained for a gpd file");
		}
		return resource.getParent().getFile(new Path(getProcessName(resource) + ".jpdl.xml"));
	}

	private IResource getJpgResource(IResource resource) {
		return resource.getParent().getFile(new Path(getProcessName(resource) + ".jpg"));
	}
	
	public String getNewProcessName() {
		return newProcessName;
	}

	public void setNewProcessName(String newName) {
		Assert.isNotNull(newName);
		newProcessName= newName;
	}

	public boolean isUpdateReferences() {
		return updateReferences;
	}

	public void setUpdateReferences(boolean updateReferences) {
		this.updateReferences= updateReferences;
	}

	public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException {
		IStatus result = null;
		if (!jpdlResource.isSynchronized(IResource.DEPTH_INFINITE)) {
			result = new Status (
					IStatus.ERROR,
					ResourcesPlugin.PI_RESOURCES,
					IResourceStatus.OUT_OF_SYNC_LOCAL,
					"Resource " + jpdlResource.getName() + "is out of sync with file system",
					null);
		} 
		if (!gpdResource.isSynchronized(IResource.DEPTH_INFINITE)) {
			IStatus temp = new Status (
						IStatus.ERROR,
						ResourcesPlugin.PI_RESOURCES,
						IResourceStatus.OUT_OF_SYNC_LOCAL,
						"Resource " + jpdlResource.getName() + "is out of sync with file system",
						null);
			if (result == null) {
				result = temp;
			} else {
				MultiStatus multi = new MultiStatus(
							ResourcesPlugin.PI_RESOURCES,
							IResourceStatus.OUT_OF_SYNC_LOCAL,
							"the resources are out of sync with file system", 
							null);
				multi.add(result);
				multi.add(temp);
				result = multi;
			}
		}
		if (!jpgResource.isSynchronized(IResource.DEPTH_INFINITE)) {
			IStatus temp = new Status (
						IStatus.ERROR,
						ResourcesPlugin.PI_RESOURCES,
						IResourceStatus.OUT_OF_SYNC_LOCAL,
						"Resource " + jpgResource.getName() + "is out of sync with file system",
						null);
			if (result == null) {
				result = temp;
			} else if (result instanceof MultiStatus){
				((MultiStatus)result).add(temp);
			} else {
				MultiStatus multi = new MultiStatus(
							ResourcesPlugin.PI_RESOURCES,
							IResourceStatus.OUT_OF_SYNC_LOCAL,
							"the resources are out of sync with file system", 
							null);
				multi.add(result);
				multi.add(temp);
				result = multi;
			}
		}
		if (result == null) {
			result = Status.OK_STATUS;
		}
		return RefactoringStatus.create(result);
	}

	public RefactoringStatus checkFinalConditions(IProgressMonitor pm, CheckConditionsContext context) throws CoreException {
		pm.beginTask("", 1);
		try {
			ResourceChangeChecker checker= (ResourceChangeChecker) context.getChecker(ResourceChangeChecker.class);
			IResourceChangeDescriptionFactory deltaFactory= checker.getDeltaFactory();
			IPath newJpdlPath= jpdlResource.getFullPath().removeLastSegments(1).append(getNewProcessName() + ".jpdl.xml");
			deltaFactory.move(
					jpdlResource, newJpdlPath);
			IPath newGpdPath= gpdResource.getFullPath().removeLastSegments(1).append("." + getNewProcessName() + ".gpd.xml");
			deltaFactory.move(
					gpdResource, newGpdPath);
			if (jpgResource != null && jpgResource.exists()) {
				IPath newJpgPath= jpgResource.getFullPath().removeLastSegments(1).append(getNewProcessName() + ".jpg");
				deltaFactory.move(
						jpgResource, newJpgPath);
			}
			return new RefactoringStatus();
		} finally {
			pm.done();
		}
	}

	public RefactoringStatus validateNewElementName(String newName) {
		Assert.isNotNull(newName, "new name");
		IContainer c= jpdlResource.getParent();
		if (c == null)
			return RefactoringStatus.createFatalErrorStatus("Internal Error");
		if ("".equals(newName)) {
			return RefactoringStatus.createFatalErrorStatus("The name of the process cannot be the empty string");
		}
		if (c.findMember(newName + ".jpdl.xml") != null || c.findMember("." + newName + ".gpd.xml") != null)
			return RefactoringStatus.createFatalErrorStatus("A process with this name already exists");

		if (!c.getFullPath().isValidSegment(newName + ".jpdl.xml") || !c.getFullPath().isValidSegment("." + newName + ".gpd.xml"))
			return RefactoringStatus.createFatalErrorStatus("This is an invalid name for a process");

		RefactoringStatus result= RefactoringStatus.create(c.getWorkspace().validateName(newName, jpdlResource.getType()));
		if (!result.hasFatalError()) {
			result.merge(RefactoringStatus.create(c.getWorkspace().validateName(newName, gpdResource.getType())));
		}
		if (!result.hasFatalError())
			result.merge(RefactoringStatus.create(c.getWorkspace().validatePath(createNewPath(jpdlResource, newName + ".jpdl.xml"), jpdlResource.getType())));
		if (!result.hasFatalError())
			result.merge(RefactoringStatus.create(c.getWorkspace().validatePath(createNewPath(gpdResource, "." + newName + ".gpd.xml"), gpdResource.getType())));
		if (!result.hasFatalError() && jpgResource != null && jpgResource.exists())
			result.merge(RefactoringStatus.create(c.getWorkspace().validatePath(createNewPath(jpgResource, newName + ".jpg"), jpgResource.getType())));
		return result;
	}

	public Change createChange(IProgressMonitor pm) throws CoreException {
		pm.beginTask("", 1); 
		try {
			CompositeChange compositeChange = new CompositeChange("process rename");
			compositeChange.add(new RenameResourceChange(jpdlResource.getFullPath(), getNewProcessName() + ".jpdl.xml"));
			compositeChange.add(new RenameResourceChange(gpdResource.getFullPath(), "." + getNewProcessName() + ".gpd.xml"));
			if (jpgResource != null && jpgResource.exists()) {
				compositeChange.add(new RenameResourceChange(jpgResource.getFullPath(), getNewProcessName() + ".jpg"));
			}
			return compositeChange;
		} finally {
			pm.done();
		}
	}

	private String createNewPath(IResource resource, String newName) {
		return resource.getFullPath().removeLastSegments(1).append(newName).toString();
	}

	public Object[] getElements() {
		if (jpgResource != null && jpgResource.exists()) {
			return new Object[] { jpdlResource, gpdResource, jpgResource };
		} else {
			return new Object[] { jpdlResource, gpdResource};
		}
	}

	public String getIdentifier() {
		return "org.jbpm.gd.jpdl.refactoring.renameProcessProcessor"; 
	}

	public String getProcessorName() {
		return "Rename Process";
	}

	public boolean isApplicable() {
		if (jpdlResource == null)
			return false;
		if (!jpdlResource.exists())
			return false;
		if (!jpdlResource.isAccessible())
			return false;
		if (gpdResource == null)
			return false;
		if (!gpdResource.exists())
			return false;
		if (!gpdResource.isAccessible())
			return false;
		return true;
	}

	public RefactoringParticipant[] loadParticipants(RefactoringStatus status, SharableParticipants shared) throws CoreException {
		return new RefactoringParticipant[0];
	}

}