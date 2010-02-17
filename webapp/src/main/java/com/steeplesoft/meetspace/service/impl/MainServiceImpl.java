/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service.impl;

import com.steeplesoft.meetspace.model.GroupMember;
import com.steeplesoft.meetspace.model.Registration;
import com.steeplesoft.meetspace.model.Sponsor;
import com.steeplesoft.meetspace.service.MainService;

import javax.enterprise.context.SessionScoped;
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
@SessionScoped
public class MainServiceImpl implements MainService {
    @Inject
    private UserTransaction txn;
    
    @PersistenceContext(unitName="em")//(name = "em")
    private EntityManager em;

    public GroupMember getMember(Long id) {
        return em.find(GroupMember.class, id);
    }

    @Override
    public Registration saveRegistration(Registration reg) {
        try {
            txn.begin();
            if (!em.contains(reg)) {
                reg = em.merge(reg);
            }
            em.persist(reg);
            em.flush();
            txn.commit();
            return reg;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            try {
                txn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            throw new RuntimeException(e);
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

    @Override
    public Sponsor getRandomSponsor() {
        Sponsor sponsor = null;
        List<Sponsor> sponsors = em.createNamedQuery("Sponsor.activeSponsors").getResultList();
        if ((sponsors != null) && (sponsors.size() > 0)) {
            long index = Math.round(Math.random() * (sponsors.size()-1));
            sponsor = sponsors.get((int)index);
        }

        return sponsor;
    }
}
