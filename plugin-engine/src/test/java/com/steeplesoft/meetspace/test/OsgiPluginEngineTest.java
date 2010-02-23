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
        // This has to be run from Maven right now (or manually set up. See pom.xml)
        OsgiPluginEngine engine = new OsgiPluginEngine(
//                "/Users/jasonlee/src/glassfish/test/glassfishv3/glassfish/modules");
                "plugin-engine/target/plugins");
        Assert.assertNotNull(engine);
    }
}
