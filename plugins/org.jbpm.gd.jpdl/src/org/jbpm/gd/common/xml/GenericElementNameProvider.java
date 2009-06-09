package org.jbpm.gd.common.xml;

import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.common.model.SemanticElement;

public class GenericElementNameProvider implements XmlAdapterNameProvider {

	public String getName(SemanticElement element) {
		if (!(element instanceof GenericElement)) return null;
		return ((GenericElement)element).getName();
	}

}
