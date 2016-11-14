package com.sb.smartgui.filter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberDocumentFilter extends DocumentFilter {

    private boolean allowDecimal;
    private boolean allowNegative;

    private boolean alreadyHasDecimal;
    private boolean alreadyHasNegative;

    public NumberDocumentFilter(boolean allowDecimal, boolean allowNegative) {
	this.allowDecimal = allowDecimal;
	this.allowNegative = allowNegative;
    }

    @Override
    public void insertString(FilterBypass bypass, int offset, String insert, AttributeSet attrSet)
	    throws BadLocationException {
	super.insertString(bypass, offset, accept(insert, offset), attrSet);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
	    AttributeSet attrs) throws BadLocationException {
	removeFromDocument(fb.getDocument().getText(offset, length));
	super.replace(fb, offset, length, accept(text, offset), attrs);
    }

    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
	removeFromDocument(fb.getDocument().getText(offset, length));
	super.remove(fb, offset, length);
    }

    private String accept(String insert, int offset) {
	char[] chars = insert.toCharArray();
	StringBuilder accepted = new StringBuilder(chars.length);
	for (int i = 0; i < insert.length(); i++) {
	    if (Character.isDigit(chars[i])) // Number
		accepted.append(chars[i]);
	    else if (chars[i] == '-' && allowNegative && i == 0 && offset == 0 && !alreadyHasNegative) { // Negative
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
	    if (chars[i] == '-')
		alreadyHasNegative = false;
	    else if (chars[i] == '.')
		alreadyHasDecimal = false;
	}
    }

    /**
     * Returns the allowNegative.
     * 
     * @return the allowNegative
     */
    public boolean isAllowNegative() {
	return allowNegative;
    }

    /**
     * Sets the value of allowNegative to that of the parameter.
     * 
     * @param allowNegative
     *            the allowNegative to set
     */
    public void setAllowNegative(boolean allowNegative) {
	this.allowNegative = allowNegative;
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

}
