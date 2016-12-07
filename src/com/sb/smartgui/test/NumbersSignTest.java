package com.sb.smartgui.test;

import javax.swing.JFrame;

import com.sb.smartgui.Sign;
import com.sb.smartgui.SmartObjectPanel;
import com.sb.smartgui.SmartPanelFactory;

public class NumbersSignTest {

    @Sign(sign = -1)
    private int negative;
    @Sign(sign = 0)
    private int anything;
    @Sign(sign = 1)
    private int positive;

    public NumbersSignTest(int negative, int anything, int positive) {
	this.negative = negative;
	this.anything = anything;
	this.positive = positive;
    }

    public static void main(String[] args) {
	JFrame frame = new JFrame("Signs test");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	SmartObjectPanel panel = SmartPanelFactory.DEFAULT_FACTORY.getSmartObjectPanel(new NumbersSignTest(-1, 0, 1),
		frame);
	frame.add(panel);
	frame.pack();
	frame.setVisible(true);
    }
}
