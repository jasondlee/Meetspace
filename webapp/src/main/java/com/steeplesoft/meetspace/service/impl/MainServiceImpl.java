/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service.impl;

import com.steeplesoft.meetspace.model.GroupMember;
import com.steeplesoft.meetspace.model.Registration;
import com.steeplesoft.meetspace.service.MainService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.List;

/**
 * This will be renamed once I can brand this beast. :)
 *
 * @author jasonlee
 */
@Named
@ApplicationScoped
public class MainServiceImpl implements MainService, Serializable {
    @Inject
    private UserTransaction txn;
    @PersistenceContext(name = "em")
    private EntityManager em;

    public GroupMember getMember(Long id) {
        return em.find(GroupMember.class, id);
    }

    @Override
    public void saveRegistration(Registration reg) {
        try {
            txn.begin();
            if (!em.contains(reg)) {
                reg = em.merge(reg);
            }
            em.persist(reg);
            em.flush();
            txn.commit();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            try {
                txn.commit();
            } catch (Exception e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public Long saveMember(GroupMember member) {
        if (!em.contains(member)) {
            member = em.merge(member);
        }
        em.persist(member);
        em.flush();
        return member.getId();
    }

    public List<GroupMember> getMembers() {
        return em.createNamedQuery("allMembers").getResultList();
    }
}
