package com.sb.smartgui.filter;

import java.util.logging.Logger;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class LoggingDocumentFilter extends DocumentFilter {

    public static final Logger LOG = Logger.getLogger(LoggingDocumentFilter.class.getName());
    
    @Override
    public void insertString(FilterBypass bypass, int offset, String insert, AttributeSet attrSet)
	    throws BadLocationException {
	logInsertString(offset, insert);
	super.insertString(bypass, offset, insert, attrSet);
    }

    protected void logInsertString(int offset, String insert) {
	LOG.finest("insertString(offset = " + offset + ", insert = \"" + insert + "\"");
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
	    AttributeSet attrs) throws BadLocationException {
	logReplace(offset, length, text);
	super.replace(fb, offset, length, text, attrs);
    }

    protected void logReplace(int offset, int length, String text) {
	LOG.finest("replace(offset = " + offset + ", length = " + length + ", text = \"" + text + "\")");
    }
    
    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
	logRemove(offset, length);
	super.remove(fb, offset, length);
    }

    protected void logRemove(int offset, int length) {
	LOG.finest("remove(offset = " + offset + ", length = " + length);
    }
    
}
