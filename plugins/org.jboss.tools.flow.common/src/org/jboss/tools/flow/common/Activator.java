package org.jboss.tools.flow.common;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">kris verlaenen </a>
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.jboss.tools.process";

    private static Activator plugin;

    public Activator() {
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    public static Activator getDefault() {
        return plugin;
    }

    private static String getUniqueIdentifier() {
        if (getDefault() == null ) {
            return PLUGIN_ID;
        }
        return getDefault().getBundle().getSymbolicName();
    }

    public static void log(Throwable t) {
        log(new Status(IStatus.ERROR, getUniqueIdentifier(), 0,
            "Internal error in JBoss Tools Process Plugin: ", t));
    }

    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }

}
