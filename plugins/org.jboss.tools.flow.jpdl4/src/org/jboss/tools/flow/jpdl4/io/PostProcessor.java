/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;

interface PostProcessor {
	void postProcess(Wrapper wrapper);
}