package com.sb.smartgui;

import java.awt.event.ActionListener;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import javax.swing.JTextField;

public class SmartObjectPanel<E> extends AbstractSmartPanel<E> {

    private static final long serialVersionUID = -4050248359152782160L;

    public static final Logger LOG = Logger.getLogger(SmartObjectPanel.class.getName());

    protected E target;

    protected final LinkedHashMap<Field, SmartObjectFieldData> FIELDS_MAP = new LinkedHashMap<>();

    protected SmartPanelFactory maker;

    protected SmartObjectPanel(E target, SmartPanelFactory maker) {
	this.target = target;
	this.maker = maker;
    }

    public void display(Field field) {
	SmartObjectFieldData data = FIELDS_MAP.get(field);
	if (data == null)
	    throw new IllegalArgumentException(
		    "Field \"" + field.getName() + "\" is not a member of the object targetted by this panel.");
	data.display(true);
	add(data.getPanel(), data.getIndex());
	repaint();
    }

    /**
     * Returns the fieldsMap.
     * 
     * @return the fieldsMap
     */
    public LinkedHashMap<Field, SmartObjectFieldData> getFieldsMap() {
	return FIELDS_MAP;
    }


    /**
     * Returns the target.
     * 
     * @return the target
     */
    @Override
    public E getTarget() {
	return target;
    }

    public void hide(Field field) {
	SmartObjectFieldData data = FIELDS_MAP.get(field);
	if (data == null)
	    throw new IllegalArgumentException(
		    "Field \"" + field.getName() + "\" is not a member of the object targetted by this panel.");
	data.display(false);
	data.updateIndex();
	remove(data.getPanel());
	repaint();
    }

    /**
     * Sets the value of target to that of the parameter.
     * 
     * @param target
     *            the target to set
     */
    @Override
    public void setTarget(E target) {
	this.target = target;
	for (SmartObjectFieldData field : FIELDS_MAP.values())
	    field.setFieldOwner(target);
    }


    protected static abstract class TextFieldActionListener implements ActionListener, Serializable { // IMPROVE seems archaic. See if any replacement and more elegant way exist

	private static final long serialVersionUID = 6877961412448443847L;

	protected final JTextField TEXT_FIELD;
	protected final FieldData FIELD_DATA;

	public TextFieldActionListener(JTextField textField, FieldData fieldData) {
	    this.TEXT_FIELD = textField;
	    this.FIELD_DATA = fieldData;
	}

    }

    @Override
    public SmartFieldData[] getFields() {
	return FIELDS_MAP.values().toArray(new SmartFieldData[FIELDS_MAP.size()]);
    }
}