package com.sb.smartgui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.IdentityHashMap;
import java.util.logging.Logger;

/**
 * A SmartPanel with the execution of a method as it's goal.
 * 
 * @author Samuel Beausoleil
 * @param <E>
 *            the return type of the method
 * @param <T>
 *            the type of the object on which to invoke the method
 */
public class SmartMethodPanel<E, T> extends AbstractSmartPanel<E> {

    private static final long serialVersionUID = 1500940940681907779L;

    public static final Logger LOG = Logger.getLogger(SmartMethodPanel.class.getName());

    /**
     * The method core to this SmartMethodPanel.
     */
    private final Method METHOD;

    /**
     * A map to hold the parameters and their adjoining metadatas.
     */
    private final IdentityHashMap<Parameter, SmartFieldData> PARAMETERS;

    /**
     * The object on which to invoke the function.
     */
    private T methodInvocationTarget;

    /**
     * Constructs a SmartMethodPanel.
     * 
     * @param method
     * @param methodInvocationTarget
     * @see #SmartPanelFactory
     */
    protected SmartMethodPanel(Method method, T methodInvocationTarget) {
	this.METHOD = method;
	this.methodInvocationTarget = methodInvocationTarget;
	PARAMETERS = new IdentityHashMap<>();
    }

    /**
     * Invokes the method on the current invocation target.
     * 
     * @see AbstractSmartPanel#getTarget()
     */
    @Override
    public E getTarget() {
	try {
	    return (E) METHOD.invoke(methodInvocationTarget, getAllParameters());
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * Returns the current value of all the parameters used to invoke the method.
     * 
     * @return the current value of all the parameters used to invoke the method
     */
    public Object[] getAllParameters() {
	FieldData[] parameters = getFields();
	Object[] parametersValues = new Object[parameters.length];
	for (int i = 0; i < parameters.length; i++)
	    parametersValues[i] = parameters[i].getValue();
	return parametersValues;
    }

    /**
     * Does nothing.
     * 
     * @see AbstractSmartPanel#setTarget(Object)
     */
    @Override
    public void setTarget(E target) {}

    @Override
    public SmartFieldData[] getFields() {
	return PARAMETERS.values().toArray(new SmartFieldData[PARAMETERS.size()]);
    }

    /**
     * Returns the method's invocation target.
     * 
     * @return the methodInvocationTarget
     */
    public T getMethodInvocationTarget() {
	return methodInvocationTarget;
    }

    /**
     * Sets the value of methodInvocationTarget to that of the parameter.
     * 
     * @param methodInvocationTarget
     *            the methodInvocationTarget to set
     */
    public void setMethodInvocationTarget(T methodInvocationTarget) {
	this.methodInvocationTarget = methodInvocationTarget;
    }

    /**
     * Returns the method.
     * 
     * @return the method
     */
    public Method getMethod() {
	return METHOD;
    }

    /**
     * Returns the parameters.
     * 
     * @return the parameters
     */
    public IdentityHashMap<Parameter, SmartFieldData> getPARAMETERS() {
	return PARAMETERS;
    }
}
