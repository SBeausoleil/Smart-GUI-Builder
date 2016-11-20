package com.sb.smartgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.swing.JButton;

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

    private JButton invokeButton;
    private MethodListener buttonListener = new MethodListener();

    /**
     * Constructs a SmartMethodPanel.
     * 
     * @param method
     * @param methodInvocationTarget
     * @see SmartPanelFactory
     */
    protected SmartMethodPanel(Method method, T methodInvocationTarget) {
	this.METHOD = method;
	this.methodInvocationTarget = methodInvocationTarget;
	PARAMETERS = new IdentityHashMap<>();
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

    @Override
    public SmartFieldData[] getFields() {
	return PARAMETERS.values().toArray(new SmartFieldData[PARAMETERS.size()]);
    }

    /**
     * Returns the invokeButton.
     * 
     * @return the invokeButton
     */
    public JButton getInvokeButton() {
	return invokeButton;
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
     * Returns the method's invocation target.
     * 
     * @return the methodInvocationTarget
     */
    public T getMethodInvocationTarget() {
	return methodInvocationTarget;
    }

    /**
     * Returns the parameters.
     * 
     * @return the parameters
     */
    public IdentityHashMap<Parameter, SmartFieldData> getPARAMETERS() {
	return PARAMETERS;
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
     * Sets the value of invokeButton to that of the parameter.
     * 
     * @param invokeButton
     *            the invokeButton to set
     */
    public void setInvokeButton(JButton invokeButton) {
	this.invokeButton = invokeButton;
	invokeButton.addActionListener(buttonListener);
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
     * Does nothing.
     * 
     * @see AbstractSmartPanel#setTarget(Object)
     */
    @Override
    public void setTarget(E target) {}

    /**
     * Adds a MethodInvocationListener to call after the core method has been called.
     * 
     * @param listener
     */
    public void addMethodInvocationListener(MethodInvocationListener listener) {
	buttonListener.listeners.add(listener);
    }

    /**
     * The listener on the button that calls the method.
     * After calling the method, it passes along the method's returned value to all of it's
     * registered sub-listeners.
     * 
     * @author Samuel Beausoleil
     */
    private class MethodListener implements ActionListener {

	LinkedList<MethodInvocationListener> listeners;

	@Override
	public void actionPerformed(ActionEvent e) {
	    Object result = getTarget();

	    for (MethodInvocationListener listener : listeners)
		listener.methodInvoked(result);
	}
    }
}
