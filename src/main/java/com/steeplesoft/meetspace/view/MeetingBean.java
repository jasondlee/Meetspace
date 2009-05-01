/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Meeting;
import com.steeplesoft.meetspace.service.MeetingService;
import javax.faces.model.ManagedBean;
import javax.faces.model.RequestScoped;

/**
 *
 * @author jasonlee
 */
@ManagedBean(name="meeting")
@RequestScoped
public class MeetingBean {
    MeetingService meetingService;
    Meeting nextMeeting = null;

    public Meeting getNextMeeting() {
        if (nextMeeting == null) {
            nextMeeting = meetingService.getUpcomingMeeting();
        }

        return nextMeeting;
    }
}
