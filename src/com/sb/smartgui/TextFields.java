package com.sb.smartgui;

import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import com.sb.smartgui.filter.NumberDocumentFilter;

public class TextFields {

    // Do not instantiate
    private TextFields() {}

    public static void makeNumbersOnly(JTextField field, boolean allowDecimal, boolean allowNegative) {
	((PlainDocument) field.getDocument()).setDocumentFilter(new NumberDocumentFilter(allowDecimal, allowNegative));
    }

}
