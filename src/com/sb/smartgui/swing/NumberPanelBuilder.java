package com.sb.smartgui.swing;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sb.smartgui.FieldData;
import com.sb.smartgui.SmartFieldData;
import com.sb.smartgui.SmartPanelBuilder;
import com.sb.smartgui.SmartPanelFactory;
import com.sb.smartgui.StringFormatter;

public class NumberPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = 7370192634940756206L;

    private int allowedSign;

    public NumberPanelBuilder(int allowedSign) {
	this.allowedSign = allowedSign;
    }

    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory,
	    Frame frame) {
	TextFieldPanel panel = null;
	// Floating point number
	if (fieldData.getType() == float.class || fieldData.getType() == Float.class
		|| fieldData.getType() == double.class || fieldData.getType() == Float.class) {
	    panel = new TextFieldPanel(formatter.format(fieldData.getName()));
	    numberTextFieldSetting(panel, fieldData, true);
	} // Integer
	else if (fieldData.getType() == byte.class || fieldData.getType() == Byte.class
		|| fieldData.getType() == short.class || fieldData.getType() == Short.class
		|| fieldData.getType() == int.class || fieldData.getType() == Integer.class
		|| fieldData.getType() == long.class || fieldData.getType() == Long.class) {
	    panel = new TextFieldPanel(formatter.format(fieldData.getName()));
	    numberTextFieldSetting(panel, fieldData, false);
	}
	return panel;
    }

    private void numberTextFieldSetting(TextFieldPanel panel, FieldData fieldData, boolean allowDecimal) {
	// Set displayed field value
	System.out.println("fieldData: " + fieldData);
	System.out.println("fieldData.getValue(): " + fieldData.getValue());
	panel.setText(fieldData.getValue().toString()); 

	TextFields.makeNumbersOnly(panel.getField(), allowDecimal, allowedSign);
	// Make listener
	Class fieldClass = fieldData.getType();
	ActionListener listener = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (panel.getField().getText().length() > 0) {
		    if (!allowDecimal) {
			if (fieldClass == byte.class || fieldClass == Byte.class)
			    fieldData.setValue(Byte.parseByte(panel.getField().getText()));
			else if (fieldClass == short.class || fieldClass == Short.class)
			    fieldData.setValue(Short.parseShort(panel.getField().getText()));
			else if (fieldClass == int.class || fieldClass == Integer.class)
			    fieldData.setValue(Integer.parseInt(panel.getField().getText()));
			else // long || Long
			    fieldData.setValue(Long.parseLong(panel.getField().getText()));
		    } else {
			if (fieldClass == float.class || fieldClass == Float.class)
			    fieldData.setValue(Float.parseFloat(panel.getField().getText()));
			else // double || Double
			    fieldData.setValue(Double.parseDouble(panel.getField().getText()));
		    }
		} else {
		    // Rewrite the textfield content to be the value of the field
		    panel.getField().setText(fieldData.getValue().toString());
		}
	    }
	};
	// Add listener
	panel.getField().addActionListener(listener);
    }

    @Override
    public boolean supports(Class<?> type) {
	return type == float.class || type == Float.class
		|| type == double.class || type == Float.class
		|| type == byte.class || type == Byte.class
		|| type == short.class || type == Short.class
		|| type == int.class || type == Integer.class
		|| type == long.class || type == Long.class;
    }
}
