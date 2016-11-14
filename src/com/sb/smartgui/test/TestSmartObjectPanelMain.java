package com.sb.smartgui.test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import javax.swing.JFrame;

import com.sb.smartgui.PackageLogger;
import com.sb.smartgui.SmartObjectPanel;
import com.sb.smartgui.SmartPanelFactory;

public class TestSmartObjectPanelMain {
     public static void main(String[] args) {
	 // Set logging priority
	 ConsoleHandler handler = new ConsoleHandler();
	 handler.setLevel(Level.FINEST);
	 PackageLogger.PACKAGE_LOGGER.addHandler(handler);
	 PackageLogger.PACKAGE_LOGGER.setLevel(Level.FINEST);
	 
	 // Test
	 TestRootObject obj = new TestRootObject(0, 'a', true, "Test", 1, 'a', true, new TestChildrenObject("My name", 160));
	 
	JFrame frame = new JFrame("Smart GUI test");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	SmartObjectPanel<TestRootObject> panel = SmartPanelFactory.DEFAULT_FACTORY.getSmartObjectPanel(obj, frame);
	frame.setSize(800, 600);
	frame.add(panel);
	frame.setVisible(true);
    }
}
