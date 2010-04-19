/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.BlogEntry;
import com.steeplesoft.meetspace.model.Preference;
import com.steeplesoft.meetspace.model.Sponsor;
import com.steeplesoft.meetspace.service.MainService;
import com.steeplesoft.meetspace.service.PreferencesService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jasonlee
 */
@Named("main")
@RequestScoped
public class MainBean implements Serializable {
    @Inject
    private PreferencesService prefsService;
    @Inject
    private MainService mainService;
    private Sponsor sponsor ;

    public MainBean() {
        System.out.println(this.getClass().getSimpleName() + " was constructed");
    }

    @PostConstruct
    public void setup() {
        sponsor = mainService.getRandomSponsor();
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
        return sponsor;
    }

    public List<BlogEntry> getRecentBlogEntries() {
        return mainService.getMostRecentBlogEntries(10);
    }
}