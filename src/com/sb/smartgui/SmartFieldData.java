package com.sb.smartgui;

import java.awt.Container;

/**
 * An extension to IFieldData to extend it's capabilities to be used for complex GUI objects within
 * the SmartGUI framework. SmartFieldData objects should not be shared between multiple GUI panels,
 * usage of a SmartFieldData across multiple panels is discouraged. Instead, you should create a new
 * SmartFieldData using the FieldData level of acces used by this interface.
 * 
 * @author Samuel Beausoleil
 */
public interface SmartFieldData<E> extends FieldData<E> {

    public static final int NO_OWNER_PANEL_INDEX = -1;

    /**
     * Returns the container that holds this SmartFieldData.
     * 
     * @return the container that holds this SmartFieldData
     */
    public Container getOwnerContainer();

    /**
     * Sets the container that holds this SmartFieldData.
     * Calling this method will <b>not</b> automatically remove this SmartFieldData's panel from
     * it's
     * precedent ownerContainer.
     * 
     * @param ownerPanel
     *            the new panel to hold this SmartFieldData.
     */
    public void setOwnerContainer(Container ownerPanel);

    /**
     * Returns the panel which represents this data as the first layer of display.
     * 
     * @return the panel which represents this data
     */
    public Container getPanel();

    /**
     * Sets the panel which represents this data as the first layer of display.
     * 
     * @param panel
     */
    public void setPanel(Container panel);

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
     * Sets the index of the SmartFieldData.
     * This method does <b>not</b> change the index of the SmartFieldData within it's owner container,
     * it only changes it's internal index variable. For the purpose of actually changing it's
     * display order, use <code>changeIndex(int)</code>.
     * 
     * @param index
     */
    public void setIndex(int index);

    /**
     * Updates the stored index to reflect it's position within the components of it's owner container.
     * If there is no known owner container, the index value will be set to -1.
     * 
     * @return the new index
     */
    default public int updateIndex() {
	int index = NO_OWNER_PANEL_INDEX;
	if (getOwnerContainer() != null)
	    index = Components.getComponentIndex(getOwnerContainer());
	setIndex(index);
	return index;
    }

    /**
     * Changes the index of the SmartFieldData's panel within it's owner panel.
     * 
     * @param index
     * @param validate
     *            the owner container requires to be validated after being modified. However
     *            validation can be a lengthy process so the option is given to postpone validation
     *            until later. <b>Warning:</b> If set to false, the user must call the
     *            <code>validate()</code> method on the ownerContainer later.
     */
    default public void changeIndex(int index, boolean validate) {
	setIndex(index);
	Container owner = getOwnerContainer();
	owner.remove(getPanel());
	owner.add(getPanel(), index);
	if (validate)
	    owner.validate();
    }
}
