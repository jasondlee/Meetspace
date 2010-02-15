/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Meeting;
import com.steeplesoft.meetspace.model.Registration;
import com.steeplesoft.meetspace.service.MainService;
import com.steeplesoft.meetspace.service.MeetingService;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;

/**
 *
 * @author jasonlee
 */
@Named("regBean")
@RequestScoped
public class RegistrationBean {
    @Inject
    private MeetingService meetingService;
    @Inject
    private MainService mainService;

    @ManagedProperty(value="#{param.meetingId}")
    private Long meetingId;

    private Registration registration = new Registration();
    private Meeting meeting;

    public Meeting getMeeting() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        String id = facesContext.getExternalContext().getRequestParameterMap().get("meetingId");
        if (id != null) {
            meetingId = Long.parseLong(id);
            meeting = meetingService.getMeeting(meetingId);
        } else {
            try {
                facesContext.getExternalContext().redirect("/");
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
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