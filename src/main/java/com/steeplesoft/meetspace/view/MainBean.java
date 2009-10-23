/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Preference;
import com.steeplesoft.meetspace.service.PreferencesService;
import java.io.Serializable;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jasonlee
 */
@Named("main")
@SessionScoped
public class MainBean implements Serializable {
    @Inject
    private PreferencesService prefsService;

    public MainBean() {
        System.out.println(this.getClass().getSimpleName() + " was constructed");
    }

    public Map<String, Preference> getPreferences() {
        Map<String, Preference> preferences = prefsService.getPreferences();
        return preferences;
    }

    public PreferencesService getPrefsService() {
        return prefsService;
    }

    public void setPrefsService(PreferencesService prefsService) {
        this.prefsService = prefsService;
    }
}