package com.sb.smartgui.swing;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextFieldPanel extends JPanel {

    private static final long serialVersionUID = -6597002282329859246L;
    
    private JLabel fieldName;
    private JTextField field;
    
    public TextFieldPanel(String fieldName) {
	this(fieldName, null);
    }
    
    public TextFieldPanel(String fieldName, ActionListener actionListener) {
	super(new FlowLayout());
	this.fieldName = new JLabel(fieldName);
	field = new JTextField();

	if (actionListener != null)
	    field.addActionListener(actionListener);
	
	this.add(this.fieldName);
	this.add(this.field);
    }

    /**
     * Returns the fieldName.
     * @return the fieldName
     */
    public JLabel getFieldName() {
        return fieldName;
    }

    /**
     * Sets the value of fieldName to that of the parameter.
     * @param fieldName the fieldName to set
     */
    public void setFieldName(JLabel fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Returns the field.
     * @return the field
     */
    public JTextField getField() {
        return field;
    }

    /**
     * Sets the value of field to that of the parameter.
     * @param field the field to set
     */
    public void setField(JTextField field) {
        this.field = field;
    }

    public String getText() {
	return field.getText();
    }
    
    public void setText(String text) {
	field.setText(text);
    }
}
