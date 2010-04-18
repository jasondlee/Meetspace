/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Preference;
import com.steeplesoft.meetspace.service.PreferencesService;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author jasonlee
 */
//@ManagedBean(eager=true)
//@ApplicationScoped
public class MeetSpaceBean implements Serializable {
    @Inject
    private PreferencesService prefsService;

    private String meetspaceHome;

    @PostConstruct
    public void checkForDefaults() {
        meetspaceHome = System.getenv("MEETSPACE_HOME");
        if (meetspaceHome == null) {
            meetspaceHome = System.getProperty("user.home") + File.separator + ".meetspace";
        }

        Map<String, Preference> prefs = prefsService.getPreferences();
        if (prefs.get("theme") == null) {
            prefs.put("theme", new Preference("theme", "default"));
        }

//        prefsService.savePreferences();
    }

    public String getHome() {
        return meetspaceHome;
    }
}
