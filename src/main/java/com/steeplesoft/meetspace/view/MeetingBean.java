/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Meeting;
import com.steeplesoft.meetspace.service.MeetingService;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jasonlee
 */
@Named("meeting")
@RequestScoped
public class MeetingBean {
//    @ManagedProperty("#{meetingService}")
//    @EJB(mappedName="ejb/meetingService")
    @Inject
    MeetingService meetingService;
    Meeting nextMeeting = null;

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
}