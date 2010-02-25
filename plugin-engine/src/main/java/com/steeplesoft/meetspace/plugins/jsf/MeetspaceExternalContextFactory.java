package com.steeplesoft.meetspace.plugins.jsf;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextFactory;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 25, 2010
 * Time: 8:49:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class MeetspaceExternalContextFactory extends ExternalContextFactory {
    private ExternalContextFactory parent;

    public MeetspaceExternalContextFactory (ExternalContextFactory parent) {
        super();
        this.parent = parent;
    }

    @Override
    public ExternalContext getExternalContext(Object context, Object request, Object response) throws FacesException {
        return new MeetspaceExternalContext(getWrapped().getExternalContext(context, request, response));
    }

    @Override
    public ExternalContextFactory getWrapped() {
        return parent;
    }
}
