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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;

/**
 * Helper class for tests.
 * @author Henning Heitkoetter
 *
 */
public class TestHelper {

    /**
     * Cleans the test directory by moving the specified file to another directory.
     * @param fileURI URI of the file.
     * @throws IOException
     */
    public static void moveFile(URI fileURI) throws IOException {
        File f = new File(fileURI.toString());
        if (f.exists()) {
            File dest = new File("lastResult/" + f.getName()/*String.format("result/%tQ/", new Date())*/);
            dest.delete();
            f.renameTo(dest);
        }
    }

    /**
     * Searches for the specified String within the specified file.
     * @param fileURI URI of the file.
     * @param toFind String to find.
     * @return {@code true}, if the contents of the file include {@code toFind}.
     * @throws IOException
     */
    public static boolean search(URI fileURI, String toFind) throws IOException {
        File file = new File(fileURI.toString());
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream f = new BufferedInputStream(new FileInputStream(file));
        f.read(buffer);
        f.close();
        return new String(buffer).indexOf(toFind) != -1;
    }

}
