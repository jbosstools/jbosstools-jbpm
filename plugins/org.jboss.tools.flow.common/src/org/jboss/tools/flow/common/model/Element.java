package org.jboss.tools.flow.common.model;

public interface Element {

    void setMetaData(String name, Object value);
    
    Object getMetaData(String name);
    
}
