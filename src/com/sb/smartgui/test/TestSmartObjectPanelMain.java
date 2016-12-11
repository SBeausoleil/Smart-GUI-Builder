package com.sb.smartgui.test;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sb.smartgui.SmartObjectPanel;
import com.sb.smartgui.SmartPanelFactory;

public class TestSmartObjectPanelMain {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
	// Set logging priority
	/*ConsoleHandler handler = new ConsoleHandler();
	handler.setLevel(Level.FINEST);
	PackageLogger.PACKAGE_LOGGER.addHandler(handler);
	PackageLogger.PACKAGE_LOGGER.setLevel(Level.FINEST);*/
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	
	// Test
	TestRootObject obj = new TestRootObject(0, 'a', true, "Test", 1, 'a', true,
		new TestChildrenObject("My name", 160));

	JFrame frame = new JFrame("Smart GUI test");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	SmartObjectPanel<TestRootObject> panel = SmartPanelFactory.DEFAULT_FACTORY.getSmartObjectPanel(obj, frame);
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	frame.add(panel);
	frame.pack();
	frame.setVisible(true);
    }
}
