package com.sb.smartgui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class BooleanPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = -2248826888439763623L;

    public static final Logger LOG = Logger.getLogger(BooleanPanelBuilder.class.getName());

    @Override
    public JPanel build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory, Frame frame) {

	LOG.fine("args: type = " + fieldData.getType().getName() + ", fieldData = " + fieldData + ", formatter = " + formatter
		+ ", factory = " + factory + ", frame = " + frame);

	if (supports(fieldData.getType())) {
	    JList<Boolean> booleanOptions = new JList<>(new Boolean[] { Boolean.FALSE, Boolean.TRUE });
	    // Set base selection
	    booleanOptions.setSelectedValue(fieldData.getValue(), false);
	    // Make and add listener
	    booleanOptions.addListSelectionListener(
		    (e) -> fieldData.setValue(booleanOptions.getSelectedValue()));

	    // Make panel
	    JPanel panel = new JPanel(new GridLayout(1, 2));
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
