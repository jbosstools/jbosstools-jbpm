package org.jboss.tools.flow.jpdl4.io;

import org.eclipse.core.runtime.IConfigurationElement;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Argument;
import org.jboss.tools.flow.jpdl4.model.CancelEndEvent;
import org.jboss.tools.flow.jpdl4.model.CustomTask;
import org.jboss.tools.flow.jpdl4.model.ErrorEndEvent;
import org.jboss.tools.flow.jpdl4.model.EventListener;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;
import org.jboss.tools.flow.jpdl4.model.ExclusiveGateway;
import org.jboss.tools.flow.jpdl4.model.Field;
import org.jboss.tools.flow.jpdl4.model.ForkParallelGateway;
import org.jboss.tools.flow.jpdl4.model.HqlTask;
import org.jboss.tools.flow.jpdl4.model.HumanTask;
import org.jboss.tools.flow.jpdl4.model.JavaTask;
import org.jboss.tools.flow.jpdl4.model.JoinParallelGateway;
import org.jboss.tools.flow.jpdl4.model.MailTask;
import org.jboss.tools.flow.jpdl4.model.Parameter;
import org.jboss.tools.flow.jpdl4.model.PrimitiveObject;
import org.jboss.tools.flow.jpdl4.model.Process;
import org.jboss.tools.flow.jpdl4.model.ScriptTask;
import org.jboss.tools.flow.jpdl4.model.SequenceFlow;
import org.jboss.tools.flow.jpdl4.model.ServiceTask;
import org.jboss.tools.flow.jpdl4.model.SqlTask;
import org.jboss.tools.flow.jpdl4.model.StartEvent;
import org.jboss.tools.flow.jpdl4.model.SubprocessTask;
import org.jboss.tools.flow.jpdl4.model.SuperState;
import org.jboss.tools.flow.jpdl4.model.Swimlane;
import org.jboss.tools.flow.jpdl4.model.TerminateEndEvent;
import org.jboss.tools.flow.jpdl4.model.Timer;
import org.jboss.tools.flow.jpdl4.model.WaitTask;
import org.w3c.dom.Element;

public class Registry {
	
	public static String getElementId(String nodeName) {
		if ("process".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.process";
		else if ("start".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.startEvent";
		else if ("end".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.terminateEndEvent";
		else if ("end-error".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.errorEndEvent";
		else if ("end-cancel".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.cancelEndEvent";
		else if ("state".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.waitTask";
		else if ("hql".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.hqlTask";
		else if ("sql".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.sqlTask";
		else if ("java".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.javaTask";
		else if ("script".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.scriptTask";
		else if ("esb".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.serviceTask";
		else if ("mail".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.mailTask";
		else if ("task".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.humanTask";
		else if ("custom".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.customTask";
		else if ("sub-process".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.subprocessTask";
		else if ("decision".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.exclusiveGateway";
		else if ("join".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.parallelJoinGateway";
		else if ("fork".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.parallelForkGateway";
		else if ("transition".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.sequenceFlow";
		else if ("swimlane".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.swimlane";
		else if ("timer".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.timer";
		else if ("on".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.eventListenerContainer";
		else if ("event-listener".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.eventListener";
		else if ("parameter-in".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.inputParameter";
		else if ("parameter-out".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.outputParameter";
		else if ("field".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.field";
		else if ("arg".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.argument";
		// wire object group treated as one kind of element
		else if ("null".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("ref".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("env-ref".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("jndi".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("list".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("map".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("set".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("properties".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("object".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("string".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("byte".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("char".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("double".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("false".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("float".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("int".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("long".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("short".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		else if ("true".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.primitive";
		// no corresponding element
		else return null;
	}

	public static ElementDeserializer getElementDeserializer(Wrapper wrapper) {
		if (wrapper instanceof FlowWrapper) {
			return new ProcessDeserializer();
		} else if (wrapper instanceof NodeWrapper) {
			return getNodeDeserializer(wrapper);
		} else if (wrapper instanceof ConnectionWrapper) {
			return new SequenceFlowDeserializer();
		} else if (wrapper instanceof DefaultWrapper) {
			return getDefaultDeserializer(wrapper);
		}
		return null;
	}

	public static PostProcessor getPostProcessor(Wrapper wrapper) {
		if (wrapper instanceof FlowWrapper) {
			return new ProcessPostProcessor();
		}
		return null;
	}
	

	private static ElementDeserializer getDefaultDeserializer(Wrapper wrapper) {
		Object element = wrapper.getElement();
		if (element instanceof Swimlane) {
			return new SwimlaneDeserializer();
		} else if (element instanceof Timer) {
			return new TimerDeserializer();
		} else if (element instanceof EventListenerContainer) {
			return new EventListenerContainerDeserializer();
		} else if (element instanceof EventListener) {
			return new EventListenerDeserializer();
		} else if (element instanceof Parameter) {
			return new ParameterDeserializer();
		} else if (element instanceof Argument) {
			return new ArgumentDeserializer();
		} else if (element instanceof Field) {
			return new FieldDeserializer();
		} else if (element instanceof PrimitiveObject) {
			return new PrimitiveObjectDeserializer();
		}
		return null;
	}
	
	private static ElementDeserializer getNodeDeserializer(Wrapper wrapper) {
		Object element = wrapper.getElement();
		if (element instanceof HumanTask) {
			return new HumanTaskDeserializer();
		} else if (element instanceof SubprocessTask) {
			return new SubprocessTaskDeserializer();
		} else if (element instanceof ExclusiveGateway) {
			return new ExclusiveGatewayDeserializer();
		} else if (element instanceof TerminateEndEvent) {
			return new TerminateEndEventDeserializer();
		} else if (element instanceof JavaTask) {
			return new JavaTaskDeserializer();
		} else if (element instanceof ScriptTask) {
			return new ScriptTaskDeserializer();
		} else if (element instanceof HqlTask) {
			return new HqlTaskDeserializer();
		} else {
			return new NodeDeserializer();
		}
	}
	
	protected static Wrapper createWrapper(Element element) {
		String elementId = Registry.getElementId(element.getNodeName());
		if (elementId == null) return null;
		Wrapper result = ElementRegistry.createWrapper(elementId);
		if (result == null) return null;
		ElementDeserializer elementDeserializer = Registry.getElementDeserializer(result);
		if (elementDeserializer != null) {
			elementDeserializer.deserializeAttributes(result, element);
			elementDeserializer.deserializeChildNodes(result, element);
		}
		PostProcessor postProcessor = Registry.getPostProcessor(result);
		if (postProcessor != null) {
			postProcessor.postProcess(result);
		}
		return result;
	}
	
	public static ElementSerializer getElementSerializer(org.jboss.tools.flow.common.model.Element element) {
    	if (element instanceof SequenceFlow) {
    		return new SequenceFlowSerializer();
    	} else if (element instanceof TerminateEndEvent) {
    		return new TerminateEndEventSerializer();
    	} else if (element instanceof ErrorEndEvent) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof CancelEndEvent) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof StartEvent) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof SuperState) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof WaitTask) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof HqlTask) {
    		return new HqlTaskSerializer();
    	} else if (element instanceof SqlTask) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof JavaTask) {
    		return new JavaTaskSerializer();
       	} else if (element instanceof ScriptTask) {
       		return new ScriptTaskSerializer();
       	} else if (element instanceof MailTask) {
       		return new ProcessNodeSerializer();
    	} else if (element instanceof ServiceTask) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof HumanTask) {
    		return new HumanTaskSerializer();
    	} else if (element instanceof SubprocessTask) {
    		return new SubprocessTaskSerializer();
    	} else if (element instanceof CustomTask) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof ExclusiveGateway) {
    		return new ExclusiveGatewaySerializer();
    	} else if (element instanceof ForkParallelGateway) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof JoinParallelGateway) {
    		return new ProcessNodeSerializer();
    	} else if (element instanceof Process) {
    		return new ProcessSerializer();
    	} else if (element instanceof Swimlane) {
    		return new SwimlaneSerializer();
    	} else if (element instanceof Timer) {
    		return new TimerSerializer();
    	} else if (element instanceof EventListenerContainer) {
    		return new EventListenerContainerSerializer();
    	} else if (element instanceof EventListener) {
    		return new EventListenerSerializer();
    	} else if (element instanceof Parameter) {
    		return new ParameterSerializer();
    	} else if (element instanceof Argument) {
    		return new ArgumentSerializer();
    	} else if (element instanceof Field) {
    		return new FieldSerializer();
    	}
		return null;
	}
	
    public static String getXmlNodeName(org.jboss.tools.flow.common.model.Element element) {
    	IConfigurationElement configuration = (IConfigurationElement)element.getMetaData("configurationElement");
    	String elementId = configuration.getAttribute("id");
		if ("org.jboss.tools.flow.jpdl4.process".equals(elementId)) return "process";
		else if ("org.jboss.tools.flow.jpdl4.startEvent".equals(elementId)) return "start";
		else if ("org.jboss.tools.flow.jpdl4.terminateEndEvent".equals(elementId)) return "end";
		else if ("org.jboss.tools.flow.jpdl4.errorEndEvent".equals(elementId)) return "end-error";
		else if ("org.jboss.tools.flow.jpdl4.cancelEndEvent".equals(elementId)) return "end-cancel";
		else if ("org.jboss.tools.flow.jpdl4.waitTask".equals(elementId)) return "state";
		else if ("org.jboss.tools.flow.jpdl4.hqlTask".equals(elementId)) return "hql";
		else if ("org.jboss.tools.flow.jpdl4.sqlTask".equals(elementId)) return "sql";
		else if ("org.jboss.tools.flow.jpdl4.javaTask".equals(elementId)) return "java";
		else if ("org.jboss.tools.flow.jpdl4.scriptTask".equals(elementId)) return "script";
		else if ("org.jboss.tools.flow.jpdl4.mailTask".equals(elementId)) return "mail";
		else if ("org.jboss.tools.flow.jpdl4.serviceTask".equals(elementId)) return "esb";
		else if ("org.jboss.tools.flow.jpdl4.humanTask".equals(elementId)) return "task";
		else if ("org.jboss.tools.flow.jpdl4.subprocessTask".equals(elementId)) return "sub-process";
		else if ("org.jboss.tools.flow.jpdl4.customTask".equals(elementId)) return "custom";
		else if ("org.jboss.tools.flow.jpdl4.exclusiveGateway".equals(elementId)) return "decision";
		else if ("org.jboss.tools.flow.jpdl4.parallelJoinGateway".equals(elementId)) return "join";
		else if ("org.jboss.tools.flow.jpdl4.parallelForkGateway".equals(elementId)) return "fork";
		else if ("org.jboss.tools.flow.jpdl4.sequenceFlow".equals(elementId)) return "transition";
		else if ("org.jboss.tools.flow.jpdl4.swimlane".equals(elementId)) return "swimlane";
		else if ("org.jboss.tools.flow.jpdl4.timer".equals(elementId)) return "timer";
		else if ("org.jboss.tools.flow.jpdl4.eventListenerContainer".equals(elementId)) return "on";
		else if ("org.jboss.tools.flow.jpdl4.eventListener".equals(elementId)) return "event-listener";
		else if ("org.jboss.tools.flow.jpdl4.inputParameter".equals(elementId)) return "parameter-in";
		else if ("org.jboss.tools.flow.jpdl4.outputParameter".equals(elementId)) return "parameter-out";
		else if ("org.jboss.tools.flow.jpdl4.argument".equals(elementId)) return "arg";
		else if ("org.jboss.tools.flow.jpdl4.field".equals(elementId)) return "field";
		else return null;
    }
    
}
