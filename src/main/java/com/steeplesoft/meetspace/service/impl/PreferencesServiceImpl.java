/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steeplesoft.meetspace.service.impl;

import com.steeplesoft.meetspace.service.PreferencesService;
import com.steeplesoft.meetspace.model.Preference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jasonlee
 */
public class PreferencesServiceImpl implements PreferencesService {

    @PersistenceContext(name = "em")//, type = PersistenceContextType.EXTENDED)
    private EntityManager em;
    private Map<String, Preference> prefs;

    public Map<String, Preference> getPreferences() {
        synchronized (this) {
            if (prefs == null) {
                prefs = new ConcurrentHashMap<String, Preference>();
                Query query = em.createQuery("SELECT p from Preference p");
                List<Preference> list = query.getResultList();
                for (Preference pref : list) {
                    prefs.put(pref.getName(), pref);
                }
            }
        }
        return prefs;
    }

    public void savePreferences() {
        for (Preference pref : prefs.values()) {
            em.merge(pref);
            em.persist(pref);
        }
        em.flush();
    }
}
