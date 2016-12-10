package com.sb.smartgui.filter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CharacterCountDocumentFilter extends LoggingDocumentFilter {

    private int maxCharCount;
    private int charCount;

    public CharacterCountDocumentFilter(int maxCharCount) {
	this.maxCharCount = maxCharCount;
	charCount = 0;
    }

    @Override
    public void insertString(FilterBypass bypass, int offset, String insert, AttributeSet attrSet)
	    throws BadLocationException {
	super.logInsertString(offset, insert);
	String accepted = accept(insert);
	if (accepted != null)
	    super.insertString(bypass, offset, accepted, attrSet);
    }


    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
	    AttributeSet attrs) throws BadLocationException {
	super.logReplace(offset, length, text);
	remove(fb, offset, length);
	insertString(fb, offset, text, attrs);
    }

    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
	super.logRemove(offset, length);
	charCount -= length;
	if (charCount < 0)
	    charCount = 0;
	super.remove(fb, offset, length);
    }

    private String accept(String insert) {
	int availableChars = maxCharCount - charCount;
	if (availableChars == 0)
	    return null;

	if (insert.length() <= availableChars) {
	    charCount += insert.length();
	    return insert;
	}

	charCount = maxCharCount;
	return insert.substring(0, availableChars);
    }
}
