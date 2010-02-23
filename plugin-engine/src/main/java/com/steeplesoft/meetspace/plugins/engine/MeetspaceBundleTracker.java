/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.plugins.engine;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;
import javassist.ClassPool;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import javax.faces.application.Application;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.FacesValidator;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author jasonlee
 */
public class MeetspaceBundleTracker implements BundleTrackerCustomizer {
    private static Logger logger = Logger.getLogger(MeetspaceBundleTracker.class.getName());
    private static final Class<?>[] SCOPES = {
            RequestScoped.class,
            SessionScoped.class,
            ApplicationScoped.class,
            ViewScoped.class,
            NoneScoped.class,
            CustomScoped.class
    };
    private static final Class<?>[] ANNOTATIONS = {
            ManagedBean.class,
            FacesConverter.class,
            FacesValidator.class
    };

    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        System.out.println("Tracking bundle " + bundle.getSymbolicName());
        try {
            Enumeration<URL> e = bundle.findEntries("/", "*.class", true);
            if (e != null) {
                while (e.hasMoreElements()) {
                    URL url = e.nextElement();
                    String className = getClassName(url.toString());
                    try {
                        Class clazz = bundle.loadClass(className);
                        System.out.println(clazz.getName());
                    } catch (Exception ex) {
                        //
                    }
                    try {
                        Class clazz = event.getBundle().loadClass(className);
                        System.out.println(clazz.getName());
                    } catch (Exception ex) {
                        //
                    }
                    try {
                        Class clazz = bundle.getClass().getClassLoader().loadClass(className);
                        System.out.println(clazz.getName());
                    } catch (Exception ex) {
                        //
                    }
//                    processClass(clazz);
                    processEntry(url, bundle);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return bundle;
    }

    protected void processClass(Class clazz) {
        System.out.println(clazz.getName());
    }

    private String getClassName(String path) {
        String className = path.substring(path.indexOf("/", 9)+1, path.length()-6).replaceAll("/", ".");
        return className;
    }

    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
        //
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        //
    }

    protected void processEntry(URL url, Bundle bundle) throws Exception {
        ClassFile cf = new ClassFile(new DataInputStream(new BufferedInputStream(url.openStream())));
        List annotations = new ArrayList();
        AttributeInfo info = cf.getAttribute(AnnotationsAttribute.visibleTag);
        if (info != null) {
            if (info instanceof AnnotationsAttribute) {
                for (Annotation a : ((AnnotationsAttribute) info).getAnnotations()) {
                    annotations.add(a.toAnnotationType(bundle.getClass().getClassLoader(), new ClassPool()));
                }

                String className = getClassName(url.toString());
//                Class clazz = bundle.loadClass(className);
                processAnnotations(annotations);
            }
            System.out.println(info.toString());
        }
    }

    protected void processAnnotations(List annotations) {
        for (Object annotation : annotations) {
            System.out.println(annotation);
        }
    }

    protected void processFile(File file) {
        try {
            File files[] = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    processFile(f);
                } else {
                    System.out.println(f.getCanonicalPath());
                }
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    protected void postProcessClass(Class<?> c) {
        for (Class<?> scope : ANNOTATIONS) {
            java.lang.annotation.Annotation a = c.getAnnotation((Class<? extends java.lang.annotation.Annotation>) scope);
            if (a != null) {
                if (a instanceof ManagedBean) {
                    processManagedBean(c, (ManagedBean) a);
                } else if (a instanceof FacesConverter) {
                    processConverter(c, (FacesConverter) a);
                } else if (a instanceof FacesValidator) {
                    processsValidator(c, (FacesValidator) a);
                }
            }
        }
    }

    private void processsValidator(Class<?> c, FacesValidator facesValidator) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        Object key;
        app.addValidator(facesValidator.value(), c.getName());
        if (facesValidator.isDefault()) {
            app.addDefaultValidatorId(facesValidator.value());
        }
    }

    private void processConverter(Class<?> c, FacesConverter facesConverter) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        Object key;
        if (facesConverter.value().length() == 0) {
            key = facesConverter.forClass();
        } else {
            key = facesConverter.value();
        }
        if (key instanceof Class) {
            app.addConverter((Class) key, c.getName());
        } else {
            app.addConverter((String) key, c.getName());
        }
    }

    protected void processManagedBean(Class<?> c, ManagedBean annotation) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final ExternalContext externalContext = facesContext.getExternalContext();
        ApplicationAssociate associate = ApplicationAssociate.getInstance(externalContext);
        BeanManager manager = associate.getBeanManager();
        String name = getManagedBeanName(c, annotation);
        String scope = getScope(c);
        boolean eager = annotation.eager();
        Map<String, Field> annotatedFields = new LinkedHashMap<String, Field>();
        collectAnnotatedFields(c, annotatedFields);
        List<ManagedBeanInfo.ManagedProperty> properties = null;

        if (!annotatedFields.isEmpty()) {
            properties = new ArrayList<ManagedBeanInfo.ManagedProperty>(annotatedFields.size());
            for (Map.Entry<String, Field> entry : annotatedFields.entrySet()) {
                Field f = entry.getValue();
                ManagedProperty property = f.getAnnotation(ManagedProperty.class);
                ManagedBeanInfo.ManagedProperty propertyInfo =
                        new ManagedBeanInfo.ManagedProperty(entry.getKey(),
                                f.getType().getName(),
                                property.value(),
                                null,
                                null);
                properties.add(propertyInfo);
            }
        }

        manager.register(new ManagedBeanInfo(name,
                c.getName(),
                scope,
                eager,
                null,
                null,
                properties,
                null));
    }

    private String getManagedBeanName(Class<?> c, ManagedBean mb) {
        String name = mb.name();
        if (name.length() == 0) {
            String t = c.getName();
            name = t.substring(t.lastIndexOf('.') + 1);
//            char[] nameChars = name.toCharArray();
//            nameChars[0] = Character.toLowerCase(nameChars[0]);
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        return name;
    }

    private String getScope(Class<?> c) {
        String result = "request"; // default
        for (Class<?> scope : SCOPES) {
            java.lang.annotation.Annotation a = c.getAnnotation((Class<? extends java.lang.annotation.Annotation>) scope);
            if (a != null) {
                if (a instanceof RequestScoped) {
                    result = "request";
                } else if (a instanceof SessionScoped) {
                    result = "session";
                } else if (a instanceof ApplicationScoped) {
                    result = "application";
                } else if (a instanceof ViewScoped) {
                    result = "view";
                } else if (a instanceof NoneScoped) {
                    result = "none";
                } else if (a instanceof CustomScoped) {
                    result = ((CustomScoped) a).value();
                }
            }
        }

        return result;
    }

    private void collectAnnotatedFields(Class<?> baseClass, Map<String, Field> annotatedFields) {

        Field[] fields = baseClass.getDeclaredFields();
        for (Field field : fields) {
            ManagedProperty property = field.getAnnotation(ManagedProperty.class);
            if (property != null) {
                String propName = property.name();
                if (propName == null || propName.length() == 0) {
                    propName = field.getName();
                }
                if (!annotatedFields.containsKey(propName)) {
                    annotatedFields.put(propName, field);
                }
            }
        }
        Class<?> superClass = baseClass.getSuperclass();
        if (!Object.class.equals(superClass)) {
            collectAnnotatedFields(superClass, annotatedFields);
        }

    }

}