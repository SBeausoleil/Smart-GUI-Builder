package com.sb.smartgui.swing;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.sb.smartgui.SmartFieldData;
import com.sb.smartgui.SmartPanelBuilder;
import com.sb.smartgui.SmartPanelFactory;
import com.sb.smartgui.StringFormatter;

public class BooleanPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = -2248826888439763623L;

    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory, Frame frame) {
	if (supports(fieldData.getType())) {
	    JList<Boolean> booleanOptions = new JList<>(new Boolean[] { Boolean.FALSE, Boolean.TRUE });
	    // Set base selection
	    booleanOptions.setSelectedValue(fieldData.getValue(), false);
	    // Make and add listener
	    booleanOptions.addListSelectionListener(
		    (e) -> fieldData.setValue(booleanOptions.getSelectedValue()));

	    // Make panel
	    JPanel panel = new JPanel(new FlowLayout());
	    panel.add(new JLabel(formatter.format(fieldData.getName())));
	    panel.add(booleanOptions);
	    return panel;
	}
	return null;
    }

    @Override
    public boolean supports(Class<?> type) {
	return type == boolean.class || type == Boolean.class;
    }

}
