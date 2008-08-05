package org.jboss.tools.flow.jpdl4.wrapper;

import org.jboss.tools.flow.common.wrapper.DefaultContainerWrapper;
import org.jboss.tools.flow.jpdl4.model.SuperState;

public class SuperStateWrapper extends DefaultContainerWrapper {
	
    public SuperStateWrapper() {
        setElement(new SuperState());
    }
    
}
