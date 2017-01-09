package com.sb.smartgui.swing;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.lang.reflect.Array;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sb.smartgui.ArrayElementData;
import com.sb.smartgui.SmartFieldData;
import com.sb.smartgui.SmartFieldDataDecorator;
import com.sb.smartgui.SmartPanelBuilder;
import com.sb.smartgui.SmartPanelFactory;
import com.sb.smartgui.StringFormatter;

/**
 * A SmartPanelBuilder which generates Containers for the arrays.
 * 
 * @author Samuel Beausoleil
 */
public class ArrayPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = -2024928138235784224L;

    // TESTME
    // IMPROVE Consider building some sort of SmartArrayPanel to allow fine control over the produced panel
    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory,
	    Frame frame) {
	if (supports(fieldData.getType())) {
	    JPanel panel = new JPanel(new FlowLayout());
	    panel.add(new JLabel(fieldData.getName()));

	    int length = Array.getLength(fieldData.getValue());
	    for (int i = 0; i < length; i++) {
		ArrayElementData<?> data = new ArrayElementData<>(fieldData.getValue(), i, Integer.toString(i)); // IMPROVE Replace the name (i) by an actual name. Find some way to do that.
		SmartFieldDataDecorator<?, ArrayElementData> smartData = new SmartFieldDataDecorator<>(data);
		factory.generatePanel(frame, smartData, data.getType());
		panel.add(smartData.getPanel());
	    }
	    return panel;
	}
	return null;
    }

    @Override
    public boolean supports(Class<?> type) {
	return type.isArray();
    }

}
