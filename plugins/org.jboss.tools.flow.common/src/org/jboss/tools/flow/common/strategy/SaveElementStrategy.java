package org.jboss.tools.flow.common.strategy;

import org.jboss.tools.flow.common.model.Container;

public interface SaveElementStrategy {
	
	void setContainer(Container container);

	boolean acceptsElement(Object element);
	
}
