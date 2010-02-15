package com.steeplesoft.meetspace.test;

import com.steeplesoft.jsf.facestester.FacesTester;
import com.steeplesoft.meetspace.plugins.JsfPluginEngine;
import com.steeplesoft.meetspace.plugins.PluginEngine;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.faces.context.FacesContext;

import static org.junit.Assert.assertNotNull;

public class JsfPluginEngineTest  {
    private static FacesTester ft;
    private static PluginEngine pe;

    @BeforeClass
    public static void setup() {
        ft = new FacesTester();
        FacesContext fc = ft.getFacesContext(); // Prime the pump! :)
        pe = new JsfPluginEngine("src/test/webapp/WEB-INF/lib");
    }

    @Test
    public void testManagedBeanLoading() {
        Object mb = ft.getManagedBean("testManagedBean");
        assertNotNull(mb);
    }
}
