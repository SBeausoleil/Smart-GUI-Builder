package com.sb.smartgui.test;

import java.lang.reflect.Method;

import javax.swing.JFrame;

import com.sb.smartgui.MethodInvocationListener;
import com.sb.smartgui.SmartMethodPanel;
import com.sb.smartgui.SmartPanelFactory;

// TODO
public class TestSmartMethodPanelMain {

    public static void main(String[] args) {
	JFrame frame = new JFrame("Smart Method test");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	SmartMethodPanel methodPanel;
	TestRootObject obj = new TestRootObject(0, 'a', false, null, null, null, null, null);
	Method method = null;
	try {
	    method = obj.getClass().getMethod("testMethod", String.class);
	} catch (NoSuchMethodException | SecurityException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
	methodPanel = SmartPanelFactory.DEFAULT_FACTORY.getSmartMethodPanel(obj, method, frame);
	methodPanel.addMethodInvocationListener(new MethodInvocationListener() {
	    
	    @Override
	    public void methodInvoked(Object methodReturn) {
		System.out.println("The method has exited with the return value: " + methodReturn);
	    }
	});
	
	frame.add(methodPanel);
	frame.pack();
	frame.setVisible(true);
    } 

}
