/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service.impl;

import com.steeplesoft.meetspace.service.*;
import com.steeplesoft.meetspace.model.Registration;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This will be renamed once I can brand this beast. :)
 * @author jasonlee
 */
public class MainServiceImpl implements MainService {
    @PersistenceContext(name="em")
    private EntityManager em;

    public void saveRegistration(Registration reg) {
        em.persist(reg);
    }
}
