package com.sb.smartgui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * An InvocationPanel used specifically for constructing objects,
 * 
 * @author Samuel Beausoleil
 * @param <E>
 *            the type of constructed Object
 */
public class SmartConstructorPanel<E> extends ExecutablePanel<E> {

    private static final long serialVersionUID = -1695399242177681851L;

    protected final Constructor<E> CONSTRUCTOR;

    public SmartConstructorPanel(Constructor<E> constructor) {
	super();
	CONSTRUCTOR = constructor;
    }

    @Override
    public E getTarget() {
	try {
	    return CONSTRUCTOR.newInstance(getAllParameters());
	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
		| InvocationTargetException e) {
	    throw new RuntimeException(e);
	}
    }

    public Constructor<E> getConstructor() {
	return CONSTRUCTOR;
    }
}
