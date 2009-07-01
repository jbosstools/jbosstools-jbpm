/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;

interface ElementSerializer {
	void appendToBuffer(StringBuffer buffer, Wrapper wrapper, int level);
}