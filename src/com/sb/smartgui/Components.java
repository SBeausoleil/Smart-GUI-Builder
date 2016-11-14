package com.sb.smartgui;

import java.awt.Component;
import java.awt.Container;

public class Components {

    private Components() {
    }

    public static int getComponentIndex(Component component) {
	if (component != null && component.getParent() != null) {
	    Container container = component.getParent();
	    for (int i = 0; i < container.getComponentCount(); i++) {
		if (container.getComponent(i) == component)
		    return i;
	    }
	}
	return -1;
    }
}
