package com.sb.smartgui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class SmartMethodPanel<E, T> extends AbstractSmartPanel<E> {

    private static final long serialVersionUID = 1500940940681907779L;

    public static final Logger LOG = Logger.getLogger(SmartMethodPanel.class.getName());

    private final Method METHOD;

    /**
     * Array of all the adjoining data of the
     */
    private SmartFieldData<?>[] parameters;

    /**
     * The object on which to invoke the function
     */
    private T methodInvocationTarget;

    public SmartMethodPanel(Method method, T methodInvocationTarget, SmartFieldData<?>[] parameters) {
	this.METHOD = method;
	this.methodInvocationTarget = methodInvocationTarget;
	this.parameters = parameters;
    }

    @Override
    public E getTarget() {
	try {
	    return (E) METHOD.invoke(methodInvocationTarget, getAllParameters());
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	    throw new RuntimeException(e);
	}
    }

    public Object[] getAllParameters() {
	Object[] params = new Object[parameters.length];
	for (int i = 0; i < params.length; i++)
	    params[i] = parameters[i].getValue();
	return params;
    }

    /**
     * Does nothing.
     */
    @Override
    public void setTarget(E target) {}

    @Override
    public SmartFieldData[] getFields() {
	return parameters;
    }
}
