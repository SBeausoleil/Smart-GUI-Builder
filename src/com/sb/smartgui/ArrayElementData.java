package com.sb.smartgui;

import java.lang.reflect.Array;

public class ArrayElementData<E> implements FieldData<E> {

    private Object array;
    private int index;

    private String name;

    public ArrayElementData(Object array, int index, String name) {
	if (!array.getClass().isArray())
	    throw new IllegalArgumentException("Received object is not an array");
	testType(array);

	this.array = array;
	this.index = index;
	this.name = name;
    }

    /**
     * Tests the type of an array against the generic type of this ArrayElementData.
     */
    protected void testType(Object array) {
	try {
	    @SuppressWarnings("unused")
	    Class<E> x = (Class<E>) array.getClass().getComponentType(); // This will throw an exception if the generic E type is not the type of the received array
	} catch (ClassCastException e) {
	    throw new IllegalArgumentException(
		    "The received array's component type does not match the generic type of this ArrayElementData");
	}
    }

    @Override
    public Class<E> getType() {
	return (Class<E>) array.getClass().getComponentType();
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public E getValue() {
	return (E) Array.get(array, index);
    }

    @Override
    public void setValue(E value) {
	Array.set(array, index, value);
    }

    /**
     * Returns the array.
     * 
     * @return the array
     */
    public Object getArray() {
	return array;
    }

    /**
     * Sets the value of array to that of the parameter.
     * 
     * @param array
     *            the array to set
     */
    public void setArray(Object array) {
	testType(array);
	this.array = array;
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
     * Sets the value of index to that of the parameter.
     * 
     * @param index
     *            the index to set
     */
    public void setIndex(int index) {
	this.index = index;
    }

    /**
     * Sets the value of name to that of the parameter.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

}
