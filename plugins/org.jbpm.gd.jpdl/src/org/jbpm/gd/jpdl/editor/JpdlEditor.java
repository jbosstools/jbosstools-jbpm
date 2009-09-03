/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.gd.jpdl.editor;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.jbpm.gd.common.editor.ContentProvider;
import org.jbpm.gd.common.editor.Editor;
import org.jbpm.gd.common.editor.GraphicalViewer;
import org.jbpm.gd.common.editor.OutlineViewer;
import org.jbpm.gd.common.editor.SelectionSynchronizer;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.jpdl.model.ProcessDefinition;
import org.jbpm.gd.jpdl.part.JpdlEditorOutlineEditPartFactory;
import org.jbpm.gd.jpdl.part.JpdlGraphicalEditPartFactory;

public class JpdlEditor extends Editor { 

	private IResourceChangeListener resourceChangeListener;
	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		initResourceChangeListener();
		initPartName();
	}
	
	private void initResourceChangeListener() {
		resourceChangeListener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				handleResourceChange(event);
			}
		};
		getWorkspace().addResourceChangeListener(resourceChangeListener);
	}
	

	private void handleResourceChange(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			IFile file = ((IFileEditorInput)getEditorInput()).getFile();
			if (!file.exists()) {
				deleteProcessFolder(file);
			}
		} 
	}
	
	private void deleteProcessFolder(IFile file) {
		final IContainer processFolder = getWorkspace().getRoot().getFolder(file.getFullPath().removeLastSegments(1));
		if (processFolder != null && processFolder.exists()) {
			WorkspaceJob job = new WorkspaceJob("delete") {
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					processFolder.delete(true, null);
					return Status.OK_STATUS;
				}
			};
			job.setRule(getWorkspace().getRuleFactory().deleteRule(processFolder));
			job.schedule();
		}
	}
	
	private IWorkspace getWorkspace() {
		return ((IFileEditorInput)getEditorInput()).getFile().getWorkspace();
	}
	
	private void initPartName() {
		FileEditorInput fileInput = (FileEditorInput) getEditorInput();
		IPath path = fileInput.getPath().removeLastSegments(1);
		path = path.removeFirstSegments(path.segmentCount() - 1);
		setPartName(path.lastSegment());
	}

	protected SelectionSynchronizer createSelectionSynchronizer() {
		return new JpdlSelectionSynchronizer();
	}

	protected ContentProvider createContentProvider() {
		return new JpdlContentProvider();
	}

	protected GraphicalViewer createGraphicalViewer() {
		return new GraphicalViewer(this) {
			protected void initEditPartFactory() {
				setEditPartFactory(new JpdlGraphicalEditPartFactory());
			}			
		};
	}

	protected OutlineViewer createOutlineViewer() {
		return new OutlineViewer(this) {
			protected void initEditPartFactory() {
				getViewer().setEditPartFactory(new JpdlEditorOutlineEditPartFactory()); 
			}
		};
	}

	protected void createPages() {
		super.createPages();
		addPage(1, new JpdlDeploymentEditorPage(this), "Deployment");
	}

	protected SemanticElement createMainElement() {
		return getSemanticElementFactory().createById("org.jbpm.gd.jpdl.processDefinition");
	}

	public ProcessDefinition getProcessDefinition() {
		return (ProcessDefinition)getRootContainer().getSemanticElement();
	}

	public void dispose() {
		getWorkspace().removeResourceChangeListener(resourceChangeListener);
		super.dispose();
	}

}
