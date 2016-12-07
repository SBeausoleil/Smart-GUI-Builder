package com.sb.smartgui.swing;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.text.PlainDocument;

import com.sb.smartgui.SmartFieldData;
import com.sb.smartgui.SmartPanelBuilder;
import com.sb.smartgui.SmartPanelFactory;
import com.sb.smartgui.StringFormatter;
import com.sb.smartgui.filter.CharacterCountDocumentFilter;

public class CharacterPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = 1442077035869987248L;

    public static final Logger LOG = Logger.getLogger(CharacterPanelBuilder.class.getName());

    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory, Frame frame) {
	if (supports(fieldData.getType())) {
	    // Make a text field that accepts only one character
	    final TextFieldPanel TEXT_PANEL = new TextFieldPanel(formatter.format(fieldData.getName()));

	    // Accept only 1 character
	    TEXT_PANEL.getField().setColumns(1);
	    ((PlainDocument) TEXT_PANEL.getField().getDocument()).setDocumentFilter(
		    new CharacterCountDocumentFilter(1));

	    // Set default value
	    TEXT_PANEL.setText(fieldData.getValue().toString());

	    // Add a listener to update the field
	    ActionListener listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    if (TEXT_PANEL.getField().getText().length() > 0)
			fieldData.setValue(TEXT_PANEL.getField().getText().charAt(0));
		    else
			// Rewrite the textfield content to be the value of the field
			TEXT_PANEL.getField().setText(fieldData.getValue().toString());

		}
	    };
	    TEXT_PANEL.getField().addActionListener(listener);
	}
	return null;
    }

    @Override
    public boolean supports(Class<?> type) {
	return type == char.class || type == Character.class;
    }

}
