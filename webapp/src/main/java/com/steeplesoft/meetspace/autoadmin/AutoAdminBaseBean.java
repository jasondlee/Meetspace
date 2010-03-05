package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.ControllerBean;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

public abstract class AutoAdminBaseBean  {
    @Inject
    private AutoAdminBean adminBean;

    public String getListViewId() {
        return adminBean.getListViewId();
    }

    public String getAddViewId() {
        return adminBean.getAddViewId();
    }

    public String getEditViewId() {
        return adminBean.getEditViewId();
    }

    public String getViewViewId() {
        return adminBean.getViewViewId();
    }
}