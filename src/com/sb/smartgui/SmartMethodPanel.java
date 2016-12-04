package com.sb.smartgui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.IdentityHashMap;

/**
 * A SmartPanel with the execution of a method as it's goal.
 * 
 * @author Samuel Beausoleil
 * @param <E>
 *            the return type of the method
 * @param <T>
 *            the type of the object on which to invoke the method
 */
public class SmartMethodPanel<E, T> extends ExecutablePanel<E> {

    private static final long serialVersionUID = 1500940940681907779L;


    /**
     * The method core to this SmartMethodPanel.
     */
    protected final Method METHOD;
    
    /**
     * The object on which to invoke the function.
     */
    protected T methodInvocationTarget;

    /**
     * Constructs a SmartMethodPanel.
     * 
     * @param method
     * @param methodInvocationTarget
     * @see SmartPanelFactory
     */
    protected SmartMethodPanel(Method method, T methodInvocationTarget) {
	super();
	this.METHOD = method;
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
     * Returns the parameters.
     * 
     * @return the parameters
     */
    public IdentityHashMap<Parameter, SmartFieldData> getParameters() {
	return PARAMETERS;
    }
}
