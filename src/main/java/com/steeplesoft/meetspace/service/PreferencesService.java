/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service;

import com.steeplesoft.meetspace.model.Preference;
import java.util.Map;

/**
 *
 * @author jasonlee
 */
public interface PreferencesService {
    public Map<String, Preference> getPreferences();
    public void savePreferences();
}
