package com.sb.smartgui;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class SmartObjectPanel<E> extends AbstractSmartPanel<E> {

    private static final long serialVersionUID = -4050248359152782160L;

    public static final Logger LOG = Logger.getLogger(SmartObjectPanel.class.getName());

    protected E target;

    protected final LinkedHashMap<Field, SmartFieldDataDecorator<?, ObjectFieldData>> FIELDS_MAP;

    protected SmartPanelFactory maker;

    protected SmartObjectPanel(E target, SmartPanelFactory maker) {
	this.target = target;
	this.maker = maker;
	FIELDS_MAP = new LinkedHashMap<>();
    }

    protected SmartObjectPanel(E target, SmartPanelFactory maker,
	    LinkedHashMap<Field, SmartFieldDataDecorator<?, ObjectFieldData>> fields) {
	this.target = target;
	this.maker = maker;
	this.FIELDS_MAP = fields;
    }

    /**
     * Returns the fieldsMap.
     * 
     * @return the fieldsMap
     */
    public LinkedHashMap<Field, SmartFieldDataDecorator<?, ObjectFieldData>> getFieldsMap() {
	return FIELDS_MAP;
    }

    /**
     * Returns the target.
     * 
     * @return the target
     */
    @Override
    public E getTarget() {
	return target;
    }

    /**
     * Sets the value of target to that of the parameter.
     * 
     * @param target
     *            the target to set
     */
    @Override
    public void setTarget(E target) {
	this.target = target;
	for (SmartFieldDataDecorator<?, ObjectFieldData> field : FIELDS_MAP.values())
	    field.getFieldData().setFieldOwner(target);
    }

    @Override
    public SmartFieldData[] getFields() {
	return FIELDS_MAP.values().toArray(new SmartFieldData[FIELDS_MAP.size()]);
    }
}