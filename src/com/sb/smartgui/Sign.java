package com.sb.smartgui;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Target;

/**
 * Denotes a restriction on the sign of a number field.
 * If the sign value is negative, then the field can only be negative.
 * If it is zero, then the field may have any sign.
 * If it is positive, then the field can only be positive.
 * 
 * @author Samuel Beausoleil
 *
 */
@Target(FIELD)
public @interface Sign {
    public static final byte NEGATIVE = -1;
    public static final byte NEUTRAL = 0;
    public static final byte POSITIVE = 1;
    
    byte sign();
}