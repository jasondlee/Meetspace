/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.plugins.engine;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTrackerCustomizer;

/**
 *
 * @author jasonlee
 */
public class MeetspaceBundleTracker implements BundleTrackerCustomizer {
    private static Logger logger = Logger.getLogger(MeetspaceBundleTracker.class.getName());

    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        System.out.println("Tracking bundle " + bundle.getSymbolicName());
        try {
            Enumeration<URL> e = bundle.getClass().getClassLoader().getResources("");
            while (e.hasMoreElements()) {
                URL url = e.nextElement();
                processFile(new File(url.toURI()));
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return bundle;
    }

    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
        //
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        //
    }

    protected void processFile(File file) {
        try {
            File files[]  = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    processFile(f);
                } else {
                    System.out.println(f.getCanonicalPath());
                }
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

}