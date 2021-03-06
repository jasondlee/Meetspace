/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steeplesoft.meetspace.service.impl;

import com.steeplesoft.meetspace.service.*;
import com.steeplesoft.meetspace.model.Meeting;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 *
 * @author jasonlee
 */
@Named
@ApplicationScoped
public class MeetingServiceImpl implements MeetingService, Serializable {
    @PersistenceContext//(name = "em")
    private EntityManager em;

    @Override
    public Meeting getUpcomingMeeting() {
        try {
            Meeting meeting = (Meeting) em.createNamedQuery("nextMeeting").getResultList().get(0);
            System.err.println("Meeting = " + meeting);
            return meeting;
        } catch (NoResultException nre) {
        }
        return null;

    }

    @Override
    public Meeting getMeeting(Long meetingId) {
        return (Meeting) em.createNamedQuery("getMeeting").setParameter("meetingId", meetingId).getSingleResult();
    }
}
