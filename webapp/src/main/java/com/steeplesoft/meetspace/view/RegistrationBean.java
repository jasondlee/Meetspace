/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Meeting;
import com.steeplesoft.meetspace.model.Registration;
import com.steeplesoft.meetspace.service.MainService;
import com.steeplesoft.meetspace.service.MeetingService;
import com.steeplesoft.meetspace.view.util.JsfUtil;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author jasonlee
 */
@Named("registrationBean")
@RequestScoped
public class RegistrationBean implements Serializable {

    @Inject
    private MeetingService meetingService;

    @Inject
    private MainService mainService;

    @ManagedProperty(value = "#{param.meetingId}")
    private Long meetingId;

    private Registration registration = new Registration();

    private Meeting meeting;
    private String captchaText;

    public Meeting getMeeting() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();

        if (meetingId != null) {
            meeting = meetingService.getMeeting(meetingId);
        } else {
            facesContext.responseComplete();
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, "*", "/home");
            //getExternalContext().redirect("/home");
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
            JsfUtil.addSuccessMessage(registration.getFullName() + " has been successfully registered for the meeting.");
            return "/home?faces-redirect=true";
        }
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }
}
