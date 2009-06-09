package org.jbpm.gd.common.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jbpm.gd.common.registry.RegistryRegistry;
import org.jbpm.gd.common.registry.SemanticElementRegistry;

public class SemanticElementFactory {
	
	SemanticElementRegistry registry;
	String editorId;
	
	public SemanticElementFactory(String editorId) {
		this.editorId = editorId;
		registry = RegistryRegistry.getSemanticElementRegistry(editorId);
	}
	
	public SemanticElement createById(String elementId) {
		try {
			IConfigurationElement element = registry.getConfigurationElementById(elementId);
			if (element == null) return null;
			AbstractSemanticElement result = (AbstractSemanticElement)element.createExecutableExtension("modelClass");
			result.setElementId(element.getAttribute("id"));
			result.setNamePrefix(element.getAttribute("name"));
			result.setLabel(element.getAttribute("label"));
			result.setIconDescriptor(getImageDescriptor(element));
			result.setFactory(this);
			result.initialize();
			return result;
		} catch (CoreException e) {
			throw new RuntimeException("Creation of executable extension failed", e);
		}
	}
	
	private ImageDescriptor getImageDescriptor(IConfigurationElement element) {
		ImageDescriptor result = null;
		String iconPath = element.getAttribute("icon");
		if (iconPath != null) {
			result = ImageDescriptor.createFromURL(
					Platform.getBundle(element.getContributor().getName()).getEntry(iconPath));
		}
		return result;
	}
	
	public String getEditorId() {
		return editorId;
	}
	
}
