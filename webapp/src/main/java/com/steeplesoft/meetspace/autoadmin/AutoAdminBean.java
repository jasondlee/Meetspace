package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.ControllerBean;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.inject.Named;
import javax.persistence.TemporalType;
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
    public static final String NAV_BASE = "/autoAdmin";
    public static final String NAV_ADD = "/autoAdmin/form";
    public static final String NAV_EDIT = "/autoAdmin/form";
    public static final String NAV_LIST = "/autoAdmin/list";
    public static final String NAV_VIEW = "/autoAdmin/view";

    private Set<String> modelPackages = new TreeSet<String>();
    private Set<String> modelClasses = new TreeSet<String>();

    public AutoAdminBean() {
        modelClasses.add("Meeting");
        modelClasses.add("GroupMember");
        modelClasses.add("Sponsor");

        modelPackages.add("com.steeplesoft.meetspace.model");
    }

    public String getListViewId() {
        return NAV_LIST;
    }

    public String getAddViewId() {
        return NAV_ADD;
    }

    public String getEditViewId() {
        return NAV_EDIT;
    }

    public String getViewViewId() {
        return NAV_VIEW;
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

    public Class getModelClass(String className) {
        Class clazz = null;

        for (String pkg : modelPackages) {
            try {
                clazz = Class.forName(pkg + "." + className);
                break;
            } catch (ClassNotFoundException e) {
                //
            }
        }

        if (clazz == null) {
            throw new RuntimeException("The class '" + className + "' could not be found in any of the configured packages.");
        }

        return clazz;
    }
}