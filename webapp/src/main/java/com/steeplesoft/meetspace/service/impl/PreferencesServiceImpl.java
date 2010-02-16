/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steeplesoft.meetspace.service.impl;

import com.steeplesoft.meetspace.service.PreferencesService;
import com.steeplesoft.meetspace.model.Preference;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jasonlee
 */
@Named
@ApplicationScoped
public class PreferencesServiceImpl implements PreferencesService, Serializable {
    @PersistenceContext//(name = "em")
    protected EntityManager em;

    public PreferencesServiceImpl() {
    }

    @Override
    public Map<String, Preference> getPreferences() {
        Map<String, Preference> prefs = new ConcurrentHashMap<String, Preference>();
        Query query = em.createQuery("SELECT p from Preference p");
        List<Preference> list = query.getResultList();
        for (Preference pref : list) {
            prefs.put(pref.getName(), pref);
        }
        return prefs;
    }

    @Override
    public void savePreferences(Map<String, Preference> prefs) {
        em.getTransaction().begin();
        for (Preference pref : prefs.values()) {
            em.merge(pref);
            em.persist(pref);
        }
        em.flush();
        em.getTransaction().commit();
        em.close();
    }
}