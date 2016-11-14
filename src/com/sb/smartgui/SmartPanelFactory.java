package com.sb.smartgui;

import java.awt.Frame;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.logging.Logger;

import javax.swing.JPanel;

// TODO add functionality to build panels for constructors and functions
public class SmartPanelFactory {

    public static final Logger LOG = Logger.getLogger(SmartPanelFactory.class.getName());

    protected static final IdentityHashMap<Object, SmartObjectPanel> PROCESSED_OBJECTS = new IdentityHashMap<>();

    public static StringFormatter defaultFormatter = new TitleStringFormatter();

    public static SmartPanelBuilder defaultNumberBuilder = new NumberPanelBuilder(true);

    public static SmartPanelBuilder defaultCharacterBuilder = new CharacterPanelBuilder();

    public static SmartPanelBuilder defaultBooleanBuilder = new BooleanPanelBuilder();

    public static SmartPanelBuilder defaultStringBuilder = new StringPanelBuilder();

    public static SmartPanelBuilder defaultObjectBuilder = new ObjectPanelBuilder();

    public static ErrorPanelBuilder defaultErrorBuilder = new ConcreteErrorPanelBuilder();

    /**
     * Class specific factories that will override the objectBuilder of all factories.
     * Ir is to note that the priority of the check for commonClassSpecificFactories is second to
     * last, right before last resort objectBuilder.
     */
    public static IdentityHashMap<Class, SmartPanelFactory> commonClassSpecificFactories = new IdentityHashMap<>();

    public static final SmartPanelFactory DEFAULT_FACTORY = new SmartPanelFactory("Default Factory");

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

    private String generateIgnoreNames(Field[] ignore) {
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

    private SmartObjectPanel generatePanel(Object target, Frame frame, Field... ignore) {
	SmartObjectPanel panel = new SmartObjectPanel(target, this);

	// Check for any overriding class specific factories
	SmartPanelFactory overridingFactory = classSpecificFactories.get(target.getClass());
	if (overridingFactory != null)
	    return overridingFactory.generatePanel(target, frame, ignore);

	// Gather the instance fields
	Field[] fields = getFields(target, ignore);
	for (Field field : fields) {
	    field.setAccessible(true);

	    JPanel fieldPanel = null;
	    SmartObjectFieldData fieldData = new SmartObjectFieldData(target, field, null, null);
	    Class currentFieldType = field.getType();

	    // Check for any overriding class specific factories
	    overridingFactory = classSpecificFactories.get(currentFieldType);
	    if (overridingFactory != null)
		fieldPanel = overridingFactory.generatePanel(target, frame);
	    else if (numberBuilder.supports(currentFieldType))
		fieldPanel = numberBuilder.build(fieldData, formatter, this, frame);
	    else if (characterBuilder.supports(currentFieldType))
		fieldPanel = characterBuilder.build(fieldData, formatter, this, frame);
	    else if (booleanBuilder.supports(currentFieldType))
		fieldPanel = booleanBuilder.build(fieldData, formatter, this, frame);
	    else if (stringBuilder.supports(currentFieldType))
		fieldPanel = stringBuilder.build(fieldData, formatter, this, frame);
	    else if (objectBuilder.supports(currentFieldType))
		fieldPanel = objectBuilder.build(fieldData, formatter, this, frame);
	    else
		fieldPanel = errorPanelBuilder.build(fieldData, formatter, this, frame);

	    fieldData.setPanel(fieldPanel);
	    if (fieldPanel != null)
		panel.add(fieldPanel);
	}

	return panel;
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

    private Field[] getFields(Object target, Field... ignore) {
	ArrayList<Field> fields = new ArrayList<>();
	Class currentClass = target.getClass();
	while (currentClass != null) {
	    //fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
	    for (Field field : currentClass.getDeclaredFields())
		if (!Modifier.isStatic(field.getModifiers()) && !isIgnored(ignore, field))
		    fields.add(field);
	    currentClass = currentClass.getSuperclass();
	}
	return fields.toArray(new Field[fields.size()]);
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

    public SmartObjectPanel getSmartObjectPanel(Class clazz, Frame frame, Field... ignore) {
	LOG.fine("args: class = " + clazz.getName() + ", frame = " + frame + generateIgnoreNames(ignore));
	Object target = ClassUtil.instantiate(clazz);
	SmartObjectPanel panel = generatePanel(target, frame, ignore);
	PROCESSED_OBJECTS.put(target, panel);
	return panel;
    }

    /**
     * Creates a SmartObjectPanel
     * Is best used when the field value is null.
     * Use getSmartObjectPanel(IFieldData, Frame, Field...) instead.
     * 
     * @param field
     * @param fieldOwner
     * @param frame
     * @param ignore
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Deprecated // May cause issues with IFieldData
    public SmartObjectPanel getSmartObjectPanel(Field field, Object fieldOwner, Frame frame, Field... ignore)
	    throws IllegalArgumentException, IllegalAccessException {
	LOG.fine("args: field = " + field.getName() + ", fieldOwner: " + fieldOwner + ", frame = " + frame
		+ generateIgnoreNames(ignore));
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
    public SmartObjectPanel getSmartObjectPanel(IFieldData fieldData, Frame frame, Field... ignore) {
	if (fieldData.getValue() == null)
	    fieldData.setValue(ClassUtil.instantiate(fieldData.getType()));
	return getSmartObjectPanel(fieldData.getValue(), frame, ignore);
    }

    public SmartObjectPanel getSmartObjectPanel(Object target, Frame frame, Field... ignore) {
	LOG.fine("args: target = " + target + ", frame = " + frame + generateIgnoreNames(ignore));

	// NULL argument is not accepted
	if (target == null)
	    throw new IllegalArgumentException("The target may not be NULL.");

	// Check if the target is already processed
	SmartObjectPanel panel = PROCESSED_OBJECTS.get(target);
	if (panel != null) // If so, return the already processed form
	    return panel;

	panel = generatePanel(target, frame, ignore);
	PROCESSED_OBJECTS.put(target, panel);
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

    private boolean isIgnored(Field[] ignore, Field field) {
	for (Field ignoreField : ignore)
	    if (ignoreField == field)
		return true;
	return false;
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
}
