package com.sb.smartgui.filter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.sb.smartgui.Sign;

public class NumberDocumentFilter extends DocumentFilter {

    private static final String NUMBER_REGEX = "[0-9]+";

    private boolean allowDecimal;
    private int allowedSign;

    private String regex;

    public NumberDocumentFilter(boolean allowDecimal, int allowedSign) {
	this.allowDecimal = allowDecimal;
	this.allowedSign = allowedSign;
	regex = NUMBER_REGEX;
	if (allowDecimal)
	    regex += "(\\." + NUMBER_REGEX + ")?";
	if (allowedSign == Sign.NEUTRAL)
	    regex = "-?" + regex;
	else if (allowedSign <= Sign.NEGATIVE)
	    regex = "-" + regex;
	regex = "^" + regex + "$";
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String insert, AttributeSet attrSet)
	    throws BadLocationException {
	if (simulateInsert(fb.getDocument().getText(0, fb.getDocument().getLength()), offset, insert).matches(regex))
	    super.insertString(fb, offset, insert, attrSet);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
	    AttributeSet attrs) throws BadLocationException {
	if (simulateReplace(fb.getDocument().getText(0, fb.getDocument().getLength()), offset, length, text).matches(
		regex))
	    super.replace(fb, offset, length, text, attrs);
    }

    private String simulateReplace(String text, int offset, int length, String replaceWith) {
	String preReplace = text.substring(0, offset);
	String afterReplace = offset + length >= text.length() ? "" : text.substring(offset + length);
	return preReplace + replaceWith + afterReplace;
    }

    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
	if (simulateRemove(fb.getDocument().getText(0, fb.getDocument().getLength()), offset, length).matches(regex))
	super.remove(fb, offset, length);
    }

    private String simulateRemove(String text, int offset, int length) {
	String preRemove = text.substring(0, offset);
	String postRemove = offset + length >= text.length() ? "" : text.substring(offset + length);
	return preRemove + postRemove;
    }

    /**
     * Returns the allowNegative.
     * 
     * @return the allowNegative
     */
    public boolean allowNegative() {
	return allowedSign == Sign.NEGATIVE || allowedSign == Sign.NEUTRAL;
    }

    /**
     * Returns the allowDecimal.
     * 
     * @return the allowDecimal
     */
    public boolean isAllowDecimal() {
	return allowDecimal;
    }

    /**
     * Sets the value of allowDecimal to that of the parameter.
     * 
     * @param allowDecimal
     *            the allowDecimal to set
     */
    public void setAllowDecimal(boolean allowDecimal) {
	this.allowDecimal = allowDecimal;
    }

    private String simulateInsert(String docContent, int offset, String insert) {
	return docContent.substring(0, offset) + insert + docContent.substring(offset);
    }

}
