package com.steeplesoft.meetspace.test;

import com.steeplesoft.jsf.facestester.FacesTester;
import com.steeplesoft.jsf.facestester.FacesTesterException;
import com.steeplesoft.meetspace.plugins.JsfPluginEngine;
import com.steeplesoft.meetspace.plugins.PluginEngine;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.faces.context.FacesContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 15, 2010
 * Time: 9:41:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsfPluginEngineTest  {
    private static FacesTester ft;
    private static File fakeWebAppDir;
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

    protected static void createTempFile(String contents) {
        try {
            File file = new File (fakeWebAppDir, "web.xml");
            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);
            os.write(contents.getBytes());
            os.close();
            file.deleteOnExit(); // just in case :)
        } catch (IOException ex) {
            throw new FacesTesterException("Unable to create temporary file", ex);
        }
    }
}
