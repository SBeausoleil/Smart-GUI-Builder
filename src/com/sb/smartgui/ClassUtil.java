package com.sb.smartgui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

// FIXME causes StackOverFlowException when given a LinkedList
// FIXME_ADDENDUM: probably also to anything that have looped constructor type references
// FIXME_SOLUTION: When a constructor has a type parameter loop, give NULL to these parameters
public final class ClassUtil {

    public static volatile int numberInitializationValue = 0;
    public static volatile char charInitializationValue = 'a';
    public static volatile boolean booleanInitializationValue = false;

    /**
     * The instantiate(Class) method will return a null instead of throwing an error if it fails to
     * instantiate an Object.
     */
    public static volatile boolean giveNullIfFailedInstantiation = false;

    /**
     * The percentage of primitives within the parameters for a constructor required to pass this
     * constructor in a prefered way.
     */
    public static volatile float primsToParamsRatio = 0.5f;

    public static final Logger LOG = Logger.getLogger(ClassUtil.class.getName());

    private ClassUtil() {}

    public static <T> T instantiate(Class<T> clazz) {
	LOG.fine(clazz.getName());

	// Test for primitives
	if (clazz == byte.class || clazz == short.class || clazz == int.class || clazz == long.class
		|| clazz == float.class || clazz == double.class)
	    return (T) toObject(numberInitializationValue);
	else if (clazz == char.class)
	    return (T) toObject(charInitializationValue);
	else if (clazz == boolean.class)
	    return (T) toObject(booleanInitializationValue);
	// Test for primitives wrappers
	try {
	    if (clazz == Byte.class)
		return clazz.getConstructor(byte.class).newInstance((byte) numberInitializationValue);
	    else if (clazz == Short.class)
		return clazz.getConstructor(short.class).newInstance((short) numberInitializationValue);
	    else if (clazz == Integer.class)
		return clazz.getConstructor(int.class).newInstance(numberInitializationValue);
	    else if (clazz == Long.class)
		return clazz.getConstructor(long.class).newInstance((long) numberInitializationValue);
	    else if (clazz == Float.class)
		return clazz.getConstructor(long.class).newInstance((long) numberInitializationValue);
	    else if (clazz == Double.class)
		return clazz.getConstructor(double.class).newInstance((double) numberInitializationValue);
	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
	    LOG.fine(String.format("%s caught while testing primitive wrappers.", e.getClass().getName()));
	    LOG.fine(e.getMessage());
	    throw new RuntimeException(e);
	}

	// Test for a null constructor
	try {
	    return clazz.newInstance();
	} catch (InstantiationException | IllegalAccessException e) {}

	// Test for other constructors
	Constructor[] constructors = clazz.getDeclaredConstructors();
	orderConstructors(constructors);

	// Attempt instantiation
	for (Constructor constructor : constructors) {
	    try {
		return (T) instantiate(constructor);
	    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException e) {
		// Silence the error and try again with a different constructor
		LOG.fine("Constructor with parameters: " + Arrays.toString(constructor.getParameterTypes())
			+ " failed instantiation.");
		LOG.log(Level.FINER, "Exception thrown: ", e);
	    }
	}
	if (giveNullIfFailedInstantiation)
	    return null;
	else
	    throw new RuntimeException("Did not manage to instantiate class \"" + clazz.toString() + "\"");
    }

    public static Object instantiate(Constructor constructor)
	    throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	Class constructorType = constructor.getDeclaringClass();
	// Get the types list
	Class[] types = constructor.getParameterTypes();
	// Make the paramaters
	Object[] parameters = new Object[types.length];
	// Instantiate the parameters
	for (int i = 0; i < parameters.length; i++) {
	    // To prevent StackOverflowError when given looped type parameters in the constructor, give a NULL value to the looped parameters
	    if (types[i] == constructorType)
		parameters[i] = null;
	    else
		parameters[i] = instantiate(types[i]);
	}

	return constructor.newInstance(parameters);
    }

    /**
     * Orders constructors based upon their number of parameters and the prevalence of their
     * primitives.
     * 
     * @param constructors
     */
    public static void orderConstructors(Constructor[] constructors) {
	// Mixed parameters and primitive preference evaluator
	Arrays.sort(constructors, (a, b) -> {
	    int nParamsA = a.getParameterCount();
	    int nParamsB = b.getParameterCount();

	    // Check if there is any empty constructors (those are safer)
	    if (nParamsA == 0)
		return -1;
	    else if (nParamsB == 0)
		return 1;

	    // Evaluate upon the ratio of nPrims:nParams
	    int nPrimsA = countPrimitiveParameters(a);
	    int nPrimsB = countPrimitiveParameters(b);
	    float primsToParamsA = calculateRatio(nPrimsA, nParamsA);
	    float primsToParamsB = calculateRatio(nPrimsB, nParamsB);

	    // IF there is a significant ratio favor the ratio over of the number of parameters for comparison
	    if (primsToParamsA >= primsToParamsRatio || primsToParamsB >= primsToParamsRatio) {
		// Favor highest ratio
		if (primsToParamsA < primsToParamsB)
		    return -1;
		else if (primsToParamsA == primsToParamsB)
		    return 0;
		else
		    return 1;
	    } else {
		// Favor the number of params
		if (nParamsA < nParamsB)
		    return -1;
		else if (nParamsA == nParamsB)
		    return 0;
		else
		    return 1;
	    }
	});
    }

    private static float calculateRatio(int nPrims, int nParams) {
	return nPrims / nParams;
    }

    public static int countPrimitiveParameters(Constructor constructor) {
	int nPrimitives = 0;
	for (Class<?> type : constructor.getParameterTypes())
	    if (type.isPrimitive() || isPrimitiveWrapper(type))
		nPrimitives++;
	return nPrimitives;
    }

    public static boolean isPrimitiveWrapper(Class clazz) {
	return clazz == Byte.class || clazz == Short.class || clazz == Integer.class
		|| clazz == Long.class || clazz == Float.class || clazz == Double.class || clazz == Character.class
		|| clazz == Boolean.class;
    }

    public static boolean instanceOf(Class<?> type, Class<?> clazz) {
	while (type != null) {
	    // Start by checking interfaces
	    for (Class implementedInterface : type.getInterfaces())
		if (implementedInterface == clazz)
		    return true;
	    // Assign the superclass to the type variable
	    type = type.getSuperclass();
	    if (type == clazz)
		return true;
	}
	return false;
    }

    /**
     * Returns the received argument as an Object.
     * This method does nothing but return the received argument as an Object. This is used to hide
     * the type of primitives to allow them to be casted as generics.
     * 
     * @param obj
     * @return the argument
     */
    private static Object toObject(Object obj) {
	return obj;
    }
}
