package com.sb.smartgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.JButton;

/**
 * An abstraction of smart method panels to allow for sharing of useful attributes and methods.
 * 
 * @author Samuel Beausoleil
 * @param <E>
 *            the return type of the invocation
 */
public abstract class ExecutablePanel<E> extends AbstractSmartPanel<E> {

    private static final long serialVersionUID = -8376490580447770779L;

    /**
     * A map to hold the parameters and their adjoining metadatas.
     * It is critical to use a map which retains insertion order and to insert parameter key/pair in
     * the correct order. The correct order of insertion is the order of parameters within the
     * associated method or constructor's signature.
     */
    protected final LinkedHashMap<Parameter, SmartFieldData> PARAMETERS;

    protected JButton invokeButton;
    protected MethodListener buttonListener;

    /**
     * Constructs a SmartMethodPanel.
     * 
     * @param method
     * @param methodInvocationTarget
     * @see SmartPanelFactory
     */
    protected ExecutablePanel() {
	buttonListener = new MethodListener();
	PARAMETERS = new LinkedHashMap<>();
    }

    /**
     * Adds a MethodInvocationListener to call after the core method has been called.
     * 
     * @param listener
     */
    public void addExecutionListener(ExecutionListener listener) {
	buttonListener.listeners.add(listener);
    }

    /**
     * Returns the current value of all the parameters used to invoke what this panel invokes.
     * 
     * @return the current value of all the parameters used to invoke.
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
     * Sets the value of invokeButton to that of the parameter.
     * Calling this method will also remove the method listener on the previous button.
     * 
     * @param invokeButton
     *            the invokeButton to set
     */
    public void setInvokeButton(JButton invokeButton) {
	if (this.invokeButton != null)
	    this.invokeButton.removeActionListener(buttonListener);
	this.invokeButton = invokeButton;
	invokeButton.addActionListener(buttonListener);
    }

    /**
     * Does nothing.
     * 
     * @see AbstractSmartPanel#setTarget(Object)
     */
    @Override
    public void setTarget(E target) {}

    /**
     * Returns the parameters.
     * 
     * @return the parameters
     */
    public LinkedHashMap<Parameter, SmartFieldData> getParameters() {
	return PARAMETERS;
    }

    /**
     * The listener on the button that calls the method.
     * After calling the method, it passes along the method's returned value to all of it's
     * registered sub-listeners.
     * 
     * @author Samuel Beausoleil
     */
    private class MethodListener implements ActionListener {

	private LinkedList<ExecutionListener> listeners = new LinkedList<>();

	@Override
	public void actionPerformed(ActionEvent e) {
	    Object result = getTarget();

	    for (ExecutionListener listener : listeners)
		listener.methodInvoked(result);
	}
    }
}
