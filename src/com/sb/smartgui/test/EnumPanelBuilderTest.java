package com.sb.smartgui.test;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import com.sb.smartgui.SimpleSmartFieldData;
import com.sb.smartgui.TitleStringFormatter;
import com.sb.smartgui.swing.EnumPanelBuilder;

public class EnumPanelBuilderTest {

    private enum SimpleEnum {
	ALPHA, BRAVO, CHARLIE
    }

    private enum ComplexEnum {
	DELTA("Delta"), ECHO("Echo"), FOXTROT("Foxtrot"), HOTEL("Hotel"), GEMINI("Gemini");
	
	private String name;
	
	ComplexEnum(String name) {
	    this.name = name;
	}
	
	public void foo() {}
    }
    
    public static void main(String[] args) {
	EnumPanelBuilder builder = new EnumPanelBuilder();
	supportTest(builder);
	generalExecutionTest(builder);
    }

    /**
     * @param builder
     */
    protected static void generalExecutionTest(EnumPanelBuilder builder) {
	SimpleSmartFieldData simpleEnum = new SimpleSmartFieldData<>(SimpleEnum.class, "SimpleEnum", SimpleEnum.ALPHA, null,
		null, null);
	Container simplePanel = builder.build(simpleEnum, new TitleStringFormatter(), null, null);

	SimpleSmartFieldData complexEnum = new SimpleSmartFieldData<>(ComplexEnum.class, "ComplexEnum", ComplexEnum.GEMINI, null, null, null);
	Container complexPanel = builder.build(complexEnum, new TitleStringFormatter(), null, null);
	
	JFrame frame = new JFrame("Enum test");
	frame.setLayout(new FlowLayout());
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.add(simplePanel);
	frame.add(complexPanel);
	frame.pack();
	frame.setVisible(true);
    }

    protected static void supportTest(EnumPanelBuilder builder) {
	SimpleEnum simpleValue = SimpleEnum.BRAVO;
	System.out.println("SimpleEnum: " + builder.supports(simpleValue.getClass()));
	
	ComplexEnum complexValue = ComplexEnum.FOXTROT;
	System.out.println("ComplexEnum: " + builder.supports(complexValue.getClass()));
    }
}
