package com.sample;

import junit.framework.TestCase;

import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;

public class SimpleProcessTest extends TestCase {

	public void testSimpleProcess() throws Exception {

		// Extract a process definition from the processdefinition.xml file.
		ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource("simple/processdefinition.xml");
		assertNotNull("Definition should not be null", processDefinition);

		// Create an instance of the process definition.
		ProcessInstance instance = new ProcessInstance(processDefinition);
		assertEquals(
				"Instance is in start state", 
				instance.getRootToken().getNode().getName(), 
				"start");
		assertNull(
				"Message variable should not exist yet", 
				instance.getContextInstance().getVariable("message"));

		// Move the process instance from its start state to the first state.
		// The configured action should execute and the appropriate message
		// should appear in the message process variable.
		instance.signal();
		assertEquals(
				"Instance is in first state", 
				instance.getRootToken().getNode().getName(), 
				"first");
		assertEquals(
				"Message variable contains message", 
				instance.getContextInstance().getVariable("message"),
				"Going to the first state!");

		// Move the process instance to the end state. The configured action 
		// should execute again. The message variable contains a new value.
		instance.signal();
		assertEquals(
				"Instance is in end state", 
				instance.getRootToken().getNode().getName(), 
				"end");
		assertTrue("Instance has ended", instance.hasEnded());
		assertEquals(
				"Message variable is changed", 
				instance.getContextInstance().getVariable("message"),
				"About to finish!");

	}

}
