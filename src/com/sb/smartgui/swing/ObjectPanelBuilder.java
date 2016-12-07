package com.sb.smartgui.swing;

import java.awt.Frame;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sb.smartgui.ClassUtil;
import com.sb.smartgui.SmartFieldData;
import com.sb.smartgui.SmartObjectPanel;
import com.sb.smartgui.SmartPanelBuilder;
import com.sb.smartgui.SmartPanelFactory;
import com.sb.smartgui.StringFormatter;

// FIXME
public class ObjectPanelBuilder implements SmartPanelBuilder {

    private static final long serialVersionUID = -3846982346227003615L;

    public static final Logger LOG = Logger.getLogger(ObjectPanelBuilder.class.getName());

    /**
     * Builds a panel that will handle the Field within the FieldData.
     * Changes made to this panel's UI must be reflected to the target's field within the FieldData.
     * It is important to note that this method <b>must</b> make a sanity check to ensure that the
     * type
     * received as an argument is indeed supported by this builder. If the type is not supported,
     * return null.
     * 
     * @param fieldData
     *            Data pertaining to the configuration of the field with the SmartObjectPanel. Must
     *            be a SmartObjectPanel.
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
    @Override
    public Container build(SmartFieldData fieldData, StringFormatter formatter, SmartPanelFactory factory,
	    Frame frame) {

	LOG.fine("args: fieldData.getType() = " + fieldData.getType().getName() + ", fieldData = " + fieldData
		+ ", formatter = " + formatter
		+ ", factory = " + factory + ", frame = " + frame);
	if (!supports(fieldData.getType()))
	    return null;

	// TODO test and IMPROVE // 2016-09-21: SB: Looks pretty clean
	JPanel panel = new JPanel(new FlowLayout());
	String fieldName = formatter.format(fieldData.getName());
	panel.add(new JLabel(fieldName));
	SmartObjectPanel innerPanel = null;
	if (fieldData.getValue() != null)
	    innerPanel = factory.getSmartObjectPanel(fieldData.getValue(), frame);
	else
	    innerPanel = factory.getSmartObjectPanel(fieldData, frame);
	fieldData.setInnerPanel(innerPanel);
	JButton openWindowButton = new JButton("More details");
	ActionListener listener = (e) -> openRecursive(fieldData, fieldName, frame);
	openWindowButton.addActionListener(listener);
	panel.add(openWindowButton);;
	return panel;
    }

    private static void openRecursive(SmartFieldData fieldData, String name, Frame ownerFrame) {
	JDialog dialog = new JDialog(ownerFrame, name);
	dialog.add(fieldData.getInnerPanel());
	dialog.pack();
	dialog.setVisible(true);
    }

    @Override
    public boolean supports(Class<?> type) {
	return !type.isPrimitive() && !ClassUtil.instanceOf(type, Collection.class)
		&& !ClassUtil.instanceOf(type, Array.class)
		&& !ClassUtil.instanceOf(type, Map.class); // Should support anything, but does not yet. Unsupported types listed over.
    }
}