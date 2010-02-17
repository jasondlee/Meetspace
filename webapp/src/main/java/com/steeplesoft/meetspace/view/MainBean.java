/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Preference;
import com.steeplesoft.meetspace.model.Sponsor;
import com.steeplesoft.meetspace.service.MainService;
import com.steeplesoft.meetspace.service.PreferencesService;
import java.io.Serializable;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jasonlee
 */
@Named("main")
public class MainBean implements Serializable {
    @Inject
    private PreferencesService prefsService;
    @Inject
    private MainService mainService;
    private Sponsor sponsor ;

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

    public Sponsor getRandomSponsor() {
        synchronized(this) {
            if (this.sponsor == null) {
                sponsor = mainService.getRandomSponsor();
            }
        }

        return sponsor;
    }
}