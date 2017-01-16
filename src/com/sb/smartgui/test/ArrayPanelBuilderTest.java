package com.sb.smartgui.test;

import java.awt.Container;

import javax.swing.JFrame;

import com.sb.smartgui.FieldData;
import com.sb.smartgui.SimpleFieldData;
import com.sb.smartgui.SmartFieldDataDecorator;
import com.sb.smartgui.SmartPanelFactory;
import com.sb.smartgui.TitleStringFormatter;
import com.sb.smartgui.swing.ArrayPanelBuilder;

public class ArrayPanelBuilderTest {

    public static void main(String[] args) {
	ArrayPanelBuilder builder = new ArrayPanelBuilder();
	int[] numbers = new int[] {2, 5, 6, 9};
	FieldData fieldData = new SimpleFieldData(numbers.getClass(), "numbers", numbers, null);
	JFrame frame = new JFrame();
	Container arrayPanel = builder.build(new SmartFieldDataDecorator(fieldData), new TitleStringFormatter(), SmartPanelFactory.DEFAULT_FACTORY, frame);
	
	frame.add(arrayPanel);
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }

}
