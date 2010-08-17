package org.jbpm.gd.jpdl.refactoring;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.IResourceChangeDescriptionFactory;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.MoveProcessor;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;
import org.eclipse.ltk.core.refactoring.participants.ResourceChangeChecker;
import org.eclipse.ltk.core.refactoring.participants.SharableParticipants;
import org.eclipse.ltk.core.refactoring.resource.MoveResourceChange;

public class MoveProcessProcessor extends MoveProcessor {

	private final IResource[] resourcesToMove;
	private IContainer destination;

	public MoveProcessProcessor(IResource resource) {
		IResource jpdlResource = null, gpdResource = null;
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
			resourcesToMove = new IResource[] { jpdlResource, gpdResource }; 			
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

	public void setDestination(IContainer destination) {
		Assert.isNotNull(destination);
		this.destination= destination;
	}

	public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException {
		IStatus result = null;
		if (!resourcesToMove[0].isSynchronized(IResource.DEPTH_INFINITE)) {
			result = new Status (
					IStatus.ERROR,
					ResourcesPlugin.PI_RESOURCES,
					IResourceStatus.OUT_OF_SYNC_LOCAL,
					"Resource " + resourcesToMove[0].getName() + "is out of sync with file system",
					null);
		} 
		if (!resourcesToMove[1].isSynchronized(IResource.DEPTH_INFINITE)) {
			IStatus temp = new Status (
						IStatus.ERROR,
						ResourcesPlugin.PI_RESOURCES,
						IResourceStatus.OUT_OF_SYNC_LOCAL,
						"Resource " + resourcesToMove[1].getName() + "is out of sync with file system",
						null);
			if (result == null) {
				result = temp;
			} else {
				MultiStatus multi = new MultiStatus(
							ResourcesPlugin.PI_RESOURCES,
							IResourceStatus.OUT_OF_SYNC_LOCAL,
							"both resources are out of sync with file system", 
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
			RefactoringStatus status= validateDestination(destination);
			if (status.hasFatalError()) {
				return status;
			}
			ResourceChangeChecker checker= (ResourceChangeChecker) context.getChecker(ResourceChangeChecker.class);
			IResourceChangeDescriptionFactory deltaFactory= checker.getDeltaFactory();

			for (int i= 0; i < resourcesToMove.length; i++) {
				IResource resource= resourcesToMove[i];
				IResource newResource= destination.findMember(resource.getName());
				if (newResource != null) {
					status.addWarning("'" + resourcesToMove[i].getFullPath().toString() + "' already exist. It will be replaced.");
					deltaFactory.delete(newResource);
				}
				deltaFactory.move(resourcesToMove[i], destination.getFullPath());
			}
			return status;
		} finally {
			pm.done();
		}
	}

	public RefactoringStatus validateDestination(IContainer destination) {
		Assert.isNotNull(destination, "container is null"); 
		if (destination instanceof IWorkspaceRoot)
			return RefactoringStatus.createFatalErrorStatus("Invalid parent");

		if (!destination.exists()) {
			return RefactoringStatus.createFatalErrorStatus("Destination does not exist");
		}
		return new RefactoringStatus();
	}

	public Change createChange(IProgressMonitor pm) throws CoreException {
		pm.beginTask("", resourcesToMove.length); 
		try {
			CompositeChange compositeChange= new CompositeChange("process move");
			compositeChange.markAsSynthetic();
			for (int i= 0; i < resourcesToMove.length; i++) {
				MoveResourceChange moveChange= new MoveResourceChange(resourcesToMove[i], destination);
				compositeChange.add(moveChange);
			}
			return compositeChange;
		} finally {
			pm.done();
		}
	}

	public IResource[] getResourcesToMove() {
		return resourcesToMove;
	}
	
	public String getProcessToMove() {
		return getProcessName(resourcesToMove[0]);
	}
	
	public Object[] getElements() {
		return resourcesToMove;
	}

	public String getIdentifier() {
		return "org.jbpm.gd.jpdl.refactoring.moveProcessProcessor"; 
	}

	public String getProcessorName() {
		return "Move Process";
	}

	public boolean isApplicable() {
		for (int i= 0; i < resourcesToMove.length; i++) {
			if (!canMove(resourcesToMove[i])) {
				return false;
			}
		}
		return true;
	}

	private static boolean canMove(IResource res) {
		return (res instanceof IFile || res instanceof IFolder) && res.exists() && !res.isPhantom();
	}

	public RefactoringParticipant[] loadParticipants(RefactoringStatus status, SharableParticipants shared) throws CoreException {
		return new RefactoringParticipant[0];
	}

}