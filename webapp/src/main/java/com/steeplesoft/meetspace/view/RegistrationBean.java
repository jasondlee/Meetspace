/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Meeting;
import com.steeplesoft.meetspace.model.Registration;
import com.steeplesoft.meetspace.service.MainService;
import com.steeplesoft.meetspace.service.MeetingService;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author jasonlee
 */
@Named("regBean")
@RequestScoped
public class RegistrationBean implements Serializable {
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

        if (meetingId == null) {
            String id = facesContext.getExternalContext().getRequestParameterMap().get("meetingId");
            meetingId = Long.parseLong(id);
        }
        if (meetingId != null) {
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
        registration.setMeeting(getMeeting());
        registration = mainService.saveRegistration(registration);
        if (registration == null) {
            return null;
        } else {
            return "/home";
        }
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }
}
