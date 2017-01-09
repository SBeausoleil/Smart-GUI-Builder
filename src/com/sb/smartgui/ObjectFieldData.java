package com.sb.smartgui;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
public class ObjectFieldData<E, T> implements Serializable, FieldData<E> {
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

    public ObjectFieldData(T fieldOwner, Field field, JPanel panel) {
	this(fieldOwner, field, panel, null);
    }

    public ObjectFieldData(T fieldOwner, Field field, JPanel panel, Method setter) {
	this.fieldOwner = fieldOwner;
	this.FIELD = field;
	this.setter = setter;
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
     * Sets the value of setter to that of the parameter.
     *
     * @param setter
     *            the setter to set
     */
    public void setSetter(Method setter) {
	this.setter = setter;
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
	if (!Modifier.isFinal(FIELD.getModifiers())) {
	    try {
		if (setter != null)
		    setter.invoke(fieldOwner, value);
		else
		    FIELD.set(fieldOwner, value);
	    } catch (InvocationTargetException | IllegalAccessException e) {
		throw new RuntimeException(e);
	    }
	}
    }

    @Override
    public Annotation[] getAnnotations() {
	return FIELD.getAnnotations();
    }

    /**
     * Sets the value of fieldOwner to that of the parameter.
     * 
     * @param fieldOwner
     *            the fieldOwner to set
     */
    public void setFieldOwner(T fieldOwner) {
	this.fieldOwner = fieldOwner;
    }
}