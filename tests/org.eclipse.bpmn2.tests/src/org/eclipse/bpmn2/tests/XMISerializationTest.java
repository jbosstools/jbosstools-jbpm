/**
 * Copyright (c) 2010 Henning Heitkoetter.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Henning Heitkoetter - initial API and implementation
 */
package org.eclipse.bpmn2.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * Tests serialization as XMI.
 * 
 * Runs the same tests as {@link XMLSerializationTest}, but saves them in XMI format. The test methods
 * are inherited and, where necessary, adapted.
 * @author Henning Heitkoetter
 *
 */
public class XMISerializationTest extends XMLSerializationTest {
    private static final String EXTENSION_XMI = "xmi";

    /**
     * Overrides the superclass method, instead registering the default XMI resource factory. 
     */
    @Override
    public void setUpResourceFactoryRegistry() {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING)
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(EXTENSION_XMI,
                    new XMIResourceFactoryImpl());
    }

    /**
     * @return {@code xmi}
     */
    @Override
    protected String getFileExtension() {
        return EXTENSION_XMI;
    }

    @Override
    protected void checkBasicSerialization(Resource res) {
        assertTrue("Resource is not XMI", res instanceof XMIResource);
        assertTrue("Resource loaded with errors", res.getErrors().isEmpty());
        assertTrue("Root element is not Definitions",
                res.getContents().get(0) instanceof Definitions);
        String NS_URI_EXPECTED = Bpmn2Package.eNS_URI.endsWith("-XMI") ? Bpmn2Package.eNS_URI
                : Bpmn2Package.eNS_URI.concat("-XMI");
        try {
            assertTrue(String.format("Namespace uri (%s) not found in result", NS_URI_EXPECTED),
                    TestHelper.search(res.getURI(), NS_URI_EXPECTED));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void testIdSerialization() throws Exception {
        // Success (no IDs needed in XMI)
    }
}
