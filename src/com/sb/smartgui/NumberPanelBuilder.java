package com.sb.smartgui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.sb.smartgui.SmartObjectPanel.TextFieldActionListener;

public class NumberPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = 7370192634940756206L;

    public static final Logger LOG = Logger.getLogger(NumberPanelBuilder.class.getName());

    // TODO define an annotation to denote fields that should not accept negative values
    private boolean allowNegatives;

    public NumberPanelBuilder(boolean allowNegatives) {
	this.allowNegatives = allowNegatives;
    }

    @Override
    public JPanel build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory, Frame frame) {
	LOG.fine("args: fieldData.getType() = " + fieldData.getType().getName() + ", fieldData = " + fieldData
		+ ", formatter = " + formatter
		+ ", factory = " + factory + ", frame = " + frame);
	TextFieldPanel panel = null;
	if (fieldData.getType() == float.class || fieldData.getType() == Float.class
		|| fieldData.getType() == double.class || fieldData.getType() == Float.class) {
	    panel = new TextFieldPanel(formatter.format(fieldData.getName()));
	    numberTextFieldSetting(panel, fieldData, true);
	} else if (fieldData.getType() == byte.class || fieldData.getType() == Byte.class
		|| fieldData.getType() == short.class || fieldData.getType() == Short.class
		|| fieldData.getType() == int.class || fieldData.getType() == Integer.class
		|| fieldData.getType() == long.class || fieldData.getType() == Long.class) {
	    panel = new TextFieldPanel(formatter.format(fieldData.getName()));
	    numberTextFieldSetting(panel, fieldData, false);
	}
	return panel;
    }

    private void numberTextFieldSetting(TextFieldPanel panel, IFieldData fieldData, boolean allowDecimal) {
	// Set displayed field value
	panel.setText(fieldData.getValue().toString());

	TextFields.makeNumbersOnly(panel.getField(), allowDecimal, allowNegatives);
	// Make listener
	Class fieldClass = fieldData.getType();
	TextFieldActionListener listener = new TextFieldActionListener(panel.getField(), fieldData) {
	    private static final long serialVersionUID = 5345823902622688191L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (TEXT_FIELD.getText().length() > 0) {
		    if (!allowDecimal) {
			if (fieldClass == byte.class || fieldClass == Byte.class)
			    fieldData.setValue(Byte.parseByte(TEXT_FIELD.getText()));
			else if (fieldClass == short.class || fieldClass == Short.class)
			    fieldData.setValue(Short.parseShort(TEXT_FIELD.getText()));
			else if (fieldClass == int.class || fieldClass == Integer.class)
			    fieldData.setValue(Integer.parseInt(TEXT_FIELD.getText()));
			else // long || Long
			    fieldData.setValue(Long.parseLong(TEXT_FIELD.getText()));
		    } else {
			if (fieldClass == float.class || fieldClass == Float.class)
			    fieldData.setValue(Float.parseFloat(TEXT_FIELD.getText()));
			else // double || Double
			    fieldData.setValue(Double.parseDouble(TEXT_FIELD.getText()));
		    }
		} else {
		    // Rewrite the textfield content to be the value of the field
		    TEXT_FIELD.setText(FIELD_DATA.getValue().toString());
		}
	    }
	};
	// Add listener
	panel.getField().addActionListener(listener);
    }

    /**
     * Returns the allowNegatives.
     * 
     * @return the allowNegatives
     */
    public boolean isAllowNegatives() {
	return allowNegatives;
    }

    /**
     * Sets the value of allowNegatives to that of the parameter.
     * 
     * @param allowNegatives
     *            the allowNegatives to set
     */
    public void setAllowNegatives(boolean allowNegatives) {
	this.allowNegatives = allowNegatives;
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
