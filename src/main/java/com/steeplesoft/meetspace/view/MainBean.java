/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Preference;
import com.steeplesoft.meetspace.service.PreferencesService;
import java.util.Map;
import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;

/**
 *
 * @author jasonlee
 */
@ManagedBean(name="main")
@SessionScoped
public class MainBean {
    private PreferencesService prefsService;

    public Map<String, Preference> getPreferences() {
        Map<String, Preference> prefs = prefsService.getPreferences();
        return prefsService.getPreferences();
    }
}
