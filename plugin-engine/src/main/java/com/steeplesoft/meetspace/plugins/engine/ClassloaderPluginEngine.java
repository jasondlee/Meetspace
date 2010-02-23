package com.steeplesoft.meetspace.plugins.engine;

import com.steeplesoft.meetspace.plugins.Plugin;
import com.steeplesoft.meetspace.plugins.PluginMetadata;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;

import javax.faces.application.Application;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.FacesValidator;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassloaderPluginEngine {
    protected String pluginDir;
    protected Set<String> packages;
    protected static final Class[] parameters = new Class[]{URL.class};
    protected Map<String, List<Class<?>>> pluginClasses = new HashMap<String, List<Class<?>>>();
    protected Map<String, List<Object>> pluginInstances = new HashMap<String, List<Object>>();

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

    public ClassloaderPluginEngine(String pluginDir) throws IOException {
        this(pluginDir, (Set<String>) null);
    }

    public ClassloaderPluginEngine(String pluginDir, String pkg) throws IOException {
        this.pluginDir = pluginDir;
        this.packages = new TreeSet<String>();
        packages.add(pkg);

        scanForPlugins();
    }

    public ClassloaderPluginEngine(String pluginDir, Set<String> packages) throws IOException {
        this.pluginDir = pluginDir;
        this.packages = packages;

        scanForPlugins();
    }

    public synchronized List<Class<?>> getPluginClassList(String type) {
        List<Class<?>> list = pluginClasses.get(type);
        if (list == null) {
            list = new ArrayList<Class<?>>();
            pluginClasses.put(type, list);
        }

        return list;
    }

    public synchronized List<Object> getPlugins(String type) {
        List<Object> list = pluginInstances.get(type);
        if (list == null) {
            list = new ArrayList<Object>();
            List<Class<?>> classes = pluginClasses.get(type);
            if (classes != null) {
                for (Class c : classes) {
                    try {
                        list.add(c.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            pluginInstances.put(type, list);
        }

        return list;
    }

    protected void postProcessClass(Class<?> c) {
        for (Class<?> scope : ANNOTATIONS) {
            Annotation a = c.getAnnotation((Class<? extends Annotation>) scope);
            if (a != null) {
                if (a instanceof ManagedBean) {
                    processManagedBean(c, (ManagedBean) a);
                } else if (a instanceof FacesConverter) {
                    processConverter(c, (FacesConverter) a);
                } else if (a instanceof FacesValidator) {
                    processValidator(c, (FacesValidator) a);
                }
            }
        }
    }

    protected synchronized void scanForPlugins() throws IOException {
        scanPackages();
        scanDirectory(this.pluginDir);
    }

    protected void scanPackages() {
        if (packages != null) {
            for (String p : packages) {
                try {
                    final URL resource = getClass().getClassLoader().getResource(p.replace('.', '/'));
                    File directory = new File(resource.getFile());
                    if (directory.exists()) {
                        String[] files = directory.list();
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].endsWith(".class")) {
                                processClass(null, p + "." + files[i]);
                            }
                        }
                    } else {
//                        throw new ClassNotFoundException(p + " does not appear to be a valid package");
                    }
                } catch (NullPointerException x) {
//                    throw new ClassNotFoundException(p + " does not appear to be a valid package");
                }
            }
        }
    }

    protected void scanDirectory(String directory) throws IOException {
        if (directory != null) {
            File dir = new File(directory);
            if (!dir.exists()) {
                return;
            }

            File[] entries = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String name) {
                    return ((name != null) && ((name.endsWith(".jar")) || file.isDirectory()));
                }
            });
            for (File entry : entries) {
                if (entry.isDirectory()) {
                    scanDirectory(entry.getCanonicalPath());
                } else if (entry.getName().endsWith(".jar")) {
                    try {
                        addUrl(entry.toURL());
                        processJar(entry);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void processJar(File file) throws IOException {
        JarFile jarFile = new JarFile(file);
        for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }

            String name = entry.getName();
            if (name.endsWith(".class")) {
                processClass(file, name);
            }
        }
    }

    protected void processClass(InputStream inputStream) {

    }

    protected void processClass(File file, String name) {
        name = convertToClassName(name);
        Class<?> c = null;
        if (file != null) {
            c = loadClass(file, name);
        } else {
            try {
                c = getClass().getClassLoader().loadClass(name);
//                Thread.currentThread().getContextClassLoader().loadClass(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        if (c != null) {
            if (c.isAnnotationPresent(Plugin.class)) {
                PluginMetadata pm = new PluginMetadata(c);
                List<Class<?>> list = getPluginClassList(pm.getType());
                list.add(c);
            }

            postProcessClass(c);
        }
    }

    protected static String convertToClassName(String name) {
        return name.substring(0, name.length() - 6).replace('/', '.');
    }

    protected Class<?> loadClass(File jar, String name) {
        Class<?> clazz = null;
        try {
            String filePath = "jar:file://" + jar.getAbsolutePath() + "!/";
            URL url = new File(filePath).toURL();
            ClassLoader clazzLoader = new URLClassLoader(new URL[]{url});
            clazz = clazzLoader.loadClass(name);
        } catch (Exception e) {

        }
        return clazz;
    }


    // Code borrowed from/influenced by : http://twit88.com/blog/2007/10/04/java-dynamic-loading-of-class-and-jar-file/

    /**
     * Add URL to CLASSPATH
     *
     * @param u URL
     * @throws IOException IOException
     */
    protected void addUrl(URL u) throws IOException {
        URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL urls[] = sysLoader.getURLs();
        for (int i = 0; i < urls.length; i++) {
            if (u.toString().equalsIgnoreCase(urls[i].toString())) {
                return;
            }
        }
        Class sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            boolean accessible = method.isAccessible();
            method.setAccessible(true);
            method.invoke(sysLoader, new Object[]{u});
            method.setAccessible(accessible);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }

    private void processValidator(Class<?> c, FacesValidator facesValidator) {
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
                        new ManagedBeanInfo.ManagedProperty(entry.getKey(), f.getType().getName(), property.value(), null, null);
                properties.add(propertyInfo);
            }
        }

        manager.register(new ManagedBeanInfo(name, c.getName(), scope, eager, null, null, properties, null));
    }

    private String getManagedBeanName(Class<?> c, ManagedBean mb) {
        String name = mb.name();
        if (name.length() == 0) {
            String t = c.getName();
            name = t.substring(t.lastIndexOf('.') + 1).substring(0, 1).toLowerCase() + name.substring(1);
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