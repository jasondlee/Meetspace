package com.steeplesoft.meetspace.test;

import com.steeplesoft.jsf.facestester.FacesTester;
import com.steeplesoft.meetspace.plugins.engine.JsfPluginEngine;
import com.steeplesoft.meetspace.plugins.engine.ClassloaderPluginEngine;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.faces.context.FacesContext;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class JsfPluginEngineTest  {
    private static FacesTester ft;
//    private static ClassloaderPluginEngine pe;

    @BeforeClass
    public static void setup() throws IOException {
//        ft = new FacesTester();
//        FacesContext fc = ft.getFacesContext(); // Prime the pump! :)
//        pe = new JsfPluginEngine("src/test/webapp/WEB-INF/lib");
    }

    @Test
    public void testManagedBeanLoading() {
//        Object mb = ft.getManagedBean("testManagedBean");
//        assertNotNull(mb);
    }
}
