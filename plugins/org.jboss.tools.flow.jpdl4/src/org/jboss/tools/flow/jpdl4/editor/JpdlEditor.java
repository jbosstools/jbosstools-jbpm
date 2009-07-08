package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.jboss.tools.flow.common.editor.GenericModelEditor;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.editpart.JpdlGraphicalEditPartFactory;
import org.jboss.tools.flow.jpdl4.io.JpdlDeserializer;
import org.jboss.tools.flow.jpdl4.io.JpdlSerializer;
import org.jboss.tools.flow.jpdl4.properties.JpdlPropertySheetPage;
import org.jboss.tools.flow.jpdl4.view.DetailsPage;
import org.jboss.tools.flow.jpdl4.view.IDetailsPage;

public class JpdlEditor extends GenericModelEditor implements
		ITabbedPropertySheetPageContributor {

	public static String ID = "org.jboss.tools.flow.jpdl4.editor";

	private DetailsPage detailsPage;
	private JpdlPropertySheetPage propertySheetPage;

	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		writeImage();
	}

	public void doSaveAs() {
		super.doSaveAs();
		writeImage();
	}

	protected PaletteRoot createPalette() {
		return new JpdlPaletteFactory().createPalette();
	}

	protected org.eclipse.gef.EditPartFactory createEditPartFactory() {
		return new JpdlGraphicalEditPartFactory();
	}

	protected Object createModel() {
		return ElementRegistry
				.createWrapper("org.jboss.tools.flow.jpdl4.process");
	}

	protected void writeModel(OutputStream os) throws IOException {
		Object object = getModel();
		if (object instanceof Wrapper) {
			JpdlSerializer.serialize((Wrapper) object, os);
		}
	}

	protected void writeImage() {
		// TODO repair doSave method
		SWTGraphics g = null;
		GC gc = null;
		Image image = null;

		LayerManager lm = (LayerManager) getGraphicalViewer()
				.getEditPartRegistry().get(LayerManager.ID);
		IFigure figure = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);

		try {

			Rectangle r = figure.getBounds();
			image = new Image(Display.getDefault(), r.width, r.height);
			gc = new GC(image);
			g = new SWTGraphics(gc);
			g.translate(r.x * -1, r.y * -1);
			figure.paint(g);
			ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] { image.getImageData() };
			imageLoader.save(getImageSavePath(), SWT.IMAGE_JPEG);
			refreshProcessFolder();

		} finally {
			if (g != null) {
				g.dispose();
			}
			if (gc != null) {
				gc.dispose();
			}
			if (image != null) {
				image.dispose();
			}
		}
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		IWorkbenchPage workbenchPage = getActivePage();
		if (workbenchPage != null) {
			if (workbenchPage.findView("org.eclipse.ui.views.PropertySheet") == null) {
				workbenchPage.showView("org.eclipse.ui.views.PropertySheet");
			}
			if (workbenchPage.findView("org.jboss.tools.flow.jpdl4.details") == null) {
				workbenchPage.showView("org.jboss.tools.flow.jpdl4.details");
			}
		}
	}

	private IWorkbenchPage getActivePage() {
		IWorkbenchWindow workbenchWindow = getActiveWorkbenchWindow();
		return workbenchWindow == null ? null : workbenchWindow.getActivePage();
	}

	private IWorkbenchWindow getActiveWorkbenchWindow() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		return workbench == null ? null : workbench.getActiveWorkbenchWindow();
	}

	private String getImageSavePath() {
		IFile file = getFile();
		IPath path = file.getRawLocation();
		if ("xml".equals(path.getFileExtension()))
			path = path.removeFileExtension();
		if ("jpdl".equals(path.getFileExtension()))
			path = path.removeFileExtension();
		path = path.addFileExtension("png");
		return path.toOSString();
	}

	private void refreshProcessFolder() {
		try {
			IFile file = ((FileEditorInput) getEditorInput()).getFile();
			file.getParent().refreshLocal(1, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	protected void createModel(InputStream is) {
		boolean empty = true;
		try {
			empty = is.available() == 0;
		} catch (IOException e) {
			// ignored
		}
		setModel(empty ? createModel() : JpdlDeserializer.deserialize(is));
	}

	public String getContributorId() {
		return getSite().getId();
	}

	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	protected DetailsPage getDetailsPage() {
		if (detailsPage == null) {
			initDetailsPage();
		}
		return detailsPage;
	}

	protected void initDetailsPage() {
		detailsPage = new DetailsPage(this);
		getSite().getSelectionProvider().addSelectionChangedListener(
				detailsPage);
	}

	private JpdlPropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null) {
			propertySheetPage = new JpdlPropertySheetPage(this,
					getCommandStack());
		}
		return propertySheetPage;
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class)
			return getPropertySheetPage();
		else if (adapter == IDetailsPage.class)
			return getDetailsPage();
		return super.getAdapter(adapter);
	}

}
