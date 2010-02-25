/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Meeting;
import com.steeplesoft.meetspace.service.MeetingService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author jasonlee
 */
@Named("meetingBean")
@SessionScoped
public class MeetingBean extends ControllerBean {
    public static final String NAV_ADD  = "/admin/meetings/form";
    public static final String NAV_EDIT = "/admin/meetings/form";
    public static final String NAV_LIST = "/admin/meetings/list";
    public static final String NAV_VIEW = "/admin/meetings/view";

    @Inject
    private MeetingService meetingService;
    private Meeting nextMeeting = null;

    public Meeting getNextMeeting() {
        if (nextMeeting == null) {
            nextMeeting = meetingService.getUpcomingMeeting();
        }

        return nextMeeting;
    }

    public MeetingService getMeetingService() {
        return meetingService;
    }

    public void setMeetingService(MeetingService meetingService) {
        this.meetingService = meetingService;
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

    @Override
    public Class getEntityClass() {
        return Meeting.class;
    }

    @Override
    public String edit() {
        nextMeeting = null;
        return super.edit();
    }
}