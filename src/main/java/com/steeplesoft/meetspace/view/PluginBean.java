package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.plugins.PluginEngine;
import com.steeplesoft.meetspace.plugins.PluginMetadata;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 12, 2010
 * Time: 9:01:56 AM
 * To change this template use File | Settings | File Templates.
 */
@Named
@ApplicationScoped
public class PluginBean {
    private PluginEngine pe;

    public PluginBean() {
        String home = System.getenv("MEETSPACE_HOME");
        if (home == null) {
            home = System.getProperty("user.home");
        }
        pe = new PluginEngine(home + File.separatorChar + "/plugins", "com.steeplesoft.meetspace.plugins");
    }

    public List<Object> getPlugins(String type) {
        return pe.getPlugins(type);
    }

    public Object getPlugin(String type, String name) {
        final List<Object> objectList = getPlugins(type);
        for (Object obj : objectList) {
            PluginMetadata pd = new PluginMetadata(obj);
            if (pd.getType().equals(type) && pd.getName().equals(name)) {
                return obj;
            }
        }

        return null;
    }
}
