package com.sb.smartgui.test;

import com.sb.smartgui.ClassUtil;

public class TestClassUtilMain {
    public static void main(String[] args) {
	TestRootObject obj = (TestRootObject) ClassUtil.instantiate(TestRootObject.class);
	System.out.println(obj.toString());
    }
}
