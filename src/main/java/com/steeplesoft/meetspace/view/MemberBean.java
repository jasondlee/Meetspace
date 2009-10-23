/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.GroupMember;
import com.steeplesoft.meetspace.service.impl.DataAccessController;
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
    @Inject
    private DataAccessController dataAccess;
    private DataModel dataModel;
    private int rowsPerPage = 3;
    
    Paginator paginator;

    public void resetList() {
        dataModel = null;
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