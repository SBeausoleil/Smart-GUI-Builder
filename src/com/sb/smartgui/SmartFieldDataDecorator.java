package com.sb.smartgui;

import java.awt.Container;
import java.lang.annotation.Annotation;

/**
 * A simple decorator for a FieldData to allow to quickly and simply extend the capabilities of a
 * FieldData to those of a SmartFieldData.
 * 
 * @author Samuel Beausoleil
 * @param <E>
 *            the type of data held by the decorated FieldData
 * @param <T> the type of FieldData instance that is decorated by this object
 */
public class SmartFieldDataDecorator<E, T extends FieldData<E>> implements SmartFieldData<E> {

    private T fieldData;

    protected Container ownerContainer;
    protected Container panel;
    protected AbstractSmartPanel<?> innerPanel;
    protected boolean visible;
    protected int index;

    public SmartFieldDataDecorator(T fieldData) {
	this.fieldData = fieldData;
    }

    @Override
    public Class<E> getType() {
	return fieldData.getType();
    }

    @Override
    public String getName() {
	return fieldData.getName();
    }

    @Override
    public E getValue() {
	return fieldData.getValue();
    }

    @Override
    public void setValue(E value) {
	fieldData.setValue(value);
    }

    @Override
    public Annotation[] getAnnotations() {
	return fieldData.getAnnotations();
    }

    @Override
    public Container getOwnerContainer() {
	return ownerContainer;
    }

    @Override
    public void setOwnerContainer(Container ownerContainer) {
	this.ownerContainer = ownerContainer;
    }

    @Override
    public Container getPanel() {
	return panel;
    }

    @Override
    public void setPanel(Container panel) {
	this.panel = panel;
    }

    @Override
    public AbstractSmartPanel<?> getInnerPanel() {
	return innerPanel;
    }

    @Override
    public void setInnerPanel(AbstractSmartPanel<?> innerPanel) {
	this.innerPanel = innerPanel;
    }

    @Override
    public boolean isVisible() {
	return visible;
    }

    @Override
    public void visible(boolean visible) {
	this.visible = visible;
	panel.setVisible(visible);
    }

    @Override
    public int getIndex() {
	return index;
    }

    @Override
    public void setIndex(int index) {
	this.index = index;
    }

    /**
     * Returns the fieldData.
     * @return the fieldData
     */
    public T getFieldData() {
        return fieldData;
    }

    /**
     * Sets the value of fieldData to that of the parameter.
     * @param fieldData the fieldData to set
     */
    public void setFieldData(T fieldData) {
        this.fieldData = fieldData;
    }

}
