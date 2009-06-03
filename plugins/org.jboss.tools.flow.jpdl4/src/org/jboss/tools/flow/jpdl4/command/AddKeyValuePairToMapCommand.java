package org.jboss.tools.flow.jpdl4.command;

import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

public class AddKeyValuePairToMapCommand extends Command {

    private IPropertySource target;
    
    private Object mapId;
    private Object key;
    private Object newValue;
    private Object oldValue;
    
    @SuppressWarnings("unchecked")
	public void execute() {
		Object object = target.getPropertyValue(mapId);
		if (! (object instanceof Map)) return;
		if (oldValue == null) {
			oldValue = ((Map)object).get(key);
		}
		if (newValue == null) {
			((Map)object).remove(key);
		} else {
			((Map)object).put(key, newValue);
		}
    }
    
    public boolean canExecute() {
    	return target != null;
    }
    
    public void setMapId(Object mapId) {
    	this.mapId = mapId;
    }

    public void setKey(Object key) {
    	this.key = key;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public void setTarget(IPropertySource target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
	public void undo() {
		Object object = target.getPropertyValue(mapId);
		if (! (object instanceof Map)) return;
		if (oldValue == null) {
			((Map)object).remove(key);
		} else {
			((Map)object).put(key, oldValue);
		}
    }
}
