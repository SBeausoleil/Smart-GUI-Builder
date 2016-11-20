package com.sb.smartgui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConcreteErrorPanelBuilder extends ErrorPanelBuilder {

    private static final long serialVersionUID = -3131266626499998464L;
    
    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory, Frame frame) {
	JPanel panel = new JPanel(new GridLayout(2, 1));
	panel.add(new JLabel(formatter.format(fieldData.getName())));
	panel.add(new JLabel("Type: " + fieldData.getType().getName() + " is not supported."));
	return panel;
    }

}