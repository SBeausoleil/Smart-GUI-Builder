package com.sb.smartgui;

import java.io.Serializable;
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
    protected T target;
    
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
     * The panel that represents that field within the UI.
     */
    protected JPanel panel;
    
    /**
     * Is used in case the target is a non-primitive/non-String Object.
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

    public SmartObjectFieldData(T target, Field field, JPanel panel) {
	this(target, field, panel, null);
    }

    public SmartObjectFieldData(T target, Field field, JPanel panel, Method setter) {
	this.target = target;
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
    public JPanel getPanel() {
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
     * Returns the target.
     *
     * @return the target
     */
    public T getTarget() {
	return target;
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
    public void setPanel(JPanel panel) {
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

    public void setTarget(T target) {
	this.target = target;
	if (innerPanel != null)
	    innerPanel.setTarget(target);
    }

    public void update(Object obj) {
	try {
	    if (setter != null)
		setter.invoke(target, obj);
	    else
		FIELD.set(target, obj);
	} catch (InvocationTargetException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public int updateIndex() {
	return this.index = Components.getComponentIndex(panel);
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
	    return (E) FIELD.get(target);
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void setValue(E value) {
	update(value);
    }
}