/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.GroupMember;
import com.steeplesoft.meetspace.service.impl.DataAccessController;
import com.steeplesoft.meetspace.view.util.JsfUtil;
import com.steeplesoft.meetspace.view.util.Paginator;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jasonlee
 */
@Named("memberBean")
@SessionScoped
public class MemberBean extends ControllerBean implements Serializable {
    public static final String NAV_ADD = "/admin/members/form";
    public static final String NAV_EDIT = "/admin/members/form";
    public static final String NAV_LIST = "/admin/members/list";
    public static final String NAV_VIEW = "/admin/members/view";

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
        return GroupMember.class;
    }
}