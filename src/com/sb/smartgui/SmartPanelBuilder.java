package com.sb.smartgui;

import java.awt.Container;
import java.awt.Frame;
import java.io.Serializable;

/**
 * A builder for a panel that will handle a Field within a SmartObjectPanel. This class is mostly
 * used within the context of a SmartObjectPanelFactory.
 * 
 * @author Samuel Beausoleil
 */
public interface SmartPanelBuilder extends Serializable {

    /**
     * Builds a panel that will handle the Field within the FieldData.
     * Changes made to this panel's UI must be reflected to the target's field within the FieldData.
     * It is important to note that this method <b>must</b> make a sanity check to ensure that the
     * type
     * received as an argument is indeed supported by this builder. If the type is not supported,
     * return null.
     * 
     * @param fieldData
     *            Data pertaining to the configuration of the field with the SmartObjectPanel
     * @param formatter
     *            The formatter to convert strings for display. Most often used to convert the name
     *            of the field for display.
     * @param factory
     *            A factory that the panel builder may use. May be set to null or not used at all.
     * @param frame
     *            The Frame object that will display this panel. May be set to null or not used at
     *            all.
     * @return The panel built by this method. Null if the type of the field is not supported.
     */
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory,
	    Frame frame);

    /**
     * Checks if the type is supported by this builder.
     * 
     * @param type
     * @return true if the type is supported.
     */
    public boolean supports(Class<?> type);

}
