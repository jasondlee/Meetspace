package com.steeplesoft.meetspace.test;

import com.steeplesoft.meetspace.plugins.engine.ClassloaderPluginEngine;
import com.steeplesoft.meetspace.plugins.PluginMetadata;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class PluginEngineTest {
    private static ClassloaderPluginEngine pe;

    @BeforeClass
    public static void before() throws IOException {
        Set<String> packages = new TreeSet<String>();
        packages.add("com.steeplesoft.meetspace.test");
        pe = new ClassloaderPluginEngine("/Users/jasonlee/src/jug/meetspace/plugin-engine/target/plugins", packages);
    }

    @Test
    public void testEngine() {
        List<Class<?>> pluginClasses = pe.getPluginClassList("dummy");
        assertEquals(pluginClasses.get(0), DummyPlugin.class);
        List<Object> plugins = pe.getPlugins("dummy");
        assertTrue(plugins.get(0) instanceof DummyPlugin);
    }

    @Test
    public void testMetadata() {
        List<Object> plugins = pe.getPlugins("dummy");
        PluginMetadata pd = new PluginMetadata(plugins.get(0));
        assertEquals(pd.getType(), DummyPlugin.TYPE);
        assertEquals(pd.getName(), DummyPlugin.NAME);
        assertEquals(pd.getDescription(), DummyPlugin.DESCRIPTION);
    }

    @Test
    public void testMissingType() {
        try {
            List<Object> plugins = pe.getPlugins("missing");
            assertTrue(plugins.size() == 0);
        } catch (Exception e) {
            fail();
        }
    }
}