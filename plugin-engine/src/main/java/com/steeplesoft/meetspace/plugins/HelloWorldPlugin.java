package com.steeplesoft.meetspace.plugins;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 12, 2010
 * Time: 3:52:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Plugin(name="helloWorld", type="demo")
public class HelloWorldPlugin {
    public String getMessage() {
        return "Hello, world!";
    }
}