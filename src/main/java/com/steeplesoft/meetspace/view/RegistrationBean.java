/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Meeting;
import com.steeplesoft.meetspace.model.Registration;
import com.steeplesoft.meetspace.service.MainService;
import com.steeplesoft.meetspace.service.MeetingService;
import javax.faces.model.ManagedBean;
import javax.faces.model.ManagedProperty;
import javax.faces.model.RequestScoped;

/**
 *
 * @author jasonlee
 */
@ManagedBean(name="regBean")
@RequestScoped
public class RegistrationBean {
    private MeetingService meetingService;
    private MainService mainService;

    @ManagedProperty(value="#{param.meetingId}")
    private Long meetingId;

    private Registration registration = new Registration();
    private Meeting meeting;

    public Meeting getMeeting() {
        meeting = meetingService.getMeeting(meetingId);
        return meeting;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public String saveRegistration() {
        registration.setMeeting(meeting);
        mainService.saveRegistration(registration);
        return "/home";
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }
}
