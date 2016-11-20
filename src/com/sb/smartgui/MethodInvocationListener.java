package com.sb.smartgui;

import java.util.EventListener;

/**
 * A listener for SmartMethodPanel. Is triggered whenever the owner
 * {@link SmartMethodPanel#getTarget()} method is called. This listener receives the result of the
 * core method of the panel.
 * 
 * @author Samuel Beausoleil
 * @see SmartMethodPanel
 */
@FunctionalInterface
public interface MethodInvocationListener extends EventListener {

    /**
     * Invoked when the tied-to method is invoked.
     * 
     * @param methodReturn
     *            the returned value of the tied-to method. Null if the method was void.
     */
    public void methodInvoked(Object methodReturn);
}
