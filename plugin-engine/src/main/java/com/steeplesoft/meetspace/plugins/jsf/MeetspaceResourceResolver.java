package com.steeplesoft.meetspace.plugins.jsf;

import javax.faces.view.facelets.ResourceResolver;
import java.net.URL;

public class MeetspaceResourceResolver extends ResourceResolver {
    private ResourceResolver parent;

    public MeetspaceResourceResolver(ResourceResolver parent) {
        this.parent = parent;
    }

    @Override
    public URL resolveUrl(String path) {
        URL url = null;
        try {
            url = url = Thread.currentThread().getContextClassLoader().getResource(path.substring(1));
            if (url == null) {
                url = parent.resolveUrl(path);
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return url;
    }
}
