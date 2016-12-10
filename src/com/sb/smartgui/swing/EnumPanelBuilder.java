package com.sb.smartgui.swing;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.sb.smartgui.SmartFieldData;
import com.sb.smartgui.SmartPanelBuilder;
import com.sb.smartgui.SmartPanelFactory;
import com.sb.smartgui.StringFormatter;

public class EnumPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = -4521892423149025837L;

    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory,
	    Frame frame) {
	if (supports(fieldData.getType())) {
	    Object[] constants = fieldData.getType().getEnumConstants();
	    JList options = new JList<>(constants); // TODO find a way to format the displayed names of the constants
	    // Set base selection
	    options.setSelectedValue(fieldData.getValue(), true);
	    // Make and add listener
	    options.addListSelectionListener(
		    (e) -> fieldData.setValue(options.getSelectedValue()));

	    // Make panel
	    JPanel panel = new JPanel(new FlowLayout());
	    panel.add(new JLabel(formatter.format(fieldData.getName())));
	    panel.add(options);
	    return panel;
	}
	return null;
    }

    @Override
    public boolean supports(Class<?> type) {
	return Enum.class.isAssignableFrom(type);
    }

}
