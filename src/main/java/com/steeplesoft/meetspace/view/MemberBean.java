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
public class MemberBean implements Serializable {
    public static final String NAV_ADD = "/admin/members/form";
    public static final String NAV_EDIT = "/admin/members/form";
    public static final String NAV_LIST = "/admin/members/list";
    public static final String NAV_VIEW = "/admin/members/view";
    public static final String NAV_REDIRECT = "?faces-redirect=true";

    @Inject
    private DataAccessController dataAccess;
    private DataModel dataModel;
    private int rowsPerPage = 5;
    private GroupMember current;
    private int selectedItemIndex = -1;

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
        resetList();
    }
    
    Paginator paginator;

    public void resetList() {
        dataModel = null;
    }

    public void resetPagination(ValueChangeEvent vce) {
        paginator = null;
    }

    public Paginator getPaginator() {
        if (paginator == null) {
            paginator = new Paginator(rowsPerPage) {

                @Override
                public int getItemsCount() {
                    return dataAccess.count(GroupMember.class);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(dataAccess.findRange(GroupMember.class, getPageFirstItem(), getPageFirstItem()+getPageSize()));
                }
            };
        }

        return paginator;

    }

    public void next() {
        getPaginator().nextPage();
        resetList();
    }

    public void previous() {
        getPaginator().previousPage();
        resetList();
    }

    //**************************************************************************
    // prepareFoo and Foo action methods
    public String prepareList() {
        resetList();
        return NAV_LIST + NAV_REDIRECT;
    }

    public DataModel getList() {
        if (dataModel == null) {
            dataModel = getPaginator().createPageDataModel();
        }

        return dataModel;
    }

    public String prepareCreate() {
        current = new GroupMember();
        selectedItemIndex = -1;
        return NAV_ADD + NAV_REDIRECT;
    }

    public String create() {
        try {
            dataAccess.create(current);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GroupMemberCreated")); // Left as an example :)
            JsfUtil.addSuccessMessage("Group member created");
            return NAV_LIST + NAV_REDIRECT;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, "A persistence error occurred.");
            return null;
        }
    }

    public String prepareEdit() {
        current = (GroupMember)getList().getRowData();
        selectedItemIndex = paginator.getPageFirstItem() + getList().getRowIndex();
        return NAV_EDIT + NAV_REDIRECT;
    }

    public String edit() {
        try {
            dataAccess.edit(current);
            JsfUtil.addSuccessMessage("Group member updated");
            return NAV_LIST + NAV_REDIRECT;
        } catch (Exception e) {
           JsfUtil.addErrorMessage(e, "A persistence error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    public String delete() {
        current = (GroupMember)getList().getRowData();
        selectedItemIndex = paginator.getPageFirstItem() + getList().getRowIndex();
        try {
            dataAccess.remove(current);
            JsfUtil.addSuccessMessage("Group member deleted");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "A persistence error occurred.");
        }
        resetList();
        return NAV_LIST + NAV_REDIRECT;
    }

    public String prepareView() {
        current = (GroupMember)getList().getRowData();
        selectedItemIndex = paginator.getPageFirstItem() + getList().getRowIndex();
        return NAV_VIEW + NAV_REDIRECT;
    }

    public String checkViewData() {
        // If an ID is in the URL, that overrides what might be in the session state
        String id = JsfUtil.getRequestParameter("id");
        if (id != null) {
            current = dataAccess.find(GroupMember.class, Long.valueOf(id));
        }

        if (current != null) {
            return NAV_VIEW + NAV_REDIRECT;
        } else {
            return NAV_LIST + NAV_REDIRECT;
        }
    }

    public GroupMember getSelected() {
        if (current == null) {
            current = new GroupMember();
            selectedItemIndex = -1;
        }
        return current;
    }
}