package com.sb.smartgui.filter;

import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.sb.smartgui.Sign;

public class NumberDocumentFilter extends DocumentFilter {

    private static final String NUMBER_REGEX = "[0-9]+";
    
    private boolean allowDecimal;
    private int allowedSign;

    private boolean alreadyHasDecimal;
    private boolean alreadyHasNegative;
    
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
	//super.insertString(bypass, offset, accept(insert, offset), attrSet);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
	    AttributeSet attrs) throws BadLocationException {
	if (simulateReplace(fb.getDocument().getText(0, fb.getDocument().getLength()), offset, length, text).matches(regex))
	    super.replace(fb, offset, length, text, attrs);
	/*
	removeFromDocument(fb.getDocument().getText(offset, length));
	super.replace(fb, offset, length, accept(text, offset), attrs);*/
    }

    private String simulateReplace(String text, int offset, int length, String text2) {
	
	return null;
    }

    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
	removeFromDocument(fb.getDocument().getText(offset, length));
	if (allowedSign == Sign.NEGATIVE && offset == 0) {
	    offset++;
	    length--;
	    if (length == 0)
		return; // Don't try to remove nothing
	}
	super.remove(fb, offset, length);
    }

    private String accept(String insert, int offset) { // TODO replace all of this with a regex
	char[] chars = insert.toCharArray();
	StringBuilder accepted = new StringBuilder(chars.length);
	for (int i = 0; i < insert.length(); i++) {
	    if (Character.isDigit(chars[i])) // Number
		accepted.append(chars[i]);
	    else if (chars[i] == '-' && allowNegative() && i == 0 && offset == 0 && !alreadyHasNegative) { // Negative
		alreadyHasNegative = true;
		accepted.append(chars[i]);
	    } else if (chars[i] == '.' && allowDecimal && !alreadyHasDecimal) { // Decimal
		alreadyHasDecimal = true;
		accepted.append(chars[i]);
	    }
	}
	return accepted.toString();
    }

    private void removeFromDocument(String remove) {
	char[] chars = remove.toCharArray();
	for (int i = 0; i < remove.length(); i++) {
	    if (chars[i] == '-') {
		if (allowedSign != Sign.NEGATIVE)
		    alreadyHasNegative = false;
	    } else if (chars[i] == '.')
		alreadyHasDecimal = false;
	}
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

    /**
     * Returns the alreadyIsDecimal.
     * 
     * @return the alreadyIsDecimal
     */
    public boolean alreadyHasDecimal() {
	return alreadyHasDecimal;
    }

    /**
     * Returns the alreadyIsNegative.
     * 
     * @return the alreadyIsNegative
     */
    public boolean alreadyHasNegative() {
	return alreadyHasNegative;
    }
    
    private String simulateInsert(String docContent, int offset, String insert) {
	return docContent.substring(0, offset) + insert + docContent.substring(offset);
    }

}
