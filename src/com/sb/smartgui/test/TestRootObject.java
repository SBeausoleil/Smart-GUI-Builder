package com.sb.smartgui.test;

public class TestRootObject {

    // Test round 1: primitives and simple objects
    private int intField;
    private char charField;
    private boolean boolField;
    private String stringField;
    private Integer integerField;
    private Character characterField;
    private Boolean booleanField;

    // Test round 2: complex objects
    private TestChildrenObject child;

    // Test round 3: null objects
    private TestChildrenObject nullChild = null;

    public TestRootObject(int intField, char charField, boolean boolField, String stringField, Integer integerField,
	    Character characterField, Boolean booleanField, TestChildrenObject child) {
	this.intField = intField;
	this.charField = charField;
	this.boolField = boolField;
	this.stringField = stringField;
	this.integerField = integerField;
	this.characterField = characterField;
	this.booleanField = booleanField;
	this.child = child;
    }

    /**
     * Returns the intField.
     * 
     * @return the intField
     */
    public int getIntField() {
	return intField;
    }

    /**
     * Sets the value of intField to that of the parameter.
     * 
     * @param intField
     *            the intField to set
     */
    public void setIntField(int intField) {
	this.intField = intField;
    }

    /**
     * Returns the charField.
     * 
     * @return the charField
     */
    public char getCharField() {
	return charField;
    }

    /**
     * Sets the value of charField to that of the parameter.
     * 
     * @param charField
     *            the charField to set
     */
    public void setCharField(char charField) {
	this.charField = charField;
    }

    /**
     * Returns the boolField.
     * 
     * @return the boolField
     */
    public boolean isBoolField() {
	return boolField;
    }

    /**
     * Sets the value of boolField to that of the parameter.
     * 
     * @param boolField
     *            the boolField to set
     */
    public void setBoolField(boolean boolField) {
	this.boolField = boolField;
    }

    /**
     * Returns the stringField.
     * 
     * @return the stringField
     */
    public String getStringField() {
	return stringField;
    }

    /**
     * Sets the value of stringField to that of the parameter.
     * 
     * @param stringField
     *            the stringField to set
     */
    public void setStringField(String stringField) {
	this.stringField = stringField;
    }

    /**
     * Returns the integerField.
     * 
     * @return the integerField
     */
    public Integer getIntegerField() {
	return integerField;
    }

    /**
     * Sets the value of integerField to that of the parameter.
     * 
     * @param integerField
     *            the integerField to set
     */
    public void setIntegerField(Integer integerField) {
	this.integerField = integerField;
    }

    /**
     * Returns the characterField.
     * 
     * @return the characterField
     */
    public Character getCharacterField() {
	return characterField;
    }

    /**
     * Sets the value of characterField to that of the parameter.
     * 
     * @param characterField
     *            the characterField to set
     */
    public void setCharacterField(Character characterField) {
	this.characterField = characterField;
    }

    /**
     * Returns the booleanField.
     * 
     * @return the booleanField
     */
    public Boolean getBooleanField() {
	return booleanField;
    }

    /**
     * Sets the value of booleanField to that of the parameter.
     * 
     * @param booleanField
     *            the booleanField to set
     */
    public void setBooleanField(Boolean booleanField) {
	this.booleanField = booleanField;
    }

    /**
     * Returns the child.
     * 
     * @return the child
     */
    public TestChildrenObject getChild() {
	return child;
    }

    // Is used to test the SmartMethodPanelMain
    public String testMethod(String message) {
	System.out.println("Within the method's object");
	System.out.println("Parameter: " + message);
	return message;
    }

    /**
     * Sets the value of child to that of the parameter.
     * 
     * @param child
     *            the child to set
     */
    public void setChild(TestChildrenObject child) {
	this.child = child;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "TestRootObject [intField=" + intField + ", charField=" + charField + ", boolField=" + boolField
		+ ", stringField=" + stringField + ", integerField=" + integerField + ", characterField="
		+ characterField + ", booleanField=" + booleanField + ", child=" + child + ", nullChild=" + nullChild
		+ "]";
    }

}
