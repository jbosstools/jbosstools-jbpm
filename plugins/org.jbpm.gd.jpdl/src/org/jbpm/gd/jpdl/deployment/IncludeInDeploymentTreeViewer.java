package org.jbpm.gd.jpdl.deployment;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Tree;

public class IncludeInDeploymentTreeViewer extends CheckboxTreeViewer {

	public IncludeInDeploymentTreeViewer(Tree tree) {
		super(tree);
		addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				handleCheckStateChanged(event);				
			}			
		});
	}
	
	private void handleCheckStateChanged(CheckStateChangedEvent event) {
		updateChecks(event.getElement(), event.getChecked());
	}
	
//	private void handleCheckStateChangedForChildren(Object object, boolean checked) {
//		expandToLevel(object, ALL_LEVELS);
//		setGrayed(object, false);
//		ITreeContentProvider provider = (ITreeContentProvider)getContentProvider();
//		Object[] children = provider.getChildren(object);
//		for (int i = 0; i < children.length; i++) {
//			setChecked(children[i], checked);
//			handleCheckStateChangedForChildren(children[i], checked);
//		}
//	}
	
//	private void handleCheckStateChangedForParent(Object object) {
//		ITreeContentProvider provider = (ITreeContentProvider)getContentProvider();
//		Object parent = provider.getParent(object);
//		if (parent != null) {
//			Object[] children = provider.getChildren(parent);
//			int numberChecked = 0;
//			boolean grayedChildren = false;
//			for (int i = 0; i < children.length; i++) {
//				if (getChecked(children[i])) numberChecked++;
//				if (getGrayed(children[i])) grayedChildren = true;
//			}
//			setChecked(parent, numberChecked > 0);
//			setGrayed(parent, (numberChecked > 0 && numberChecked < children.length) || grayedChildren);
//			handleCheckStateChangedForParent(parent);
//		}
//	}
	
	public boolean setChecked(final Object element, final boolean state) {
		boolean result = super.setChecked(element, state);
		if (result) {
			updateChecks(element, state);
		}				
		return result;
	}
	
	public void setCheckedElements(final Object[] elements) {
		super.setCheckedElements(elements);			
		for (int i = 0; i < elements.length; i++) {
			updateChecks(elements[i], true);
		}
	}
	
	private void updateChecks(Object object, boolean state) {
		updateChecksForChildren(object, state);
		updateChecksForParents(object, state);
	}
	
	private void updateChecksForChildren(Object object, boolean state) {
		setGrayed(object, false);
		Object[] children = ((ITreeContentProvider)getContentProvider()).getChildren(object);
		for (int i = 0; i < children.length; i++) {
			if (getChecked(children[i]) != state) {
				super.setChecked(children[i], state);
				updateChecksForChildren(children[i], state);
			}
		}
	}
	
	private void updateChecksForParents(Object object, boolean state) {
		ITreeContentProvider provider = (ITreeContentProvider)getContentProvider();
		Object child = object;
		Object parent = provider.getParent(child);
		boolean change = true;
		while (parent != null && change) {
			Object[] siblings = provider.getChildren(parent);
			int numberChecked = 0;
			boolean grayed = false;
			change = false;
			for (int i = 0; i < siblings.length; i++) {
				if (getChecked(siblings[i])) numberChecked++;
				if (getGrayed(siblings[i])) grayed = true;
			}
			if (numberChecked == 0) {
				if (getChecked(parent) || getGrayed(parent)) change = true;
				setGrayChecked(parent, false);
			}
			else if (numberChecked == siblings.length) {
				if (!getChecked(parent) || getGrayed(parent) != grayed) change = true;
				setGrayed(parent, false);
				setChecked(parent, true);
			}
			else {
				if (!getChecked(parent) || !getGrayed(parent)) change = true;
				setGrayChecked(parent, true);
			}
			child = parent;
			parent = provider.getParent(child);
		}
		
	}
	
//	public void updateChecks() {
//		getControl().getDisplay().asyncExec(new Runnable() {
//			public void run() {
//				ITreeContentProvider provider = (ITreeContentProvider)getContentProvider();
//				Object[] elements = provider.getElements(getInput());
//				for (int i = 0; i < elements.length; i++) {
//					updateChecks(provider, elements[i]);
//				}
//			}			
//		});
//	}
	
	
	
//	private int[] updateChecks(ITreeContentProvider provider, Object object) {
//		int[] result = new int[2]; // result[0] contains the number of checked, result[1] the number of grayed elements
//		int[] updates = new int[2];
//		Object[] children = provider.getChildren(object);
//		if ((children == null || children.length == 0)) {
//			if (getChecked(object)) result[0]++;
//		} else {
//			for (int i = 0; i < children.length; i++) {
//				int[] runner = updateChecks(provider, children[i]);
//				updates[0] += runner[0];
//				updates[1] += runner[1];
//			}
//			if (updates[0] ==  children.length && updates[1] == 0) {
//				setChecked(object, true);
//				result[0]++;
//				setGrayed(object, false);
//			} else if (updates[0] > 0 || updates[1] > 0) {
//				setChecked(object, true);
//				result[0]++;
//				setGrayed(object, true);
//				result[1]++;
//			} else {
//				setChecked(object, false);
//				setGrayed(object, false);
//			}
//		}
//		return result;
//	}

}