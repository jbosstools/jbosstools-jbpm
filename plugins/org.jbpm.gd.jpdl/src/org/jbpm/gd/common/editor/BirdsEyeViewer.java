package org.jbpm.gd.common.editor;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class BirdsEyeViewer extends ViewPart {
	
	private ISelectionListener pageSelectionListener;
	
	private Label label;
	private Canvas canvas;
	private LightweightSystem lws;
	private Thumbnail thumbnail;
	private IWorkbenchPart selectedPart;

	public BirdsEyeViewer() {
	}
	
	private void hookPageSelectionListener() {
		pageSelectionListener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				pageSelectionChanged();				
			}			
		};
		getSite().getPage().addPostSelectionListener(pageSelectionListener);
	}
	
	private void pageSelectionChanged() {
		selectedPart = getSite().getPage().getActiveEditor();
		if (selectedPart != null && selectedPart instanceof Editor) {
			canvas.setVisible(true);
			createOverview();
			label.setVisible(false);
		} else {
			canvas.setVisible(false);
			label.setVisible(true);
		}
	}

	public void createPartControl(Composite parent) {
		setPartName("Overview");
		parent.setLayout(new FormLayout());
		label = new Label(parent, SWT.NORMAL);
		label.setText("No view available");
		label.setLayoutData(getLayoutData());
		canvas = new Canvas(parent, SWT.NONE);
		canvas.setLayoutData(getLayoutData());
		lws = new LightweightSystem(canvas);
		hookPageSelectionListener();
		pageSelectionChanged();
	}
	
	private FormData getLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}

	public void setFocus() {
	}
	
	private void createOverview() {
		if (thumbnail != null) {
			thumbnail.deactivate();
		}
		ScalableFreeformRootEditPart rootEditPart = getModelViewerRootEditPart();
		thumbnail = new ScrollableThumbnail((Viewport)rootEditPart.getFigure());
		thumbnail.setBorder(new MarginBorder(3));
		thumbnail.setSource(rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS));
		lws.setContents(thumbnail);
	}
	
	private ScalableFreeformRootEditPart getModelViewerRootEditPart() {
		return (ScalableFreeformRootEditPart)getModelViewer().getRootEditPart();
	}
	
	private GraphicalViewer getModelViewer() {
		return ((Editor)selectedPart).getGraphicalViewer();
	}
	
	public void dispose() {
		if (pageSelectionListener != null) {
			getSite().getPage().removePostSelectionListener(pageSelectionListener);
		}
		if (null != thumbnail) {
			thumbnail.deactivate();
		}
		super.dispose();
	}

}
