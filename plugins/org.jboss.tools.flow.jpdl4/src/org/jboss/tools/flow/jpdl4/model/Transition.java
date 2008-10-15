package org.jboss.tools.flow.jpdl4.model;

import org.jboss.tools.flow.common.model.DefaultConnection;
import org.jboss.tools.flow.common.model.Node;

public class Transition extends DefaultConnection {

	private static final String SCHEMA = 
		"<element name='transition'>" +
		"   <complexType>" +
		"      <attribute name='to' type='string'/>" +
		"   </complexType>" +
		"</element>";
	
	public Transition() {
		this(null, null);
	}
	
	public Transition(Node from, Node to) {
		super(from, to);
		setMetaData("schema", SCHEMA);
	}
	
	String getToAsString() {
		if (getTo() == null) { 
			return null;
		} else {
			return getTo().getName();
		}
	}
	
}
