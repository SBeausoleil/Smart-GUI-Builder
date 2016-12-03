package com.sb.smartgui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import com.sb.smartgui.SmartObjectPanel.TextFieldActionListener;

public class StringPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = -8672669362945332484L;

    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory, Frame frame) {
	if (supports(fieldData.getType())) {
	    TextFieldPanel textPanel = new TextFieldPanel(formatter.format(fieldData.getName()));
	    // Make and add listener
	    TextFieldActionListener listener = new TextFieldActionListener(textPanel.getField(),
		    fieldData) {
		private static final long serialVersionUID = -3729683853391211653L;

		@Override
		public void actionPerformed(ActionEvent e) {
		    System.out.println("text event fired");
		    fieldData.setValue(TEXT_FIELD.getText());
		}
	    };
	    textPanel.setText((String) fieldData.getValue());
	    textPanel.getField().addActionListener(listener);
	    return textPanel;
	}
	return null;
    }

    @Override
    public boolean supports(Class<?> type) {
	return type == String.class;
    }

}
