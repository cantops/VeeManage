package org.uigl.veemanage.httpd;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.logging.Level;

import org.uigl.veemanage.VeeManage;

public class Template {

	public static InputStream applyTemplate(String resourceName, Session args) {
		InputStream template =  ClassLoader.class.getResourceAsStream(resourceName);
		return new Template.TemplateStream(template, args);
	}
	
	private static class TemplateStream extends FilterInputStream {

	    LinkedList<Character> mBuffer = new LinkedList<Character>();
	    Session mArgs;
	    
		protected TemplateStream(InputStream in, Session args) {
			super(in);
			mArgs = args;
			if (mArgs == null)
				VeeManage.LOGGER.logp(Level.SEVERE, TemplateStream.class.getName(), "TemplateStream(InputStream in, Session args)", "Null Args");
		}

		@Override
	    public int read() throws IOException {
	        if (!mBuffer.isEmpty())
	            return mBuffer.remove();

	        int c1 = super.read();
	        if (c1 != '<' || mArgs == null)
	            return c1;

	        int c2 = super.read();

	        if (c2 == '$') {
	            StringBuffer varName = new StringBuffer();
	            while ((c2 = super.read()) != '>')
	                varName.append((char) c2);
	            
	            Object varVal = mArgs.get(varName.toString());
	            if (varVal == null || varVal.toString() == null) {
	            	VeeManage.LOGGER.logp(Level.WARNING, TemplateStream.class.getName(), "read()", "Cannot find variable \"" + varName + "\"");
	            	varVal = varName;
	            }
	            for (char vc : varVal.toString().toCharArray())
	            	mBuffer.add(vc);
	            
	            return mBuffer.remove();

	        } else {
	        	mBuffer.add((char) c2);
	            return c1;
	        }
	    }
	    
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			if(len == 0)
				return 0;

			int c, reads = 0;
			while ((c = read()) >= 0 && (off + reads) < len) {
				b[off + reads++] = (byte) c;
			}
			return reads;
		}
		
	}
	
}
