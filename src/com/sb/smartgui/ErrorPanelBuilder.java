/**
 * 
 */
package com.sb.smartgui;

/**
 * A panel used to indicate that no panel builder supplied to the factory could handle the given
 * type.
 * Any class extending this must support every type. It is not expected to build a functional panel
 * to handle the type, but rather to indicate that the field could not be handled. If the build()
 * method returns null, the panel will not be displayed at all.
 * 
 * @author Samuel Beausoleil
 */
public abstract class ErrorPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = -2237815801647364517L;

    /**
     * Returns true.
     * All ErrorPanelBuilder extending classes must support everything.
     * 
     * @see com.sb.smartgui.SmartPanelBuilder#supports(java.lang.Class)
     */
    @Override
    public final boolean supports(Class<?> type) {
	return true;
    }

}
