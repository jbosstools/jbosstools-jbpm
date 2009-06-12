package org.jboss.tools.flow.common.wrapper;

public interface FlowWrapper extends ContainerWrapper {

    Integer ROUTER_LAYOUT_MANUAL = new Integer(0);
    Integer ROUTER_LAYOUT_MANHATTAN = new Integer(1);
    Integer ROUTER_LAYOUT_SHORTEST_PATH = new Integer(2);


	NodeWrapper getNodeWrapper(String id);

	Object getRouterLayout();

}
