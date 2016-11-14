package com.sb.smartgui;

import javax.swing.JPanel;


/**
 * A panel that handles directly data to be sent to an underlying Java Object or Method.
 * 
 * @author Samuel Beausoleil
 *
 * @param <E> the type of data that is the end goal product of this SmartDataPanel
 */
public abstract class AbstractSmartPanel<E> extends JPanel {

    private static final long serialVersionUID = 2813309144850041790L;
    
    /**
     * Returns the end goal product of this SmartDataPanel.
     * 
     * @return the end goal product of this SmartDataPanel.
     */
    public abstract E getTarget();

    /**
     * Sets the end goal product of this SmartDataPanel.
     * 
     * @param target
     */
    public abstract void setTarget(E target);

    /**
     * Returns the fields and their metadatas used by this SmartPanel.
     * 
     * @return the fields and their metadatas used by this SmartPanel.
     */
    public abstract SmartFieldData[] getFields();
    
}
