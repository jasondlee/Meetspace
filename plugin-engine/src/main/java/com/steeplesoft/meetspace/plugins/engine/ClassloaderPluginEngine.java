package com.steeplesoft.meetspace.plugins.engine;

import com.steeplesoft.meetspace.plugins.Plugin;
import com.steeplesoft.meetspace.plugins.PluginMetadata;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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

    public ClassloaderPluginEngine(String pluginDir) {
        this(pluginDir, (Set<String>) null);
    }

    public ClassloaderPluginEngine(String pluginDir, String pkg) {
        this.pluginDir = pluginDir;
        this.packages = new TreeSet<String>();
        packages.add(pkg);

        scanForPlugins();
    }

    public ClassloaderPluginEngine(String pluginDir, Set<String> packages) {
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

    protected synchronized void scanForPlugins() {
        scanPackages();
        scanPluginDir();
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

    protected void scanPluginDir() {
        if (pluginDir != null) {
            File dir = new File(pluginDir);
            if (!dir.exists()) {
                return;
            }

            File[] jars = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return ((name != null) && (name.endsWith(".jar")));
                }
            });
            for (File jar : jars) {
                try {
                    addUrl(jar.toURL());
                    processJar(jar);
                } catch (IOException e) {
                    e.printStackTrace();
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

    protected void processClass(File file, String name) {
        name = convertToClassName(name);
        Class<?> c = null;
        if (file != null) {
            c = loadClass(file, name);
        } else {
            try {
                c = getClass().getClassLoader().loadClass(name);
                //Thread.currentThread().getContextClassLoader().loadClass(name);
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

    protected void postProcessClass(Class<?> c) {
        //noop
    }

    protected static String convertToClassName(String name) {
        return name.substring(0, name.length() - 6).replace(',', '.');
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
}