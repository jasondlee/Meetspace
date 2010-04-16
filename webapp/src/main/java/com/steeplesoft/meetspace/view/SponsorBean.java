/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Sponsor;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author jasonlee
 */
@Named
@SessionScoped
public class SponsorBean extends ControllerBean {

    public static final String NAV_ADD = "/admin/sponsors/form";

    public static final String NAV_EDIT = "/admin/sponsors/form";

    public static final String NAV_LIST = "/admin/sponsors/list";

    public static final String NAV_VIEW = "/admin/sponsors/view";

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

    @Override
    public Class getEntityClass() {
        return Sponsor.class;
    }
}
