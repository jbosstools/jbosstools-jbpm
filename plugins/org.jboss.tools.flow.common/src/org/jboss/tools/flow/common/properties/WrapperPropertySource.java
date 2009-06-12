package org.jboss.tools.flow.common.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class WrapperPropertySource implements IPropertySource {
	
	private Wrapper wrapper;
	private IPropertySource elementPropertySource;
	
	public WrapperPropertySource(Wrapper wrapper) {
		this.wrapper = wrapper;
		if (wrapper != null && wrapper.getElement() != null) {
			Object object = wrapper.getElement().getMetaData("propertySource");
			if (object != null && object instanceof IPropertySource) {
				elementPropertySource = (IPropertySource)object;
			}
		}
	}

	public Object getEditableValue() {
		if (elementPropertySource != null) {
			return elementPropertySource.getEditableValue();
		}
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (elementPropertySource != null) {
			return elementPropertySource.getPropertyDescriptors();
		}
		return new IPropertyDescriptor[0];
	}

	public Object getPropertyValue(Object id) {
		if (elementPropertySource != null) {
			return elementPropertySource.getPropertyValue(id);
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		if (elementPropertySource != null) {
			return elementPropertySource.isPropertySet(id);
		}
		return false;
	}

	public void resetPropertyValue(Object id) {
		if (elementPropertySource != null) {
			elementPropertySource.resetPropertyValue(id);
		}
	}

	public void setPropertyValue(Object id, Object newValue) {
		if (elementPropertySource != null) {
			Object oldValue = elementPropertySource.getPropertyValue(id);
			elementPropertySource.setPropertyValue(id, newValue);
			wrapper.notifyListeners(Wrapper.CHANGE_PROPERTY, id, wrapper, oldValue, newValue);
		}
	}
	
	protected Wrapper getWrapper() {
		return wrapper;
	}
	
	protected IPropertyDescriptor[] merge(IPropertyDescriptor[] first, IPropertyDescriptor[] second) {
		IPropertyDescriptor[] result = new IPropertyDescriptor[first.length + second.length];
		for (int i = 0; i < first.length; i++) {
			result[i] = first[i];
		}
		for (int i = first.length; i < result.length; i++) {
			result[i] = second[i - first.length];
		}
		return result;
	}

}
