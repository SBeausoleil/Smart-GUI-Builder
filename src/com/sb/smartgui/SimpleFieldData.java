package com.sb.smartgui;

import java.lang.annotation.Annotation;

public class SimpleFieldData<E> implements FieldData<E> {

    private final Class<E> TYPE;
    private String name;
    protected E value;
    private Annotation[] annotations;
    
    public SimpleFieldData(Class<E> type, String name, E value, Annotation[] annotations) {
	this.TYPE = type;
	this.name = name;
	this.value = value;
	setAnnotations(annotations);
    }

    @Override
    public Class<E> getType() {
	return TYPE;
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

    @Override
    public Annotation[] getAnnotations() {
	return annotations;
    }

    public final void setAnnotations(Annotation[] annotations) {
	if (annotations == null)
	    annotations = new Annotation[0];
	this.annotations = annotations;
    }
}
