package com.steeplesoft.meetspace.plugins;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;

import javax.faces.application.Application;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.FacesValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class JsfPluginEngine extends PluginEngine {
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

    public JsfPluginEngine(String pluginDir) {
        super(pluginDir);
    }

    public JsfPluginEngine(String pluginDir, String pkg) {
        super(pluginDir, pkg);
    }

    public JsfPluginEngine(String pluginDir, Set<String> packages) {
        super(pluginDir, packages);
    }

    @Override
    protected void postProcessClass(Class<?> c) {
        for (Class<?> scope : ANNOTATIONS) {
            Annotation a = c.getAnnotation((Class<? extends Annotation>) scope);
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
            Annotation a = c.getAnnotation((Class<? extends Annotation>) scope);
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