package com.sb.smartgui;

public class TitleStringFormatter implements StringFormatter {

    @Override
    public String format(String toFormat) {
	char[] chars = toFormat.toCharArray();
	StringBuilder builder = new StringBuilder(chars.length);
	
	chars[0] = Character.toUpperCase(chars[0]);
	builder.append(chars[0]);
	for (int i = 1; i < chars.length; i++) {
	    if (Character.isUpperCase(chars[i]))
		builder.append(" ");
	    builder.append(chars[i]);
	}
	return builder.toString();
    }

}
