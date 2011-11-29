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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.bpmn2.util.NamespaceHelper;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.FeatureNotFoundException;
import org.eclipse.emf.ecore.xmi.PackageNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXParseException;

/**
 * Tests serialization as XML.
 * @author Henning Heitkoetter
 *
 */
public class XMLSerializationTest {
    private static final String EXTENSION_BPMN2 = "bpmn2";
    protected Definitions model;
    protected List<URI> createdFiles;

    // Set-up and tear-down methods

    /**
     * Registers the BPMN2 resource factory under the "bpmn2" extension (only in standalone mode).
     */
    @Before
    public void setUpResourceFactoryRegistry() {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING)
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(EXTENSION_BPMN2,
                    new Bpmn2ResourceFactoryImpl());
    }

    /**
     * Prepares a test run by initializing all fields.
     * 
     * A basic BPMN2 model is created in {@link #model}, thereby initializing the BPMN2 package.
     */
    @Before
    public void setUpFields() {
        model = Bpmn2Factory.eINSTANCE.createDefinitions();
        createdFiles = new LinkedList<URI>();
    }

    /**
     * Tears down a test run by clearing the resource factory registry and moving {@link #createdFiles created
     * files} to a result folder. 
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING)
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().clear();

        for (URI cur : createdFiles)
            TestHelper.moveFile(cur);
    }

    // Utility methods

    /**
     * The extension for all files that are created.
     * @return File extension, i.e. {@code "bpmn2"}.
     */
    protected String getFileExtension() {
        return EXTENSION_BPMN2;
    }

    /**
     * Creates a resource with the specified name, sets its content and saves it.
     * Afterwards, loads the thus created resource from a fresh resource set and returns it.
     * 
     * @param name Filename, without folder and extension.
     * @param content Designated content of the resource.
     * @return The loaded resource.
     * @throws IOException
     */
    public Resource createWithContentAndLoad(final String name, EObject content) throws IOException {
        URI fileUri = URI.createFileURI("tmp/" + name + "." + getFileExtension());
        createResourceWithContent(fileUri, content);

        return getResource(fileUri);
    }

    /**
     * Loads the resource from the specified URI.
     * @param fileUri The URI of the file.
     * @return The resource.
     */
    protected Resource getResource(URI fileUri) {
        try {
            return new ResourceSetImpl().getResource(fileUri, true);
        } catch (WrappedException e) {
            if (e.exception() instanceof PackageNotFoundException)
                fail(String.format("Package %s not registered",
                        ((PackageNotFoundException) e.exception()).uri()));
            else
                throw e;
        }
        return null; // never reached.
    }

    /**
     * Creates a resource with the specified name, sets its content and saves it.
     * 
     * @param fileUri Filename, without folder and extension.
     * @param content Designated content of the resource.
     * @throws IOException
     */
    protected void createResourceWithContent(URI fileUri, EObject content) throws IOException {
        Resource res = new ResourceSetImpl().createResource(fileUri);
        assertNotNull("No resource factory registered for " + fileUri, res);
        res.getContents().add(content);
        res.save(null);
        createdFiles.add(fileUri);
    }

    protected Definitions getRootDefinitionElement(Resource res) {
        EObject root = res.getContents().get(0);
        if (root instanceof DocumentRoot)
            return ((DocumentRoot) root).getDefinitions();
        return (Definitions) root;
    }

    // Tests

    /**
     * Checks if serialization works at all.
     * @throws Exception
     */
    @Test
    public void testBasicSerialization() throws Exception {
        Resource res = createWithContentAndLoad("basic", model);

        assertTrue("Resource loaded with errors", res.getErrors().isEmpty());

        checkBasicSerialization(res);
    }

    /**
     * Performs the actual checks if the basic serialization succeeded.
     * 
     * Can be overriden by subclasses.
     * @param res
     */
    protected void checkBasicSerialization(Resource res) {
        EObject root = res.getContents().get(0);
        if (root instanceof DocumentRoot) {
            DocumentRoot docRoot = (DocumentRoot) root;
            assertTrue("Namespace prefix bpmn2 not present", docRoot.getXMLNSPrefixMap()
                    .containsKey(Bpmn2Package.eNS_PREFIX));
            String NS_URI_EXPECTED = Bpmn2Package.eNS_URI.endsWith("-XMI") ? NamespaceHelper
                    .xmiToXsdNamespaceUri(Bpmn2Package.eNS_URI) : Bpmn2Package.eNS_URI;
            assertEquals("Namespace URI of prefix bpmn2", NS_URI_EXPECTED, docRoot
                    .getXMLNSPrefixMap().get(Bpmn2Package.eNS_PREFIX));
            assertNotNull("No definitions object in doc root", docRoot.getDefinitions());
        } else
            fail("Root element is not DocumentRoot");
    }

    /**
     * Tests if an ID is generated upon save if necessary.
     * @throws Exception
     */
    @Test
    public void testIdSerialization() throws Exception {
        Collaboration c = Bpmn2Factory.eINSTANCE.createCollaboration();
        c.setName("collab1");
        Process p = Bpmn2Factory.eINSTANCE.createProcess();
        p.setDefinitionalCollaborationRef(c);
        model.getRootElements().add(c);
        model.getRootElements().add(p);
        Resource res = createWithContentAndLoad("idOK", model);

        Definitions d = getRootDefinitionElement(res);
        // Technically, only collab1 needs to have an ID, because it is referenced by another element
        for (RootElement cur : d.getRootElements())
            if (cur instanceof Collaboration && ((Collaboration) cur).getName().equals("collab1")) {
                assertNotNull(
                        "No id generated for element \"collab1\", although it is referenced by another element",
                        cur.getId());
                break;
            }
    }

    /**
     * Asserts that no ID is generated for elements that don't have a corresponding feature.
     * @throws IOException 
     */
    @Test
    public void testNoIDForImport() throws IOException {
        model.getImports().add(Bpmn2Factory.eINSTANCE.createImport());
        try {
            Resource res = createWithContentAndLoad("noIDForImport", model);
        } catch (WrappedException e) {
            if (e.exception() instanceof FeatureNotFoundException) {
                FeatureNotFoundException fnfe = ((FeatureNotFoundException) e.exception());
                if (fnfe.getName().equals("id")) {
                    fail("ID was generated for an import element (Import does not have an ID feature)");
                }
            } else
                throw e;
        }
    }

    @Test
    public void testIDAlreadySet() throws Exception {
        model.setId("id1");
        Resource res = null;
        try {
            res = createWithContentAndLoad("idAlreadySet", model);
        } catch (WrappedException e) {
            if (e.exception() instanceof SAXParseException)
                fail("Duplicate attribute 'id'.");
            else
                throw e;
        }
        assertEquals("id1", getRootDefinitionElement(res).getId());
    }
}
