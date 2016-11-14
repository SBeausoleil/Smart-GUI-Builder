package com.sb.smartgui.test;

public class TestChildrenObject {
    
    private String name;
    private int id;
    
    public TestChildrenObject(String name, int id) {
	this.name = name;
	this.id = id;
    }

    /**
     * Returns the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of name to that of the parameter.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the id.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of id to that of the parameter.
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "TestChildrenObject [name=" + name + ", id=" + id + "]";
    }

    
}
