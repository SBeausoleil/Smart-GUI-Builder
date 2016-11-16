package com.sb.smartgui;

public class SimpleFieldData<E> implements FieldData<E> {

    private Class<E> type;
    private String name;
    protected E value;
    
    public SimpleFieldData(Class<E> type, String name, E value) {
	this.type = type;
	this.name = name;
	this.value = value;
    }

    @Override
    public Class<E> getType() {
	return type;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public E getValue() {
	return value;
    }

    @Override
    public void setValue(E value) {
	this.value = value;
    }

}
