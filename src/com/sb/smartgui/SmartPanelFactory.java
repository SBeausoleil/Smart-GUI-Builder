package com.sb.smartgui;

import java.awt.Frame;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.IdentityHashMap;

import javax.swing.JPanel;

// TODO add functionality to build panels for constructors and functions
// TODO test for support of enums
public class SmartPanelFactory {

    protected static final IdentityHashMap<Object, SmartObjectPanel> PROCESSED_OBJECTS = new IdentityHashMap<>();
    protected static final IdentityHashMap<Method, SmartMethodPanel> PROCESSED_METHODS = new IdentityHashMap<>();

    public static StringFormatter defaultFormatter = new TitleStringFormatter();

    public static SmartPanelBuilder defaultNumberBuilder = new NumberPanelBuilder(true);

    public static SmartPanelBuilder defaultCharacterBuilder = new CharacterPanelBuilder();

    public static SmartPanelBuilder defaultBooleanBuilder = new BooleanPanelBuilder();

    public static SmartPanelBuilder defaultStringBuilder = new StringPanelBuilder();

    public static SmartPanelBuilder defaultObjectBuilder = new ObjectPanelBuilder();

    public static ErrorPanelBuilder defaultErrorBuilder = new ConcreteErrorPanelBuilder();

    /**
     * Class specific factories that will override the objectBuilder of all factories.
     * It is to note that the priority of the check for commonClassSpecificFactories is second to
     * last, right before last resort objectBuilder.
     */
    public static IdentityHashMap<Class, SmartPanelFactory> commonClassSpecificFactories = new IdentityHashMap<>();

    public static IdentityHashMap<Method, SmartPanelFactory> commonMethodSpecificFactories = new IdentityHashMap<>();

    public static final SmartPanelFactory DEFAULT_FACTORY = new SmartPanelFactory("Default Factory");

    private static String generateIgnoreNames(Field[] ignore) {
	if (ignore.length == 0)
	    return "";

	StringBuilder builder = new StringBuilder(", ignore: ");
	for (int i = 0; i < ignore.length; i++) {
	    builder.append(ignore[i].getName());
	    if (i + 1 < ignore.length)
		builder.append(", ");
	}
	return builder.toString();
    }

    private static Field[] getFields(Object target, Field... ignore) {
	ArrayList<Field> fields = new ArrayList<>();
	Class currentClass = target.getClass();
	while (currentClass != null) {
	    for (Field field : currentClass.getDeclaredFields())
		if (!Modifier.isStatic(field.getModifiers()) && !isIgnored(ignore, field))
		    fields.add(field);
	    currentClass = currentClass.getSuperclass();
	}
	return fields.toArray(new Field[fields.size()]);
    }

    private static boolean isIgnored(Field[] ignore, Field field) {
	for (Field ignoreField : ignore)
	    if (ignoreField == field)
		return true;
	return false;
    }

    /**
     * Formats field names for representation on the UI.
     */
    private StringFormatter formatter;
    private SmartPanelBuilder numberBuilder;
    private SmartPanelBuilder characterBuilder;
    private SmartPanelBuilder booleanBuilder;

    private SmartPanelBuilder stringBuilder;

    private SmartPanelBuilder objectBuilder;

    private ErrorPanelBuilder errorPanelBuilder;

    private String name;

    /**
     * Overriding factories for specific class.
     * These factories have a priority of 1, meaning that they will override any and all specific
     * builders of this factory.
     */
    private IdentityHashMap<Class, SmartPanelFactory> classSpecificFactories;

    /**
     * Overriding factories for specific method.
     * These factories have a priority of 1, meaning that they will override any and all specific
     * builders of this factory.
     */
    private IdentityHashMap<Method, SmartPanelFactory> methodSpecificFactories;

    public SmartPanelFactory(String name) {
	this.name = name;
	this.formatter = defaultFormatter;
	this.numberBuilder = defaultNumberBuilder;
	this.characterBuilder = defaultCharacterBuilder;
	this.booleanBuilder = defaultBooleanBuilder;
	this.stringBuilder = defaultStringBuilder;
	this.objectBuilder = defaultObjectBuilder;
	this.errorPanelBuilder = defaultErrorBuilder;
	this.classSpecificFactories = new IdentityHashMap<>();
    }

    protected SmartObjectPanel generateObjectPanel(Object target, Frame frame, Field... ignore) {
	SmartObjectPanel panel = new SmartObjectPanel(target, this);

	// Check for any overriding class specific factories
	SmartPanelFactory overridingFactory = classSpecificFactories.get(target.getClass());
	if (overridingFactory != null)
	    return overridingFactory.generateObjectPanel(target, frame, ignore);

	// Gather the instance fields
	Field[] fields = getFields(target, ignore);
	for (Field field : fields) {
	    field.setAccessible(true);

	    JPanel fieldPanel = null;
	    SmartObjectFieldData fieldData = new SmartObjectFieldData(target, field, null);
	    Class type = fieldData.getType();

	    // Check for any overriding class specific factories
	    overridingFactory = classSpecificFactories.get(type);
	    if (overridingFactory != null) {
		fieldPanel = overridingFactory.generateObjectPanel(target, frame);
	    }

	    fieldPanel = generatePanel(frame, fieldData, type);

	    fieldData.setPanel(fieldPanel);
	    if (fieldPanel != null) {
		panel.add(fieldPanel);
		panel.getFieldsMap().put(fieldData.getField(), fieldData);
		fieldData.setOwnerContainer(panel);
	    }
	}

	// Register the SmartPanel
	PROCESSED_OBJECTS.put(target, panel);

	return panel;
    }

    /**
     * @param frame
     * @param fieldData
     * @param type
     * @return
     */
    protected JPanel generatePanel(Frame frame, SmartObjectFieldData fieldData, Class type) {
	JPanel fieldPanel;
	if (numberBuilder.supports(type))
	fieldPanel = numberBuilder.build(fieldData, formatter, this, frame);
	else if (characterBuilder.supports(type))
	fieldPanel = characterBuilder.build(fieldData, formatter, this, frame);
	else if (booleanBuilder.supports(type))
	fieldPanel = booleanBuilder.build(fieldData, formatter, this, frame);
	else if (stringBuilder.supports(type))
	fieldPanel = stringBuilder.build(fieldData, formatter, this, frame);
	else if (objectBuilder.supports(type))
	fieldPanel = objectBuilder.build(fieldData, formatter, this, frame);
	else
	fieldPanel = errorPanelBuilder.build(fieldData, formatter, this, frame);
	return fieldPanel;
    }

    private SmartMethodPanel generateMethodPanel(Object invocationTarget, Method method) {
	// Check if the method's panel already exists
	
	Parameter[] parameters = method.getParameters();

	// Check for any overriding method specific factories
	SmartMethodPanel
	
	return null;
    }

    /**
     * Returns the booleanBuilder.
     *
     * @return the booleanBuilder
     */
    public SmartPanelBuilder getBooleanBuilder() {
	return booleanBuilder;
    }

    /**
     * Returns the characterBuilder.
     *
     * @return the characterBuilder
     */
    public SmartPanelBuilder getCharacterBuilder() {
	return characterBuilder;
    }

    /**
     * Returns the classSpecificFactories.
     *
     * @return the classSpecificFactories
     */
    public IdentityHashMap<Class, SmartPanelFactory> getClassSpecificFactories() {
	return classSpecificFactories;
    }

    /**
     * Returns the errorPanelBuilder.
     *
     * @return the errorPanelBuilder
     */
    public ErrorPanelBuilder getErrorPanelBuilder() {
	return errorPanelBuilder;
    }

    /**
     * Returns the formatter.
     *
     * @return the formatter
     */
    public StringFormatter getFormatter() {
	return formatter;
    }

    /**
     * Returns the name.
     *
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Returns the numberBuilder.
     *
     * @return the numberBuilder
     */
    public SmartPanelBuilder getNumberBuilder() {
	return numberBuilder;
    }

    /**
     * Returns the objectBuilder.
     *
     * @return the objectBuilder
     */
    public SmartPanelBuilder getObjectBuilder() {
	return objectBuilder;
    }

    /**
     * Returns a SmartObjectPanel.
     * 
     * @param clazz
     *            the class of the desired central value of the SmartObjectPanel.
     * @param frame
     * @param ignore
     * @return
     */
    public SmartObjectPanel getSmartObjectPanel(Class clazz, Frame frame, Field... ignore) {
	return generateObjectPanel(ClassUtil.instantiate(clazz), frame, ignore);
    }

    /**
     * Creates a SmartObjectPanel
     * Is best used when the Field's value is null but will work nevertheless if it is initialized.
     *
     * @param field
     * @param fieldOwner
     * @param frame
     * @param ignore
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @see #getSmartObjectPanel(FieldData, Frame, Field...)
     */
    public SmartObjectPanel getSmartObjectPanel(Field field, Object fieldOwner, Frame frame, Field... ignore)
	    throws IllegalArgumentException, IllegalAccessException {
	Object fieldValue = field.get(fieldOwner);
	if (fieldValue == null) {
	    // Instantiate the field
	    fieldValue = ClassUtil.instantiate(field.getType());
	    field.set(fieldOwner, fieldValue);
	}
	return getSmartObjectPanel(fieldValue, frame, ignore);
    }

    /**
     * Creates a SmartObjectPanel
     * Can handle null value within the FieldData.
     *
     * @param fieldData
     * @param frame
     * @param ignore
     * @return
     */
    public SmartObjectPanel getSmartObjectPanel(FieldData fieldData, Frame frame, Field... ignore) {
	// This method does not check for a
	if (fieldData.getValue() == null)
	    fieldData.setValue(ClassUtil.instantiate(fieldData.getType()));
	return getSmartObjectPanel(fieldData.getValue(), frame, ignore);
    }

    /**
     * Creates a SmartObjectPanel for the specified target object.
     * If there already exists a SmartObjectPanel for this specific object (identity check), the
     * pre-existing panel will be sent back instead of a new one.
     * 
     * @param target
     * @param frame
     * @param ignore
     * @return
     */
    public SmartObjectPanel getSmartObjectPanel(Object target, Frame frame, Field... ignore) {
	// NULL argument is not accepted
	if (target == null)
	    throw new IllegalArgumentException("The target may not be NULL.");

	// Check if the target is already processed
	SmartObjectPanel panel = PROCESSED_OBJECTS.get(target);
	if (panel != null) // If so, return the already processed form
	    return panel;

	panel = generateObjectPanel(target, frame, ignore);
	return panel;
    }

    /**
     * Returns the stringBuilder.
     *
     * @return the stringBuilder
     */
    public SmartPanelBuilder getStringBuilder() {
	return stringBuilder;
    }

    /**
     * Sets the value of booleanBuilder to that of the parameter.
     *
     * @param booleanBuilder
     *            the booleanBuilder to set
     */
    public void setBooleanBuilder(SmartPanelBuilder booleanBuilder) {
	this.booleanBuilder = booleanBuilder;
    }

    /**
     * Sets the value of characterBuilder to that of the parameter.
     *
     * @param characterBuilder
     *            the characterBuilder to set
     */
    public void setCharacterBuilder(SmartPanelBuilder characterBuilder) {
	this.characterBuilder = characterBuilder;
    }

    /**
     * Sets the value of classSpecificFactories to that of the parameter.
     *
     * @param classSpecificFactories
     *            the classSpecificFactories to set
     */
    public void setClassSpecificFactories(IdentityHashMap<Class, SmartPanelFactory> classSpecificFactories) {
	this.classSpecificFactories = classSpecificFactories;
    }

    /**
     * Sets the value of errorPanelBuilder to that of the parameter.
     *
     * @param errorPanelBuilder
     *            the errorPanelBuilder to set
     */
    public void setErrorPanelBuilder(ErrorPanelBuilder errorPanelBuilder) {
	this.errorPanelBuilder = errorPanelBuilder;
    }

    /**
     * Sets the value of formatter to that of the parameter.
     *
     * @param formatter
     *            the formatter to set
     */
    public void setFormatter(StringFormatter formatter) {
	this.formatter = formatter;
    }

    /**
     * Sets the value of name to that of the parameter.
     *
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Sets the value of numberBuilder to that of the parameter.
     *
     * @param numberBuilder
     *            the numberBuilder to set
     */
    public void setNumberBuilder(SmartPanelBuilder numberBuilder) {
	this.numberBuilder = numberBuilder;
    }

    /**
     * Sets the value of objectBuilder to that of the parameter.
     *
     * @param objectBuilder
     *            the objectBuilder to set
     */
    public void setObjectBuilder(SmartPanelBuilder objectBuilder) {
	this.objectBuilder = objectBuilder;
    }

    /**
     * Sets the value of stringBuilder to that of the parameter.
     *
     * @param stringBuilder
     *            the stringBuilder to set
     */
    public void setStringBuilder(SmartPanelBuilder stringBuilder) {
	this.stringBuilder = stringBuilder;
    }

    /**
     * Returns the methodSpecificFactories.
     * 
     * @return the methodSpecificFactories
     */
    public IdentityHashMap<Method, SmartPanelFactory> getMethodSpecificFactories() {
	return methodSpecificFactories;
    }

    /**
     * Sets the value of methodSpecificFactories to that of the parameter.
     * 
     * @param methodSpecificFactories
     *            the methodSpecificFactories to set
     */
    public void setMethodSpecificFactories(IdentityHashMap<Method, SmartPanelFactory> methodSpecificFactories) {
	this.methodSpecificFactories = methodSpecificFactories;
    }
}
