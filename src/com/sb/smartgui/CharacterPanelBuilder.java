package com.sb.smartgui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.text.PlainDocument;

import com.sb.smartgui.SmartObjectPanel.TextFieldActionListener;
import com.sb.smartgui.filter.CharacterCountDocumentFilter;

public class CharacterPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = 1442077035869987248L;

    public static final Logger LOG = Logger.getLogger(CharacterPanelBuilder.class.getName());

    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory, Frame frame) {
	TextFieldPanel textPanel = null;
	if (supports(fieldData.getType())) {
	    // Make a text field that accepts only one character
	    textPanel = new TextFieldPanel(formatter.format(fieldData.getName()));

	    // Accept only 1 character
	    textPanel.getField().setColumns(1);
	    ((PlainDocument) textPanel.getField().getDocument()).setDocumentFilter(
		    new CharacterCountDocumentFilter(1));

	    // Set default value
	    textPanel.setText(fieldData.getValue().toString());

	    // Add a listener to update the field
	    TextFieldActionListener listener = new TextFieldActionListener(textPanel.getField(), fieldData) {

		private static final long serialVersionUID = -8670572285438479725L;

		@Override
		public void actionPerformed(ActionEvent e) {
		    if (TEXT_FIELD.getText().length() > 0)
			FIELD_DATA.setValue(TEXT_FIELD.getText().charAt(0));
		    else
			// Rewrite the textfield content to be the value of the field
			TEXT_FIELD.setText(FIELD_DATA.getValue().toString());

		}
	    };
	    textPanel.getField().addActionListener(listener);
	}
	return null;
    }

    @Override
    public boolean supports(Class<?> type) {
	return type == char.class || type == Character.class;
    }

}
