package com.sb.smartgui;

import java.awt.Container;
import java.awt.Frame;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Vector;

import javax.swing.JButton;

import com.sb.smartgui.swing.BooleanPanelBuilder;
import com.sb.smartgui.swing.CharacterPanelBuilder;
import com.sb.smartgui.swing.ConcreteErrorPanelBuilder;
import com.sb.smartgui.swing.EnumPanelBuilder;
import com.sb.smartgui.swing.NumberPanelBuilder;
import com.sb.smartgui.swing.ObjectPanelBuilder;
import com.sb.smartgui.swing.StringPanelBuilder;

// TODO test for support of enums
public class SmartPanelFactory {

    /**
     * A map of all the created SmartObjectPanel.
     * Is needed to resolve looped references.
     */
    protected static final IdentityHashMap<Object, SmartObjectPanel> PROCESSED_OBJECTS = new IdentityHashMap<>();

    public static StringFormatter defaultFormatter = new TitleStringFormatter();
    public static SmartPanelBuilder defaultNumberBuilder = new NumberPanelBuilder();
    public static SmartPanelBuilder defaultCharacterBuilder = new CharacterPanelBuilder();
    public static SmartPanelBuilder defaultBooleanBuilder = new BooleanPanelBuilder();
    public static SmartPanelBuilder defaultStringBuilder = new StringPanelBuilder();
    public static SmartPanelBuilder defaultEnumBuilder = new EnumPanelBuilder();
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

    protected Vector<SmartPanelBuilder> builders;

    protected SmartPanelBuilder numberBuilder;
    protected SmartPanelBuilder characterBuilder;
    protected SmartPanelBuilder booleanBuilder;
    protected SmartPanelBuilder stringBuilder;
    protected SmartPanelBuilder enumBuilder;

    /**
     * The builder for generic objects.
     * Is <b>always</b> called second to last.
     */
    protected SmartPanelBuilder objectBuilder;
    /**
     * The builder for objects that are not supported by any builder.
     * Is <b>always</b> called last.
     */
    protected ErrorPanelBuilder errorPanelBuilder;

    protected String name;

    /**
     * Overriding factories for specific class.
     * These factories have a priority of 0, meaning that they will override any and all specific
     * builders of this factory.
     */
    protected IdentityHashMap<Class, SmartPanelFactory> classSpecificFactories;

    /**
     * Overriding factories for specific method.
     * These factories have a priority of 0, meaning that they will override any and all specific
     * builders of this factory.
     */
    protected IdentityHashMap<Method, SmartPanelFactory> methodSpecificFactories;

    /**
     * Overriding factories for specific constructor.
     * These factories have a priority of 0, meaning that they will override any and all specific
     * builders of this factory.
     */
    protected IdentityHashMap<Constructor, SmartPanelFactory> constructorSpecificFactories;

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
	this.enumBuilder = defaultEnumBuilder;

	this.objectBuilder = defaultObjectBuilder;
	this.errorPanelBuilder = defaultErrorBuilder;
	this.classSpecificFactories = new IdentityHashMap<>();
	this.methodSpecificFactories = new IdentityHashMap<>();
	this.constructorSpecificFactories = new IdentityHashMap<>();

	// Add all the unfixed-priority builders to the builders vector
	builders = new Vector<>(8);
	builders.add(numberBuilder);
	builders.add(characterBuilder);
	builders.add(booleanBuilder);
	builders.add(stringBuilder);
	builders.add(enumBuilder);
    }

    protected SmartConstructorPanel generateConstructorPanel(Constructor constructor, Frame frame, String[] names) {
	// Check if there is any overriding factory
	SmartPanelFactory overridingFactory = constructorSpecificFactories.get(constructor);
	if (overridingFactory != null && this != overridingFactory)
	    return overridingFactory.generateConstructorPanel(constructor, frame, names);

	// Generate individual parameter panels
	SmartConstructorPanel smartPanel = new SmartConstructorPanel<>(constructor);
	generateExecutablePanel(frame, constructor.getParameters(), smartPanel, names);

	return smartPanel;
    }

    /**
     * Completes an ExecutablePanel passed in argument.
     * 
     * @param frame
     * @param parameters
     * @param smartPanel
     *            the ExecutablePanel to complete
     */
    protected void generateExecutablePanel(Frame frame, Parameter[] parameters, ExecutablePanel smartPanel,
	    String[] names) {
	Container paramPanel;
	Class<?> type;
	for (int i = 0; i < parameters.length; i++) {
	    paramPanel = null;
	    type = parameters[i].getType();
	    SimpleSmartFieldData fieldData = new SimpleSmartFieldData(type,
		    (i < names.length ? names[i] : parameters[i].getName()), null, null, smartPanel,
		    paramPanel);

	    // Give a basic value to the fieldData (doing otherwise will usually result in a crash from one of the builders)
	    fieldData.setValue(ClassUtil.instantiate(type));

	    // Check for overriding factory
	    SmartPanelFactory overridingFactory = classSpecificFactories.get(type);
	    if (overridingFactory != null)
		paramPanel = overridingFactory.getSmartObjectPanel(parameters[i].getType(), frame);
	    else
		paramPanel = generatePanel(frame, fieldData, type);

	    if (paramPanel != null) {
		fieldData.setPanel(paramPanel);
		smartPanel.getParameters().put(parameters[i], fieldData);
		smartPanel.add(paramPanel);
	    }
	}

	// Add a button to run the method
	JButton runButton = new JButton("Run");
	smartPanel.setInvokeButton(runButton);
	smartPanel.add(runButton);
    }

    /**
     * Generates a SmartMethodPanel.
     * 
     * @param invocationTarget
     * @param method
     * @return
     */
    protected SmartMethodPanel generateMethodPanel(Object invocationTarget, Method method, Frame frame,
	    String[] names) {
	// Check if there is any method specific factory
	SmartPanelFactory overridingFactory = methodSpecificFactories.get(method);
	if (overridingFactory != null && this != overridingFactory)
	    return overridingFactory.generateMethodPanel(invocationTarget, method, frame, names);

	// Generate individual parameter panels
	SmartMethodPanel smartPanel = new SmartMethodPanel(method, invocationTarget);
	generateExecutablePanel(frame, method.getParameters(), smartPanel, names);
	return smartPanel;
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
	    ObjectFieldData fieldData = new ObjectFieldData(target, field, null);
	    SmartFieldDataDecorator decorator = new SmartFieldDataDecorator<>(fieldData);
	    Class type = fieldData.getType();

	    // Check for any overriding class specific factories
	    overridingFactory = classSpecificFactories.get(type);
	    if (overridingFactory != null)
		fieldPanel = overridingFactory.generateObjectPanel(target, frame);
	    else
		fieldPanel = generatePanel(frame, decorator, type);

	    decorator.setPanel(fieldPanel);
	    if (fieldPanel != null) {
		smartPanel.add(fieldPanel);
		smartPanel.getFieldsMap().put(fieldData.getField(), fieldData);
		decorator.setOwnerContainer(smartPanel);
	    }
	}

	return smartPanel;
    }

    /**
     * Creates a panel using the most appropriate SmartPanelBuilder.
     * <p>
     * <b>WARNING: Direct usage of this method is discouraged.</b> <br/>
     * This method should only be used directly by objects extending this class or implementing a
     * SmartBuilder.
     * <p>
     * Checks the following SmartPanelBuilders in the order of the this#builders vector.
     * The first builder found to support the type of the specified fieldData is used.
     * In the class' default configuration, errorPanel will always catch everything if it is
     * reached.
     * 
     * @param frame
     * @param fieldData
     * @param type
     * @return
     */
    public Container generatePanel(Frame frame, SmartFieldData fieldData, Class type) {
	Container fieldPanel = null;
	for (SmartPanelBuilder builder : builders) {
	    if (builder.supports(type)) {
		fieldPanel = builder.build(fieldData, formatter, this, frame);
		break;
	    }
	}

	if (fieldPanel == null && objectBuilder.supports(type))
	    fieldPanel = objectBuilder.build(fieldData, formatter, this, frame);
	if (fieldPanel == null)
	    fieldPanel = errorPanelBuilder.build(fieldData, formatter, this, frame);

	return fieldPanel;
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
     * Returns the builders.
     * 
     * @return the builders
     */
    public Vector<SmartPanelBuilder> getBuilders() {
	return builders;
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
     * Returns the constructorSpecificFactories.
     * 
     * @return the constructorSpecificFactories
     */
    public IdentityHashMap<Constructor, SmartPanelFactory> getConstructorSpecificFactories() {
	return constructorSpecificFactories;
    }

    /**
     * Returns the enumBuilder.
     * 
     * @return the enumBuilder
     */
    public SmartPanelBuilder getEnumBuilder() {
	return enumBuilder;
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
     * Returns the methodSpecificFactories.
     * 
     * @return the methodSpecificFactories
     */
    public IdentityHashMap<Method, SmartPanelFactory> getMethodSpecificFactories() {
	return methodSpecificFactories;
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
     * Returns a SmartConstructorPanel for the supplied constructor.
     * It is to note that depending upon the compiler's arguments, the name of each parameters may
     * be erased, so the ability to provide names if desired is provided.
     * 
     * @param constructor
     * @param frame
     * @param names
     *            the name of each parameters. Is optional.
     * @return
     */
    public <T> SmartConstructorPanel<T> getSmartConstructorPanel(Constructor<T> constructor, Frame frame,
	    String... names) {
	if (constructor == null)
	    throw new IllegalArgumentException("Constructor may not be null.");

	return generateConstructorPanel(constructor, frame, names);
    }

    /**
     * Creates a SmartMethodPanel and returns it.
     * It is to note that depending upon the compiler's arguments, the name of each parameters may
     * be erased, so the ability to provide names if desired is provided.
     * 
     * @param invocationTarget
     *            the object on which the method shall be invoked
     * @param method
     *            the method core to the SmartMethodPanel
     * @param frame
     *            the frame that will receive tbe generated SmartMethodPanel
     * @param names
     * @return a new SmartMethodPanel
     */
    public <T> SmartMethodPanel<?, T> getSmartMethodPanel(T invocationTarget, Method method, Frame frame,
	    String... names) {
	if (method == null)
	    throw new IllegalArgumentException("The method may not be null");
	if (invocationTarget == null)
	    throw new IllegalArgumentException("The invocation target may not be null");
	return generateMethodPanel(invocationTarget, method, frame, names);
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
    public <T> SmartObjectPanel<T> getSmartObjectPanel(Class<T> clazz, Frame frame, Field... ignore) {
	return generateObjectPanel(ClassUtil.instantiate(clazz), frame, ignore);
    }

    /**
     * Creates a SmartObjectPanel
     * Is best used when the Field's value is null but will work nevertheless if it is initialized.
     *
     * @param field
     *            The Field which wraps the target value
     * @param fieldType
     *            The type of the field
     * @param fieldOwner
     *            The Object owning the field argument
     * @param frame
     *            The Frame which will hold the produced SmartObjectPanel
     * @param ignore
     *            Fields to ignore within the wrapped value in the field argument
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @see #getSmartObjectPanel(FieldData, Frame, Field...)
     */
    public <T> SmartObjectPanel<T> getSmartObjectPanel(Field field, Class<T> fieldType, Object fieldOwner, Frame frame,
	    Field... ignore)
	    throws IllegalArgumentException, IllegalAccessException {
	Object fieldValue = field.get(fieldOwner);
	if (fieldValue == null) {
	    // Instantiate the field
	    fieldValue = ClassUtil.instantiate(field.getType());
	    field.set(fieldOwner, fieldValue);
	}
	return (SmartObjectPanel<T>) getSmartObjectPanel(fieldValue, frame, ignore);
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
    public <T> SmartObjectPanel<T> getSmartObjectPanel(FieldData<T> fieldData, Frame frame, Field... ignore) {
	// This method does not check for a
	if (fieldData.getValue() == null)
	    fieldData.setValue((T) ClassUtil.instantiate(fieldData.getType()));
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
    public <T> SmartObjectPanel<T> getSmartObjectPanel(T target, Frame frame, Field... ignore) {
	// NULL argument is not accepted
	if (target == null)
	    throw new IllegalArgumentException("The target may not be NULL.");

	// Check if the target is already processed
	SmartObjectPanel<T> panel = PROCESSED_OBJECTS.get(target);
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

    protected void replaceBuilder(SmartPanelBuilder oldBuilder, SmartPanelBuilder newBuilder) {
	int index = builders.indexOf(oldBuilder);
	if (index == -1)
	    builders.add(newBuilder);
	else
	    builders.set(index, newBuilder);
    }

    /**
     * Sets the value of booleanBuilder to that of the parameter.
     *
     * @param booleanBuilder
     *            the booleanBuilder to set
     */
    public void setBooleanBuilder(SmartPanelBuilder booleanBuilder) {
	replaceBuilder(this.booleanBuilder, booleanBuilder);
	this.booleanBuilder = booleanBuilder;
    }

    /**
     * Sets the value of characterBuilder to that of the parameter.
     *
     * @param characterBuilder
     *            the characterBuilder to set
     */
    public void setCharacterBuilder(SmartPanelBuilder characterBuilder) {
	replaceBuilder(this.characterBuilder, characterBuilder);
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
     * Sets the value of constructorSpecificFactories to that of the parameter.
     * 
     * @param constructorSpecificFactories
     *            the constructorSpecificFactories to set
     */
    public void setConstructorSpecificFactories(
	    IdentityHashMap<Constructor, SmartPanelFactory> constructorSpecificFactories) {
	this.constructorSpecificFactories = constructorSpecificFactories;
    }

    /**
     * Sets the value of enumBuilder to that of the parameter.
     * 
     * @param enumBuilder
     *            the enumBuilder to set
     */
    public void setEnumBuilder(SmartPanelBuilder enumBuilder) {
	replaceBuilder(this.enumBuilder, enumBuilder);
	this.enumBuilder = enumBuilder;
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
     * Sets the value of methodSpecificFactories to that of the parameter.
     * 
     * @param methodSpecificFactories
     *            the methodSpecificFactories to set
     */
    public void setMethodSpecificFactories(IdentityHashMap<Method, SmartPanelFactory> methodSpecificFactories) {
	this.methodSpecificFactories = methodSpecificFactories;
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
	replaceBuilder(this.numberBuilder, numberBuilder);
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
	replaceBuilder(this.stringBuilder, stringBuilder);
	this.stringBuilder = stringBuilder;
    }
}

/*
 * Need: Make it possible to set the priority of each builder while retaining the ability to modify
 * them.
 * Solution: Use a Vector to list all the builders (priority set by index)
 * -> Issue: Keeping track of where each builders are can be problematic.
 * :: Solution :: Usage of the method <code>vector.indexOf(Object)</code> to find back the location
 * of the main builders. User will have to keep track of his own builders.
 */