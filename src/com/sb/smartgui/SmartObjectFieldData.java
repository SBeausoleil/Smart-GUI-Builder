package com.sb.smartgui;

import java.awt.Container;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JPanel;

/**
 * Data relating to a field and it's display within a smart component.
 * This specific implementation works specifically for usage with the Field class.
 *
 * @author Samuel Beausoleil
 * @param <E>
 *            The data type held
 * @param <T>
 *            The object type owning the field.
 */
public class SmartObjectFieldData<E, T> implements Serializable, SmartFieldData<E> {
    private static final long serialVersionUID = 8272692292592395946L;

    /**
     * The object owning the Field.
     */
    protected T fieldOwner;

    /**
     * The Field that is controlled by this FieldData
     */
    protected Field FIELD;

    /**
     * An optional setter method to use when modifying the field.
     * May be null.
     */
    protected Method setter;

    /**
     * The panel that is currently displaying this SmartObjectFieldData.
     * Does not need to be set, however some functionalities of this class will not be functional if
     * it is null.
     */
    protected Container ownerPanel;

    /**
     * The panel that represents that field within the UI.
     */
    protected Container panel;

    /**
     * Is used in case the field's value is a non-primitive/non-String Object.
     */
    protected AbstractSmartPanel<T> innerPanel;

    /**
     * Wether that field's panel is currently displayed or not.
     */
    protected boolean display;

    /**
     * The index of that field's panel within the UI.
     * Used when restoring the display to true so that it goes back to it's original place.
     */
    protected int index;

    public SmartObjectFieldData(T fieldOwner, Field field, JPanel panel) {
	this(fieldOwner, field, panel, null);
    }

    public SmartObjectFieldData(T fieldOwner, Field field, JPanel panel, Method setter) {
	this.fieldOwner = fieldOwner;
	this.FIELD = field;
	this.panel = panel;
	this.setter = setter;
	this.display = true;
	updateIndex();
    }

    /**
     * Returns the field.
     *
     * @return the field
     */
    public Field getField() {
	return FIELD;
    }

    /**
     * Returns the index.
     *
     * @return the index
     */
    @Override
    public int getIndex() {
	return index;
    }

    /**
     * Returns the innerPanel.
     *
     * @return the innerPanel
     */
    @Override
    public AbstractSmartPanel<?> getInnerPanel() {
	return innerPanel;
    }

    /**
     * Returns the panel.
     *
     * @return the panel
     */
    @Override
    public Container getPanel() {
	return panel;
    }

    /**
     * Returns the setter.
     *
     * @return the setter
     */
    public Method getSetter() {
	return setter;
    }

    /**
     * Returns the object owning the field.
     *
     * @return the owner of the field
     */
    public T getFieldOwner() {
	return fieldOwner;
    }

    /**
     * Returns the display.
     *
     * @return the display
     */
    @Override
    public boolean isDisplayed() {
	return display;
    }

    /**
     * Sets the value of display to that of the parameter.
     *
     * @param display
     *            the display to set
     */
    @Override
    public void display(boolean display) {
	this.display = display;
    }

    /**
     * Sets the value of innerPanel to that of the parameter.
     *
     * @param innerPanel
     *            the innerPanel to set
     */
    @Override
    public void setInnerPanel(AbstractSmartPanel<?> innerPanel) {
	this.innerPanel = (AbstractSmartPanel<T>) innerPanel;
    }

    /**
     * Sets the value of panel to that of the parameter.
     *
     * @param panel
     *            the panel to set
     */
    @Override
    public void setPanel(Container panel) {
	this.panel = panel;
	updateIndex();
    }

    /**
     * Sets the value of setter to that of the parameter.
     *
     * @param setter
     *            the setter to set
     */
    public void setSetter(Method setter) {
	this.setter = setter;
    }

    /**
     * Sets the fieldOwner to that of the parameter.
     * 
     * @param fieldOwner
     */
    public void setFieldOwner(T fieldOwner) {
	this.fieldOwner = fieldOwner;
	if (innerPanel != null)
	    innerPanel.setTarget(this.fieldOwner);
    }

    /**
     * Updates the current state of the field value.
     * Will try to set the field's value by reflection or if possible, via a setter method.
     * 
     * @param obj
     *            the new value of the field
     */
    public void updateField(Object obj) {
	try {
	    if (setter != null)
		setter.invoke(fieldOwner, obj);
	    else
		FIELD.set(fieldOwner, obj);
	} catch (InvocationTargetException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public Class<E> getType() {
	return (Class<E>) FIELD.getType();
    }

    @Override
    public String getName() {
	return FIELD.getName();
    }

    @Override
    public E getValue() {
	try {
	    return (E) FIELD.get(fieldOwner);
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void setValue(E value) {
	updateField(value);
    }

    @Override
    public Container getOwnerContainer() {
	return ownerPanel;
    }

    @Override
    public void setOwnerContainer(Container ownerPanel) {
	this.ownerPanel = ownerPanel;
    }

    @Override
    public void setIndex(int index) {
	this.index = index;
    }

    @Override
    public Annotation[] getAnnotations() {
	return FIELD.getAnnotations();
    }
}