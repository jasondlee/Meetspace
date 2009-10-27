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
import javax.faces.event.ComponentSystemEvent;
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

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
        resetList();
        resetPagination(null);
    }
    private int selectedItemIndex = -1;
    
    Paginator paginator;

    public void resetList() {
        dataModel = null;
    }

    public String prepareList() {
        resetList();
        return NAV_LIST + NAV_REDIRECT;
    }

    public void resetPagination(ComponentSystemEvent event) {
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

    public DataModel getMembers() {
        if (dataModel == null) {
            dataModel = getPaginator().createPageDataModel();
        }

        return dataModel;
    }

    public void next() {
        getPaginator().nextPage();
        resetList();
    }

    public void previous() {
        getPaginator().previousPage();
        resetList();
    }

    public String add() {
        current = new GroupMember();
        selectedItemIndex = -1;
        return NAV_ADD + NAV_REDIRECT;
    }

    public String edit() {
        current = (GroupMember)getMembers().getRowData();
        selectedItemIndex = paginator.getPageFirstItem() + getMembers().getRowIndex();
        return NAV_EDIT + NAV_REDIRECT;
    }

    public String view() {
        current = (GroupMember)getMembers().getRowData();
        selectedItemIndex = paginator.getPageFirstItem() + getMembers().getRowIndex();
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

    public String create() {
        try {
            dataAccess.create(current);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GroupMemberCreated"));
            return NAV_LIST + NAV_REDIRECT;
        } catch (Exception e) {
            e.printStackTrace();
//            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update() {
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
        current = (GroupMember)getMembers().getRowData();
        selectedItemIndex = paginator.getPageFirstItem() + getMembers().getRowIndex();
        performDestroy();
        resetList();
        return NAV_LIST + NAV_REDIRECT;
    }

    private void performDestroy() {
        try {
            dataAccess.remove(current);
            JsfUtil.addSuccessMessage("Group member deleted");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "A persistence error occurred.");
        }
    }

    /*
    @Inject
    private MainService mainService;
    private GroupMember member = new GroupMember();


    @ManagedProperty("#{param.id}")
    private Long memberId;

    @PostConstruct
    public void initMember() {
        if (memberId != null) {
            member = mainService.getMember(memberId);
        }
    }

    public GroupMember getMember() {
        return member;
    }

    public void setMember(GroupMember member) {
        this.member = member;
    }

    public String save() {
        mainService.saveMember(member);
        FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("The member '" + member.getFirstName() + " " +
                member.getLastName() + "' was successfully saved"));
        return "memberList";
    }

    public MainService getMainService() {
        return mainService;
    }

    public void setMainService(MainService mainService) {
        this.mainService = mainService;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    */
}