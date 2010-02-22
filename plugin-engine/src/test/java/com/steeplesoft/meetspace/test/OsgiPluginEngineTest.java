/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.test;

import com.steeplesoft.meetspace.plugins.engine.OsgiPluginEngine;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jasonlee
 */
public class OsgiPluginEngineTest {
    @Test
    public void testOsgiPluingEngine() throws IOException {
        OsgiPluginEngine engine = new OsgiPluginEngine("/Users/jasonlee/src/glassfish/test/glassfishv3/glassfish/modules");
        Assert.assertNotNull(engine);
    }
}
