package com.sb.smartgui.test;

import javax.swing.JFrame;

import com.sb.smartgui.ExecutionListener;
import com.sb.smartgui.SmartConstructorPanel;
import com.sb.smartgui.SmartPanelFactory;

public class TestConstructorPanel {
    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
	JFrame frame = new JFrame("Smart Method test");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	SmartConstructorPanel methodPanel = SmartPanelFactory.DEFAULT_FACTORY.getSmartConstructorPanel(
		TestRootObject.class.getConstructor(int.class, char.class, boolean.class, String.class, Integer.class,
			Character.class, Boolean.class, TestChildrenObject.class),
		frame);
	
	methodPanel.addExecutionListener(new ExecutionListener() {
	    
	    @Override
	    public void methodInvoked(Object returnValue) {
		System.out.println("The method has exited with the return value: " + returnValue);
	    }
	});
	
	frame.add(methodPanel);
	frame.pack();
	frame.setVisible(true);
    }
}
