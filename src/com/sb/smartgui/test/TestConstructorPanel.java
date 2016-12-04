package com.sb.smartgui.test;

import javax.swing.JFrame;

import com.sb.smartgui.SmartMethodPanel;
import com.sb.smartgui.SmartPanelFactory;

public class TestConstructorPanel {
    public static void main(String[] args) {
	JFrame frame = new JFrame("Smart Method test");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	//SmartConstructorPanel methodPanel = SmartPanelFactory.DEFAULT_FACTORY.getSmartMethodPanel(TestRootObject.class, TestRootObject.class.getConstructor(int.class, char.class, boolean.class, String.class, Integer.class, Character.class, Boolean.class, TestChildrenObject.class), frame)
    }
}
