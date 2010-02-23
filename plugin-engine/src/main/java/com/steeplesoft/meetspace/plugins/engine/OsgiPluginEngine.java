/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.plugins.engine;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.logging.Logger;
import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.framework.util.StringMap;
import org.apache.felix.main.AutoProcessor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.util.tracker.BundleTracker;

import javax.faces.bean.ManagedBean;
import javax.faces.component.FacesComponent;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.convert.FacesConverter;
import javax.faces.event.NamedEvent;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.FacesRenderer;
import javax.faces.validator.FacesValidator;

/**
 *
 * @author jasonlee
 */
public class OsgiPluginEngine implements BundleActivator {
    private String bundleDir;
    private BundleTracker bundleTracker;
    private final static Logger logger = Logger.getLogger(OsgiPluginEngine.class.getName());

    private static final Set<String> FACES_ANNOTATIONS;
    private static final Set<Class<? extends Annotation>> FACES_ANNOTATION_TYPE;

    static {
        HashSet<String> annotations = new HashSet<String>(8, 1.0f);
        Collections.addAll(annotations,
                           "Ljavax/faces/component/FacesComponent;",
                           "Ljavax/faces/convert/FacesConverter;",
                           "Ljavax/faces/validator/FacesValidator;",
                           "Ljavax/faces/render/FacesRenderer;",
                           "Ljavax/faces/bean/ManagedBean;",
                           "Ljavax/faces/event/NamedEvent;",
                           "Ljavax/faces/component/behavior/FacesBehavior;",
                           "Ljavax/faces/render/FacesBehaviorRenderer;");
        FACES_ANNOTATIONS = Collections.unmodifiableSet(annotations);
        HashSet<Class<? extends Annotation>> annotationInstances =
              new HashSet<Class<? extends Annotation>>(8, 1.0f);
        Collections.addAll(annotationInstances,
                           FacesComponent.class,
                           FacesConverter.class,
                           FacesValidator.class,
                           FacesRenderer.class,
                           ManagedBean.class,
                           NamedEvent.class,
                           FacesBehavior.class,
                           FacesBehaviorRenderer.class);
        FACES_ANNOTATION_TYPE = Collections.unmodifiableSet(annotationInstances);
    }

    public OsgiPluginEngine(String bundleDir) throws IOException {
        this.bundleDir = bundleDir;
        final File cacheDir = File.createTempFile("cache", null);
        cacheDir.delete();
        cacheDir.deleteOnExit();
        Map configMap = new StringMap(false);
        configMap.put(FelixConstants.LOG_LEVEL_PROP, "4");
        configMap.put(Constants.FRAMEWORK_STORAGE, cacheDir.getAbsolutePath());
        configMap.put(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, bundleDir);
        configMap.put(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, "install");
        configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES,
            "org.osgi.framework; version=1.3.0," +
            "org.osgi.service.packageadmin; version=1.2.0," +
            "org.osgi.service.startlevel; version=1.0.0," +
            "org.osgi.service.url; version=1.0.0");
        // Create list to hold custom framework activators.
        List list = new ArrayList();
        // Add our own activator.
        list.add(this);
        // Add activator to process auto-start/install properties.
//        list.add(new StartLevelActivator(configMap));
        // Add our custom framework activators to the configuration map.
        configMap.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, list);

        try
        {
            // Now create an instance of the framework.
            Felix felix = new Felix(configMap);
            felix.start();
            AutoProcessor.process(configMap, felix.getBundleContext());
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void start(BundleContext context) throws Exception {
        bundleTracker = new BundleTracker(context, Bundle.INSTALLED, new MeetspaceBundleTracker());
        bundleTracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        bundleTracker.close();
    }


}