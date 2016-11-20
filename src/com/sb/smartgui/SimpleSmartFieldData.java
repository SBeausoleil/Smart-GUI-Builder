package com.sb.smartgui;

import java.awt.Container;

public class SimpleSmartFieldData<E> extends SimpleFieldData<E> implements SmartFieldData<E> {

    protected Container ownerPanel;
    protected Container panel;
    protected AbstractSmartPanel<?> innerPanel;
    protected boolean display;
    protected int index;

    public SimpleSmartFieldData(Class<E> type, String name, E value, Container ownerContainer, Container panel) {
	this(type, name, value, ownerContainer, panel, null, true);
    }

    public SimpleSmartFieldData(Class<E> type, String name, E value, Container ownerPanel, Container panel,
	    AbstractSmartPanel<?> innerPanel, boolean display) {
	super(type, name, value);
	this.ownerPanel = ownerPanel;
	this.panel = panel;
	this.innerPanel = innerPanel;
	this.display = display;
	updateIndex();
    }

    @Override
    public void display(boolean display) {
	this.display = display;
    }

    @Override
    public int getIndex() {
	return index;
    }

    @Override
    public AbstractSmartPanel<?> getInnerPanel() {
	return innerPanel;
    }

    @Override
    public Container getOwnerContainer() {
	return ownerPanel;
    }

    @Override
    public Container getPanel() {
	return panel;
    }

    @Override
    public boolean isDisplayed() {
	return display;
    }

    @Override
    public void setIndex(int index) {
	this.index = index;
    }

    @Override
    public void setInnerPanel(AbstractSmartPanel<?> innerPanel) {
	this.innerPanel = innerPanel;
    }

    @Override
    public void setOwnerContainer(Container ownerPanel) {
	this.ownerPanel = ownerPanel;
    }

    @Override
    public void setPanel(Container panel) {
	this.panel = panel;
    }
}
