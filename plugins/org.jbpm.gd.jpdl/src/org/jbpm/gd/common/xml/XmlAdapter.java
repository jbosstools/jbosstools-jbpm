package org.jbpm.gd.common.xml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.model.SemanticElementFactory;
import org.jbpm.gd.common.registry.XmlAdapterRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public abstract class XmlAdapter implements PropertyChangeListener, INodeAdapter {
	
	private static final String PADDING = "\t";
	private static final String INTER_NODE_TYPE_SPACING = "\n";
	private static final String INTER_CHILD_SPACING = "\n";
	private static final String[] CHILD_ELEMENTS = {};
	private static HashMap NODE_TYPES = null;
	
	private Node node;
	private SemanticElement semanticElement;
	private XmlAdapterFactory factory;
	private boolean changing = false;
	
	protected void initialize(String elementType, Document document) {
		node = document.createElement(elementType);
	}
	
	protected void initialize() {
	}
	
	public String getElementType() {
		return node != null ? node.getNodeName() : null;
	}
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
		}
		return NODE_TYPES;
	}
	
	public Node getNode() {
		return node;
	}
	
	public void setNode(Node node) {
		this.node = node;
	}
	
	protected void addElements(SemanticElement[] jpdlElements) {
		for (int i = 0; i < jpdlElements.length; i++) {
			addElement(jpdlElements[i]);
		}
	}
	
	protected void addElement(XmlAdapter adapter) {
		if (adapter == null) return;
		if (contains(adapter)) return;
		Node insertionPoint = null;
		int level = getLevel();
		if (isEmpty()) {
			insertionPoint = node.appendChild(node.getOwnerDocument().createTextNode(getInterNodeTypeSpacing() + getPaddingString(level - 1)));
			insertionPoint = node.insertBefore(adapter.getNode(), insertionPoint);
			node.insertBefore(node.getOwnerDocument().createTextNode(getInterNodeTypeSpacing() + getPaddingString(level)), insertionPoint);
		} else if (hasNode(getNodeType(adapter.getElementType()))) {
			insertionPoint = getLastNode(getNodeType(adapter.getElementType())).getNextSibling();
			if (insertionPoint == null) {
				insertionPoint = node.appendChild(node.getOwnerDocument().createTextNode(getInterNodeTypeSpacing()));
			}
			insertionPoint = node.insertBefore(adapter.getNode(), insertionPoint);
			node.insertBefore(node.getOwnerDocument().createTextNode(getInterChildSpacing() + getPaddingString(level)), insertionPoint);
		} else {
			insertionPoint = prepareInsertionPoint(getNodeType(adapter.getElementType()), level);
			node.insertBefore(adapter.getNode(), insertionPoint);
		}
		adapter.initialize();
	}
	
	private boolean contains(XmlAdapter adapter) {
		Node parent = getNode();
		Node child = adapter.getNode();
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i) == child) {
				return true;
			}
		}
		return false;
	}
	
	protected String getInterNodeTypeSpacing() { 
		return INTER_NODE_TYPE_SPACING;
	}
	
	protected String getInterChildSpacing() {
		return INTER_CHILD_SPACING;
	}
	
	private Node prepareInsertionPoint(String nodeType, int level)  {
		boolean before = false;
		Node insertAfter = null;
		Node insertBefore = null;
		for (int i = 0; i < getChildElements().length; i++) {
			if (getChildElements()[i].equals(nodeType)) {
				before = true;
			} else {
				if (before) {
					if (insertBefore == null) {
						insertBefore = getFirstNode(getChildElements()[i]);
					}
				}
				else {
					Node candidateAfter = getLastNode(getChildElements()[i]);
					if (candidateAfter != null) {
						insertAfter = candidateAfter;
					}
				}			
			}
		}
		if (before && insertBefore != null) {
			return node.insertBefore(node.getOwnerDocument().createTextNode(getInterNodeTypeSpacing() + getPaddingString(level)), insertBefore);
		} else if (insertAfter != null) {
			Node candidate = insertAfter.getNextSibling();
			if (candidate != null) {
				node.insertBefore(node.getOwnerDocument().createTextNode(getInterNodeTypeSpacing() + getPaddingString(level)), candidate);		
				return candidate;
			} else {
				return node.appendChild(node.getOwnerDocument().createTextNode(getInterNodeTypeSpacing()));
			}
		} else {
			return node.appendChild(node.getOwnerDocument().createTextNode(getInterNodeTypeSpacing()));
		}
	}
	
	protected void removeElement(XmlAdapter adapter) {
		if (adapter == null) return;
		Node last = getLastNode(getNodeType(adapter.getElementType()));
		Node toDelete = adapter.getNode();
		Node parent = toDelete.getParentNode();
		if (parent != null && parent == node) {
			if (toDelete == last) {
				removeTextChild(toDelete.getPreviousSibling());
			} else {
				removeTextChild(toDelete.getNextSibling());
			}
			node.removeChild(toDelete);
			if (hasOnlyTextChildren()) {
				emptyNode();
			}
		}		
	}
	
	protected void removeFirst(String nodeType) {
		Node toDelete = getFirstNode(nodeType);
		if (toDelete != null) {
			removeTextChild(toDelete.getPreviousSibling());
			removeTextChild(toDelete.getNextSibling());
			getNode().removeChild(toDelete);
		}
	}
	
	private int getLevel() {
		int result = 0;
		Node node = getNode();
		Document document = getNode().getOwnerDocument();
		while (node != null && node != document) {
			result++;
			node = node.getParentNode();
		}
		return result;
	}
	
	private String getPaddingString(int level) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < level; i++) {
			result.append(PADDING);
		}
		return result.toString();
	}
	
	protected String getNodeType(String elementType) {
		return (String)getNodeTypes().get(elementType);
	}
	
	protected boolean isEmpty() {
		NodeList childNodes = node.getChildNodes();
		int length = childNodes.getLength();
		if (length == 0) return true;
		for (int i = 0; i < length; i++) {
			if (childNodes.item(i).getNodeType() != Node.TEXT_NODE || !("".equals(childNodes.item(i).getNodeValue().trim()))) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean hasOnlyTextChildren() {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() != Node.TEXT_NODE) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean isNodeOfType(Node node, String type) {
		boolean result = false;
		XmlAdapter adapter = getFactory().adapt(node);
		if (adapter != null) {
			result = adapter.getElementType().equals(type);
		}
		return type.equals(getNodeTypes().get(node.getNodeName())) || result;
	}
	
	protected void emptyNode() {
		Node first = node.getFirstChild();
		while (first != null) {
			node.removeChild(first);
			first = node.getFirstChild();
		}
	}
	
	protected Node getFirstNode(String nodeType) {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node item = list.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE && isNodeOfType(item, nodeType)) {
				return item;
			}
		}
		return null;
	}
	
	protected Node getLastNode(String nodeType) {
		NodeList list = node.getChildNodes();
		for (int i = list.getLength(); i > 0; i--) {
			Node item = list.item(i - 1);
			if (item.getNodeType() == Node.ELEMENT_NODE && isNodeOfType(item, nodeType)) {
				return item;
			}
		}
		return null;
	}
	
	protected boolean hasNode(String nodeType) {
		return getFirstNode(nodeType) != null;
	}
	
	protected void removeTextChild(Node child) {
		if (child != null && child.getNodeType() == Node.TEXT_NODE && "".equals(child.getNodeValue().trim())) {
			node.removeChild(child);
		}
	}
	
	protected void setTextContent(String content) {
		String oldContents = getTextContent();
		if (oldContents == null && content == null) return;
		if (oldContents.equals(content)) return;
		removeTextChildren();
		if (content != null) {
			replaceTextChild(content);
		}
	}
		
	private void removeTextChildren() {
		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.TEXT_NODE) {
				getNode().removeChild(list.item(i));
			}
		}
	}

	private void replaceTextChild(String content) {
		int level = getLevel();
		StringBuffer buff = new StringBuffer();
		buff.append(getInterNodeTypeSpacing());
		buff.append(getPaddingString(level));
		buff.append(content);
		buff.append(getInterNodeTypeSpacing());
		buff.append(getPaddingString(level - 1));
		Text text = getNode().getOwnerDocument().createTextNode(buff.toString());
		getNode().appendChild(text);
	}
	
	protected String getTextContent() {
		StringBuffer buffer = new StringBuffer();
		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			String nodeValue = list.item(i).getNodeValue();
			if (nodeValue != null) { 
				buffer.append(nodeValue);
			}
		}
		return buffer.toString().trim();
	}
	
	protected void setAttribute(String name, String value) {
		if (name == null) return;
		String defaultValue = getDefaultValue(name);
		if (defaultValue != null && defaultValue.equals(value)) {
			value = null;
		}
		Node attr = getNode().getAttributes().getNamedItem(name);
		if (value == null) {
			if (attr != null) {
				getNode().getAttributes().removeNamedItem(name);
			} 				
		} else {
			if (attr == null) {
				attr = getNode().getOwnerDocument().createAttribute(name);
				attr.setNodeValue(value);
				getNode().getAttributes().setNamedItem(attr);
			} else if (!value.equals(attr.getNodeValue())) {
				attr.setNodeValue(value);
			}
		}
	}
	
	protected String getDefaultValue(String attributeName) {
		return null;
	}
	
	protected String getAttribute(String name) {
		if (name == null) return null;
		String defaultValue = getDefaultValue(name);
		Node attr = getNode().getAttributes().getNamedItem(name);
		return attr == null ? defaultValue : attr.getNodeValue();
	}
	
	protected Map getAttributes() {
		HashMap result = new HashMap();
		NamedNodeMap map = getNode().getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			Node node = map.item(i);
			result.put(node.getNodeName(), node.getNodeValue());
		}
		return result;
	}
	
	protected XmlAdapter getAdapter(Node node) {
		return getFactory().adapt(node);
	}
	
	protected void setElement(String elementType, SemanticElement oldElement, SemanticElement newElement) {
		removeElement(oldElement);
		addElement(newElement);
	}
	
	protected void removeElement(SemanticElement jpdlElement) {
		if (jpdlElement == null) return;
		XmlAdapter jpdlElementDomAdapter = getFactory().getRegisteredAdapterFor(jpdlElement);
		if (jpdlElementDomAdapter != null) {
			jpdlElementDomAdapter.unregister();
			jpdlElement.removePropertyChangeListener(jpdlElementDomAdapter);
			jpdlElementDomAdapter.setSemanticElement(null);			
		}
		removeElement(jpdlElementDomAdapter);
	}
	
	protected void addElement(SemanticElement jpdlElement) {
		if (jpdlElement == null) return;
		XmlAdapter jpdlElementDomAdapter = getFactory().getRegisteredAdapterFor(jpdlElement);
		if (jpdlElementDomAdapter == null) {
			jpdlElementDomAdapter = getFactory().createAdapterFromModel(jpdlElement);
			jpdlElementDomAdapter.setSemanticElement(jpdlElement);
			jpdlElement.addPropertyChangeListener(jpdlElementDomAdapter);
			jpdlElementDomAdapter.register();
		}
		addElement(jpdlElementDomAdapter);
	}
	
	protected void register() {
		getFactory().register(this);
	}
	
	private void unregister() {
		getFactory().unregister(this);
		NodeList nodeList = getNode().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			XmlAdapter jpdlElementDomAdapter = getAdapter(nodeList.item(i));
			if (jpdlElementDomAdapter != null) {
				jpdlElementDomAdapter.unregister();
			}
		}		
	}
	
	protected void setSemanticElement(SemanticElement semanticElement) {
		this.semanticElement = semanticElement;
	}
	
	public SemanticElement getSemanticElement() {
		return semanticElement;
	}
	
	protected void setFactory(XmlAdapterFactory factory) {
		this.factory = factory;
	}
	
	protected XmlAdapterFactory getFactory() {
		return factory;
	}
	
	protected SemanticElementFactory getSemanticElementFactory() {
		return factory.getSemanticElementFactory();
	}
	
	protected XmlAdapterRegistry getXmlAdapterRegistry() {
		return factory.getXmlAdapterRegistry();
	}
	
	public boolean isAdapterForType(Object type) {
		return type == getFactory() ;
	}

	public void notifyChanged(INodeNotifier notifier, int eventType, Object changedFeature, Object oldValue, Object newValue, int pos) {
		if (INodeNotifier.ADD == eventType) {
			handleDomAdd(newValue);
		} else if (INodeNotifier.REMOVE == eventType) {
			handleDomRemove(oldValue);
		} else 	if (changedFeature != null) {
			modelUpdate(((Node)changedFeature).getNodeName(), (String)newValue);
		} 
	}
	
	protected void handleDomAdd(Object newObject) {
		if (!(newObject instanceof INodeNotifier)) return;
		XmlAdapter adapter = (XmlAdapter)getFactory().adapt((INodeNotifier)newObject);
		if (adapter != null) { 
			modelAdd(adapter);
		}
	}
	
	protected void handleDomRemove(Object oldObject) {
		if (!(oldObject instanceof INodeNotifier)) return;
		XmlAdapter adapter = (XmlAdapter)getFactory().adapt((INodeNotifier)oldObject);
		if (adapter != null) {
			modelRemove(adapter);
		}
	}
	
	protected void modelUpdate(String name, String newValue) {
		if (changing) return;
		SemanticElement semanticElement = getSemanticElement();
		if (semanticElement == null) return;
		changing = true;
		doModelUpdate(name, newValue);
		changing = false;
	}
	
	protected void modelAdd(XmlAdapter child) {
		if (changing) return;
		changing = true;
		doModelAdd(child);
		changing = false;
	}
	
	protected void modelRemove(XmlAdapter child) {
		if (changing) return;
		changing = true;
		doModelRemove(child);
		changing = false;
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		if (changing) return; 
		changing = true;
		doPropertyChange(event);
		changing = false;
	}
	
	protected abstract void doModelUpdate(String name, String newValue);	
	protected abstract void doModelAdd(XmlAdapter child);	
	protected abstract void doModelRemove(XmlAdapter child);
	protected abstract void doPropertyChange(PropertyChangeEvent event);
	
	public void initialize(SemanticElement jpdlElement) {
		setSemanticElement(jpdlElement);
		NodeList nodeList = getNode().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			XmlAdapter jpdlElementDomAdapter = getAdapter(nodeList.item(i));
			if (jpdlElementDomAdapter != null && !"#comment".equals(jpdlElementDomAdapter.getNode().getNodeName())) {
				doModelAdd(jpdlElementDomAdapter);
			}
		}
		register();
	}
	
	protected SemanticElement createSemanticElementFor(XmlAdapter child) {
		IConfigurationElement configurationElement = 
			getXmlAdapterRegistry().getConfigurationElementByXmlNode(child.getNode());
		if (configurationElement == null) return null;
		String id = configurationElement.getAttribute("semanticElement");
		return getSemanticElementFactory().createById(id);
	}
	
}
