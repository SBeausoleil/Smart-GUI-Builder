package com.sb.smartgui;

import javax.swing.JPanel;

/**
 * An extension to IFieldData to extend it's capabilities to be used for complex GUI objects within
 * the SmartGUI framework. SmartFieldData objects should not be shared between multiple GUI panels,
 * s
 * 
 * @author Samuel Beausoleil
 */
public interface SmartFieldData<E> extends IFieldData<E> {

    /**
     * Returns the panel which represents this data as the first layer of display.
     * 
     * @return the panel which represents this data
     */
    public JPanel getPanel();

    /**
     * Sets the panel which represents this data as the first layer of display.
     * 
     * @param panel
     */
    public void setPanel(JPanel panel);

    /**
     * Returns the panel which may represent this data as a second layer of display.
     * Only used when a SmartPanel is required to display a complex Object.
     * 
     * @return the optional inner panel
     */
    public AbstractSmartPanel<?> getInnerPanel();

    /**
     * Sets the optional inner panel.
     * 
     * @param innerPanel
     */
    public void setInnerPanel(AbstractSmartPanel<?> innerPanel);

    /**
     * Checks if the field is displayed
     * 
     * @return <t>true</t> if it is displayed
     */
    public boolean isDisplayed();

    /**
     * Sets whether the field should be displayed or not.
     * 
     * @param display
     */
    public void display(boolean display);

    /**
     * Returns the index of it's display order within it's owner panel.
     * 
     * @return it's position within it's primary panel
     */
    public int getIndex();

    /**
     * Updates the stored index to reflect it's position within the components of it's owner panel.
     * @return the new index
     */
    public int updateIndex();
}
