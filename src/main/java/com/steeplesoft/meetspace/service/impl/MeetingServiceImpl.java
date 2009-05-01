/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service.impl;

import com.steeplesoft.meetspace.service.*;
import com.steeplesoft.meetspace.model.Meeting;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author jasonlee
 */
public class MeetingServiceImpl implements MeetingService {

    @PersistenceContext //(name="em", type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    public Meeting getUpcomingMeeting() {
        Meeting meeting = (Meeting)em.createNamedQuery("nextMeeting")
                .getSingleResult();
        System.err.println("Meeting = " + meeting);

        return meeting;
    }

    public Meeting getMeeting(Long meetingId) {
        return (Meeting)em.createNamedQuery("getMeeting")
                .setParameter("meetingId", meetingId)
                .getSingleResult();
    }
}
