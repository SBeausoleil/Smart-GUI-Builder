package com.sb.smartgui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConcreteErrorPanelBuilder extends ErrorPanelBuilder {

    private static final long serialVersionUID = -3131266626499998464L;
    
    public static final Logger LOG = Logger.getLogger(ConcreteErrorPanelBuilder.class.getName());

    @Override
    public JPanel build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory, Frame frame) {
	LOG.fine("args: type = " + fieldData.getType().getName() + ", fieldData = " + fieldData + ", formatter = " + formatter
		+ ", factory = " + factory + ", frame = " + frame);
	
	JPanel panel = new JPanel(new GridLayout(2, 1));
	panel.add(new JLabel(formatter.format(fieldData.getName())));
	panel.add(new JLabel("Type: " + fieldData.getType().getName() + " is not supported."));
	return panel;
    }

}