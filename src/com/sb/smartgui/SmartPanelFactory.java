package com.sb.smartgui;

import java.awt.Container;
import java.awt.Frame;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.IdentityHashMap;

import javax.swing.JButton;

// TODO add functionality to build panels for constructors and functions
// TODO test for support of enums
public class SmartPanelFactory {

    /**
     * A map of all the created SmartObjectPanel.
     * Is needed to resolve looped references.
     */
    protected static final IdentityHashMap<Object, SmartObjectPanel> PROCESSED_OBJECTS = new IdentityHashMap<>(); // OPTIMIZE test if IdentityHashMap is good in cases where 

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
    protected StringFormatter formatter;
    protected SmartPanelBuilder numberBuilder;
    protected SmartPanelBuilder characterBuilder;
    protected SmartPanelBuilder booleanBuilder;
    protected SmartPanelBuilder stringBuilder;
    protected SmartPanelBuilder objectBuilder;
    protected ErrorPanelBuilder errorPanelBuilder;

    protected String name;

    /**
     * Overriding factories for specific class.
     * These factories have a priority of 1, meaning that they will override any and all specific
     * builders of this factory.
     */
    protected IdentityHashMap<Class, SmartPanelFactory> classSpecificFactories;

    /**
     * Overriding factories for specific method.
     * These factories have a priority of 1, meaning that they will override any and all specific
     * builders of this factory.
     */
    protected IdentityHashMap<Method, SmartPanelFactory> methodSpecificFactories;

    /**
     * Creates a new SmartPanelFactory.
     * The builders used by a new SmartPanelFactory are the default ones.
     * 
     * @param name
     *            the name of the factory.
     */
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
	this.methodSpecificFactories = new IdentityHashMap<>();
    }

    /**
     * Generates a SmartObjectPanel using the factorie's builders.
     * It is recommended to not call this method directly and instead to pass via
     * 
     * @param target
     *            the core object of the SmartObjectPanel
     * @param frame
     *            the Frame that will hold the SmartObjectPanel
     * @param ignore
     *            fields not to produce
     * @return an ObjectPanel
     */
    protected SmartObjectPanel generateObjectPanel(Object target, Frame frame, Field... ignore) {
	// Check for any overriding class specific factories
	SmartPanelFactory overridingFactory = classSpecificFactories.get(target.getClass());
	if (overridingFactory != null && this != overridingFactory)
	    return overridingFactory.generateObjectPanel(target, frame, ignore);

	SmartObjectPanel smartPanel = new SmartObjectPanel(target, this);
	// Register the SmartPanel
	PROCESSED_OBJECTS.put(target, smartPanel);

	// Gather the instance fields
	Field[] fields = getFields(target, ignore);
	for (Field field : fields) {
	    field.setAccessible(true);

	    Container fieldPanel = null;
	    SmartObjectFieldData fieldData = new SmartObjectFieldData(target, field, null);
	    Class type = fieldData.getType();

	    // Check for any overriding class specific factories
	    overridingFactory = classSpecificFactories.get(type);
	    if (overridingFactory != null)
		fieldPanel = overridingFactory.generateObjectPanel(target, frame);
	    else
		fieldPanel = generatePanel(frame, fieldData, type);

	    fieldData.setPanel(fieldPanel);
	    if (fieldPanel != null) {
		smartPanel.add(fieldPanel);
		smartPanel.getFieldsMap().put(fieldData.getField(), fieldData);
		fieldData.setOwnerContainer(smartPanel);
	    }
	}

	return smartPanel;
    }

    /**
     * Creates a panel using the most appropriate SmartPanelBuilder.
     * Checks the following SmartPanelBuilders in this specific order:
     * <li>numberBuilder</li>
     * <li>characterBuilder</li>
     * <li>booleanPanel</li>
     * <li>stringBuilder</li>
     * <li>objectBuilder</li>
     * <li>errorPanelBuilder</li>
     * The first builder found to support the type of the specified fieldData is used.
     * In the class' default configuration, errorPanel will always catch everything if it is
     * reached.
     * 
     * @param frame
     * @param fieldData
     * @param type
     * @return
     */
    protected Container generatePanel(Frame frame, SmartFieldData fieldData, Class type) {
	Container fieldPanel;
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

    /**
     * Generates a SmartMethodPanel.
     * 
     * @param invocationTarget
     * @param method
     * @return
     */
    // TESTME
    protected SmartMethodPanel generateMethodPanel(Object invocationTarget, Method method, Frame frame) {
	// Check if there is any method specific factory
	SmartPanelFactory overridingFactory = methodSpecificFactories.get(method);
	if (overridingFactory != null && this != overridingFactory)
	    return overridingFactory.generateMethodPanel(invocationTarget, method, frame);

	// Get the parameters
	Parameter[] parameters = method.getParameters();

	// Generate individual parameter panels
	SmartMethodPanel smartPanel = new SmartMethodPanel(method, invocationTarget);
	Container paramPanel;
	Class<?> type;
	for (Parameter param : parameters) {
	    paramPanel = null;
	    type = param.getType();
	    SimpleSmartFieldData fieldData = new SimpleSmartFieldData(type, name, null, smartPanel, paramPanel);

	    // Check for overriding factory
	    overridingFactory = classSpecificFactories.get(type);
	    if (overridingFactory != null)
		paramPanel = overridingFactory.getSmartObjectPanel(param.getType(), frame);
	    else
		paramPanel = generatePanel(frame, fieldData, type);

	    if (paramPanel != null) {
		fieldData.setPanel(paramPanel);
		smartPanel.getParameters().put(param, fieldData);
		smartPanel.add(paramPanel);
	    }
	}

	// Add a button to run the method
	JButton runButton = new JButton("Run");
	smartPanel.setInvokeButton(runButton);
	smartPanel.add(runButton);
	return smartPanel;
    }

    /**
     * Creates a SmartMethodPanel and returns it.
     * 
     * @param invocationTarget
     *            the object on which the method shall be invoked
     * @param method
     *            the method core to the SmartMethodPanel
     * @param frame
     *            the frame that will receive tbe generated SmartMethodPanel
     * @return a new SmartMethodPanel
     */
    public SmartMethodPanel getSmartMethodPanel(Object invocationTarget, Method method, Frame frame) {
	if (method == null)
	    throw new IllegalArgumentException("The method may not be null");
	if (invocationTarget == null)
	    throw new IllegalArgumentException("The invocation target may not be null");
	return generateMethodPanel(invocationTarget, method, frame);
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
