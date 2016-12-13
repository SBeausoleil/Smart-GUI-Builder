package com.sb.smartgui.swing;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sb.smartgui.ClassUtil;
import com.sb.smartgui.SmartFieldData;
import com.sb.smartgui.SmartPanelBuilder;
import com.sb.smartgui.SmartPanelFactory;
import com.sb.smartgui.StringFormatter;

public class IterablePanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = -2024928138235784224L;

    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory,
	    Frame frame) {
	if (supports(fieldData.getType())) {
	    JPanel panel = new JPanel(new FlowLayout());
	    panel.add(new JLabel(fieldData.getName()));
	    return panel;
	}
	return null;
    }

    @Override
    public boolean supports(Class<?> type) {
	return ClassUtil.instanceOf(type, Iterable.class);
    }

}
