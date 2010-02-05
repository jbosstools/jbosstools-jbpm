package org.jbpm.gd.common.editor;

import java.util.ArrayList;
import java.util.EventObject;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.INestableKeyBindingService;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.model.SemanticElementFactory;
import org.jbpm.gd.common.notation.NotationElementFactory;
import org.jbpm.gd.common.notation.RootContainer;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.common.xml.XmlAdapterFactory;
import org.jbpm.gd.jpdl.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class Editor extends XMLMultiPageEditorPart implements
		ITabbedPropertySheetPageContributor {

	private ActionRegistry actionRegistry;

	private EditDomain editDomain;

	private CommandStackListener commandStackListener;

	private boolean isDirty = false;

	private ISelectionListener selectionListener;

	private SelectionSynchronizer selectionSynchronizer;

	private SemanticElementFactory semanticElementFactory;
	
	private NotationElementFactory notationElementFactory;

	private RootContainer rootContainer;
	
	private GraphPage graphPage;

	private StructuredTextEditor sourcePage;
	
	private MenuManager sourceContextMenuManager;
	
	private ContentProvider contentProvider;
	
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		initActionRegistry();
		initEditDomain();
		initCommandStackListener();
		initSelectionListener();
		initSelectionSynchronizer();
	}

	protected void createPages() {
			super.createPages();
			initSourcePage();
			initGraphPage();
			setActivePage(0);
			checkReadOnly();
	}

	protected void addPage(int index, IEditorPart part, String label) {
		try {
			addPage(index, part, getEditorInput());
			setPageText(index, label);
		} catch (PartInitException e) {
			Logger.logError("Could not create editor page", e);
		}
	}

	public String getContributorId() {
		return getSite().getId();
	}

	private void initActionRegistry() {
		actionRegistry = new ActionRegistry(this);
	}

	private void initEditDomain() {
		editDomain = new DefaultEditDomain(this);
	}
	
	private void initSelectionSynchronizer() {
		selectionSynchronizer = createSelectionSynchronizer();
	}

	private void initCommandStackListener() {
		commandStackListener = new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				handleCommandStackChanged();
			}
		};
		getCommandStack().addCommandStackListener(commandStackListener);
	}

	private void initSelectionListener() {
		selectionListener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart part,
					ISelection selection) {
				getActionRegistry().updateEditPartActions();
			}
		};
		ISelectionService selectionService = getSite().getWorkbenchWindow()
				.getSelectionService();
		selectionService.addSelectionListener(selectionListener);
	}
	
	protected void initGraphPage() {
		graphPage = new GraphPage(this);
		addPage(0, graphPage, "Diagram");
		getContentProvider().addNotationInfo(getRootContainer(), getEditorInput());
	}

	protected void initSourcePage() {
		int pageCount = getPageCount();
		for (int i = 0; i < pageCount; i++) {
			if (getEditor(i) instanceof StructuredTextEditor) {
				sourcePage = (StructuredTextEditor) getEditor(i);
				SemanticElement semanticElement = getSemanticElement(sourcePage);
				CreationFactory factory = new CreationFactory(semanticElement.getElementId(), getSemanticElementFactory(), getNotationElementFactory());
				setRootContainer((RootContainer)factory.getNewObject());
				getRootContainer().setSemanticElement(semanticElement);
				semanticElement.addPropertyChangeListener(getRootContainer());
			}
		}
		if (sourcePage != null) {
			initSourcePageContextMenu(sourcePage.getTextViewer()
					.getTextWidget());
		}
	}

	private SemanticElement getSemanticElement(
			StructuredTextEditor sourcePage) {
		Node node = getDocumentElement(sourcePage);
		XmlAdapterFactory factory = new XmlAdapterFactory(node.getOwnerDocument(), getSemanticElementFactory());
		XmlAdapter xmlAdapter = factory.adapt(node);
		SemanticElement semanticElement = createMainElement();
		xmlAdapter.initialize(semanticElement);
		return semanticElement;
	}
	
	private Node getDocumentElement(StructuredTextEditor sourcePage) {
		Node result = null;
		Document document = (Document) sourcePage
				.getAdapter(Document.class);
		if (document != null) {
			result = (Node) document.getDocumentElement();
		}
		return result;
	}

	private void initSourcePageContextMenu(final Control control) {
		sourceContextMenuManager = new MenuManager("#PopupMenu");
		sourceContextMenuManager.setRemoveAllWhenShown(true);
		sourceContextMenuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager m) {
				fillContextMenu(m);
			}
		});
		Menu menu = sourceContextMenuManager.createContextMenu(control);
		getSite().registerContextMenu(
				"org.jbpm.ui.editor.DesignerEditor.SourcePopupMenu",
				sourceContextMenuManager, getSite().getSelectionProvider());

		control.setMenu(menu);
	}

	private void fillContextMenu(final IMenuManager menuManager) {
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void handleCommandStackChanged() {
		getActionRegistry().updateStackActions();
		if (!isDirty() && getCommandStack().isDirty() && editAllowed()) {
			setDirty(true);
		}
	}
	
	private boolean editAllowed() {
		return true;
	}

	protected void pageChange(int newPageIndex) {
		if (newPageIndex == 0) {
			IKeyBindingService service = getSite().getKeyBindingService();
			if (service instanceof INestableKeyBindingService) {
				INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
				nestableService.activateKeyBindingService(null);
			}
		}
		super.pageChange(newPageIndex);
	}

	public void setDirty(boolean dirty) {
		isDirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	public boolean isDirty() {
		return isDirty || super.isDirty();
	}

	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	public GraphPage getGraphPage() {
		return graphPage;
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	public CommandStack getCommandStack() {
		return editDomain.getCommandStack();
	}

	public void setSelectionSynchronizer(
			SelectionSynchronizer selectionSynchronizer) {
		this.selectionSynchronizer = selectionSynchronizer;
	}

	public SelectionSynchronizer getSelectionSynchronizer() {
		return selectionSynchronizer;
	}

	public SemanticElementFactory getSemanticElementFactory() {
		if (semanticElementFactory == null) {
			semanticElementFactory = new SemanticElementFactory(getContributorId());
		}
		return semanticElementFactory;
	}
	
	public NotationElementFactory getNotationElementFactory() {
		if (notationElementFactory == null) {
			notationElementFactory = new NotationElementFactory();
		}
		return notationElementFactory;
	}
	
	public RootContainer getRootContainer() {
		return rootContainer;
	}
	
	public void setRootContainer(RootContainer rootContainer) {
		this.rootContainer = rootContainer;
	}
	
	public ContentProvider getContentProvider() {
		if (contentProvider == null) {
			contentProvider = createContentProvider();
		}
		return contentProvider;
	}
	
	public GraphicalViewer getGraphicalViewer() {
		return getGraphPage().getDesignerModelViewer();
	}

	public OutlineViewer getOutlineViewer() {
		return getGraphPage().getOutlineViewer();
	}

	public Object getAdapter(Class adapter) {
		Object result = null;
		if (adapter == CommandStack.class) {
			result = getCommandStack();
		} else if (adapter == IContentOutlinePage.class) {
			return getOutlineViewer();
		} else if (adapter == IPropertySheetPage.class) {
	            return new TabbedPropertySheetPage(this);
		} else if (adapter == org.eclipse.gef.GraphicalViewer.class) {
			return getGraphicalViewer();
		} else {
			result = super.getAdapter(adapter);
		}
		return result;
	}

	public void doSave(IProgressMonitor monitor) {
		if (!checkReadOnly()) return;
		super.doSave(monitor);
		getGraphPage().doSave(monitor);
		boolean saved = getContentProvider().saveToInput(getEditorInput(), getRootContainer());
		if (saved) {
			getCommandStack().markSaveLocation();
			setDirty(false);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}
	
	public void dispose() {
		super.dispose();
	}
	
	protected SelectionSynchronizer createSelectionSynchronizer() {
		return new SelectionSynchronizer(); 
	}
	
	private boolean checkReadOnly() {
		IFile inputFile = ((FileEditorInput)getEditorInput()).getFile();
		IFolder inputFolder = (IFolder)inputFile.getParent();
		IFile notationInfoFile = inputFolder.getFile(getContentProvider().getNotationInfoFileName(inputFile.getName()));
		IFile diagramImageFile = inputFolder.getFile(getContentProvider().getDiagramImageFileName(inputFile.getName()));
		String readOnlyFiles = "";
		ArrayList readOnlyFilesList = new ArrayList();
		if (inputFile.isReadOnly()) {
			readOnlyFiles += ("- " + inputFile.getFullPath().toOSString() + "\n");
			readOnlyFilesList.add(inputFile); 
		}
		if (notationInfoFile.exists() && notationInfoFile.isReadOnly()) {
			readOnlyFiles += ("- " + notationInfoFile.getFullPath().toOSString() + "\n");
			readOnlyFilesList.add(notationInfoFile);
		}
		if (diagramImageFile.exists() && diagramImageFile.isReadOnly()) {
			readOnlyFiles += ("- " + diagramImageFile.getFullPath().toOSString() + "\n");
			readOnlyFilesList.add(diagramImageFile);
		}
		if (readOnlyFilesList.isEmpty()) return true;
		boolean answer = MessageDialog.openQuestion(
				getSite().getShell(), 
				"Read-only Input Files Detected", 
				"The following files have a read-only indicator which needs to be changed to read-write in order to be able to save the process correctly.\n\n" +
				readOnlyFiles + "\n" +
				"Do you want to perform this change now?");
		if (answer) {
			try {
				ResourceAttributes resourceAttributes = new ResourceAttributes();
				resourceAttributes.setReadOnly(false);
				for (int i = 0; i < readOnlyFilesList.size(); i++) {
					((IFile)readOnlyFilesList.get(i)).setResourceAttributes(resourceAttributes);
				}
				return false;
			} catch (CoreException e) {
				Logger.logError("Error while trying to set files writeable", e);
				return true;
			}
		} else {
			return true;
		}
	}
	
	protected abstract ContentProvider createContentProvider();
	protected abstract SemanticElement createMainElement();
	protected abstract GraphicalViewer createGraphicalViewer();
	protected abstract OutlineViewer createOutlineViewer();

}
