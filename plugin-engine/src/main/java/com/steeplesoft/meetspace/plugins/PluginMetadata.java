package com.steeplesoft.meetspace.plugins;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 12, 2010
 * Time: 3:55:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PluginMetadata {
    protected String name;
    protected String type;
    protected String description;

    public PluginMetadata(Object plugin) {
        this(plugin.getClass());
    }

    public PluginMetadata(Class<?> c) {
        Plugin p = c.getAnnotation(Plugin.class);
        type = p.type();
        name = p.name();
        description = p.description();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
