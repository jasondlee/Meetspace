/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Preference;
import com.steeplesoft.meetspace.service.PreferencesService;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author jasonlee
 */
//@ManagedBean(eager=true)
//@ApplicationScoped
public class MeetSpaceBean {
    @Inject
    PreferencesService prefsService;

    @PostConstruct
    public void checkForDefaults() {
        Map<String, Preference> prefs = prefsService.getPreferences();
        if (prefs.get("theme") == null) {
            prefs.put("theme", new Preference("theme", "default"));
        }

//        prefsService.savePreferences();
    }
}
