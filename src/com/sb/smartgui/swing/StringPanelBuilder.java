package com.sb.smartgui.swing;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sb.smartgui.SmartFieldData;
import com.sb.smartgui.SmartPanelBuilder;
import com.sb.smartgui.SmartPanelFactory;
import com.sb.smartgui.StringFormatter;

public class StringPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = -8672669362945332484L;

    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory, Frame frame) {
	if (supports(fieldData.getType())) {
	    final TextFieldPanel TEXT_PANEL = new TextFieldPanel(formatter.format(fieldData.getName()));
	    // Make and add listener
	    ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    fieldData.setValue(TEXT_PANEL.getField().getText());
		}
	    };
	    TEXT_PANEL.setText((String) fieldData.getValue());
	    TEXT_PANEL.getField().addActionListener(listener);
	    return TEXT_PANEL;
	}
	return null;
    }

    @Override
    public boolean supports(Class<?> type) {
	return type == String.class;
    }

}
