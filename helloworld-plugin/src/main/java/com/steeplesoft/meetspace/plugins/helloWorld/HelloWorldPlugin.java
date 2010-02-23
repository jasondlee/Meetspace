package com.steeplesoft.meetspace.plugins.helloWorld;

import com.steeplesoft.meetspace.plugins.Plugin;

@Plugin(name="helloWorld", type="eventPlugin")
public class HelloWorldPlugin {
    public HelloWorldPlugin() {
        System.out.println("HelloWorldPlugin created");
    }
}