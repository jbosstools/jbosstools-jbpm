package org.jbpm.gd.jpdl.deployment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;

public class ClassesAndResourcesContentProvider extends
		StandardJavaElementContentProvider {

	protected Object[] getPackageFragmentRoots(IJavaProject project) throws JavaModelException {		
		Object[] roots = super.getPackageFragmentRoots(project);		
		List<Object> list = new ArrayList<Object>();	
		for (int i = 0; i < roots.length; i++) {
			if (roots[i] instanceof IPackageFragmentRoot) {
				IPackageFragmentRoot root = (IPackageFragmentRoot)roots[i];
				if (root.hasChildren()) {
					list.add(root);
				}
			}
		}		
		return list.toArray();
	}

	protected Object[] getPackageFragmentRootContent(IPackageFragmentRoot root) throws JavaModelException {		
		Object[] fragments = super.getPackageFragmentRootContent(root);		
		List<Object> list = new ArrayList<Object>();		
		for (int i = 0; i < fragments.length; i++) {
			if (fragments[i] instanceof IPackageFragment) {
				IPackageFragment fragment = (IPackageFragment)fragments[i];
				if (fragment.hasChildren()) {
					list.add(fragment);
				}
			} else {
				list.add(fragments[i]);
			}
		}		
		return list.toArray();		
	}
	
}
