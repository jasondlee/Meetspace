package com.steeplesoft.meetspace.test;

import com.steeplesoft.meetspace.plugins.HelloWorldPlugin;
import com.steeplesoft.meetspace.view.PluginBean;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 12, 2010
 * Time: 9:08:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class PluginBeanTest {
    private static PluginBean pb;
    @BeforeClass
    public static void setup() throws IOException {
        pb = new PluginBean();
    }

    @Test
    public void testPluginBean() {
//        List<Object> plugins = pb.getPlugins("demo");
//        assertTrue(plugins.get(0) instanceof HelloWorldPlugin);
    }

    @Test
    public void testGetByTypeAndName() {
//        assertTrue(pb.getPlugin("demo", "helloWorld") instanceof HelloWorldPlugin);
    }
}
