package com.steeplesoft.meetspace.plugins.engine;

import java.io.IOException;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 14, 2010
 * Time: 10:46:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeldPluginEngine extends ClassloaderPluginEngine {
    public WeldPluginEngine(String pluginDir) throws IOException {
        super(pluginDir);
    }

    public WeldPluginEngine(String pluginDir, String pkg) throws IOException {
        super(pluginDir, pkg);
    }

    public WeldPluginEngine(String pluginDir, Set<String> packages) throws IOException {
        super(pluginDir, packages);
    }

    @Override
    protected void postProcessClass(Class<?> c) {
        System.out.println("Passing " + c.getName() + " to Weld.");
    }
}
