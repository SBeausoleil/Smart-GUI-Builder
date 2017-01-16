package com.sb.smartgui;

import java.lang.annotation.Annotation;

/**
 * Data pertaining to a java object field for general wrapping.
 * 
 * @author Samuel Beausoleil
 * @param <E>
 *            the type of data that is wrapped by this FieldData
 */
public interface FieldData<E> {

    /**
     * The type of data wrapped.
     * 
     * @return the type of data wrapped within this IFieldData
     */
    public Class<E> getType();

    /**
     * The name of the wrapped data, if it has one.
     * 
     * @return the name of the wrapped data
     */
    public String getName();

    /**
     * Returns the wrapped data.
     * 
     * @return the wrapped data
     */
    public E getValue();

    /**
     * Sets the wrapped data.
     * 
     * @param value
     */
    public void setValue(E value);

    /**
     * Returns all annotations present on this element.
     * If there are no annotations, return an array of length 0.
     * 
     * @return all annotations present on this element
     */
    default Annotation[] getAnnotations() {
	return new Annotation[0];
    }
}
