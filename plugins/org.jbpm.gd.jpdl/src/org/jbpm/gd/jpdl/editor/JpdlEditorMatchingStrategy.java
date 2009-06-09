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

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.ResourceUtil;

public class JpdlEditorMatchingStrategy implements IEditorMatchingStrategy {

	public boolean matches(IEditorReference editorRef, IEditorInput input) {
        IFile inputFile = ResourceUtil.getFile(input);
        if (inputFile == null || !(input instanceof IFileEditorInput)) return false; 
    	String inputFilePath = inputFile.getFullPath().removeLastSegments(1).toString();
        String inputFileName = inputFile.getFullPath().lastSegment();
        try {
            IFile editorFile = ResourceUtil.getFile(editorRef.getEditorInput());
            if (editorFile == null) return false;
            String editorFilePath = editorFile.getFullPath().removeLastSegments(1).toString();
            if (!editorFilePath.equals(inputFilePath)) return false;
            if (inputFileName.equals("gpd.xml") || inputFileName.equals("processdefinition.xml")) return true; 
            String editorFileName = editorFile.getFullPath().lastSegment();
            return matches(editorFileName, inputFileName);
        } catch (PartInitException e) {
            return false;
        }
    }

	private boolean matches(String left, String right) {
		if (right.indexOf(".gpd.") == 0) {
			return left.equals(right.substring(5));
		} else {
			return right.equals(left);
		}
	}



}
