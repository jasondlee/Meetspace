package com.steeplesoft.meetspace.view;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 27, 2010
 * Time: 8:22:51 AM
 * To change this template use File | Settings | File Templates.
 */
@Named(value = "autoAdmin")
@SessionScoped
public class AutoAdminBean implements Serializable {
    private String modelPackage = "com.steeplesoft.meetspace.model";
    private Set<String> modelClasses = new TreeSet<String>();
    public AutoAdminBean() {
        modelClasses.add("Meeting");
        modelClasses.add("GroupMember");
        modelClasses.add("Sponsor");
    }

    public Set<String> getModelClasses() {
        return modelClasses;
    }

    public void setModelClasses(Set<String> modelClasses) {
        this.modelClasses = modelClasses;
    }

    public List getModelList() {
        return new ArrayList(modelClasses);
    }
}
