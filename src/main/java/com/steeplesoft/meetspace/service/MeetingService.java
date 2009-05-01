/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service;

import com.steeplesoft.meetspace.model.Meeting;

/**
 *
 * @author jasonlee
 */
public interface MeetingService {
    public Meeting getUpcomingMeeting();
    public Meeting getMeeting(Long meetingId);

}
