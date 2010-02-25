package com.steeplesoft.meetspace.plugins.jsf;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 25, 2010
 * Time: 6:58:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class MeetspaceExternalContext extends ExternalContextWrapper {
    private ExternalContext wrapped;

    public MeetspaceExternalContext(ExternalContext wrapped) {
        this.wrapped = wrapped;
    }

    public URL getResource(String path) throws MalformedURLException {
        System.out.println("Looking for " + path);
        URL url = Thread.currentThread().getContextClassLoader().getResource(path.substring(1));
        if (url == null) {
            url = getWrapped().getResource(path);
        }

        return url;
    }

    @Override
    public ExternalContext getWrapped() {
        return wrapped;
    }
}
