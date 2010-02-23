package com.steeplesoft.meetspace.plugins.engine;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;

import javax.faces.application.Application;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.FacesValidator;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class JsfPluginEngine extends ClassloaderPluginEngine {
    public JsfPluginEngine(String pluginDir) throws IOException {
        super(pluginDir);
    }

    public JsfPluginEngine(String pluginDir, String pkg) throws IOException {
        super(pluginDir, pkg);
    }

    public JsfPluginEngine(String pluginDir, Set<String> packages) throws IOException {
        super(pluginDir, packages);
    }


}