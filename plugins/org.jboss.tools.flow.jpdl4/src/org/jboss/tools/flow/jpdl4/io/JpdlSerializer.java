package org.jboss.tools.flow.jpdl4.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.jboss.tools.flow.common.wrapper.Wrapper;

public class JpdlSerializer {
	
    public static void serialize(Wrapper wrapper, OutputStream os) throws IOException {
    	StringBuffer buffer = new StringBuffer();
    	serialize(wrapper, buffer, 0);
    	Writer writer = new OutputStreamWriter(os);
    	writer.write(buffer.toString());
    	writer.close();
    }
    
    public static void serialize(Wrapper wrapper, StringBuffer buffer, int level) {
    	ElementSerializer elementSerializer = Registry.getElementSerializer(wrapper.getElement());
    	if (elementSerializer != null) {
    		elementSerializer.appendToBuffer(buffer, wrapper, level);
    	}
    }
    
}
