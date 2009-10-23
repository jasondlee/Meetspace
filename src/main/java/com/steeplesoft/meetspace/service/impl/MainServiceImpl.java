/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service.impl;

import com.steeplesoft.meetspace.model.GroupMember;
import com.steeplesoft.meetspace.service.*;
import com.steeplesoft.meetspace.model.Registration;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This will be renamed once I can brand this beast. :)
 * @author jasonlee
 */
@Named
@ApplicationScoped
public class MainServiceImpl implements MainService {
    @PersistenceContext(name = "em")
    protected EntityManager em;

    public GroupMember getMember(Long id) {
        return em.find(GroupMember.class, id);
    }

    @Override
    public void saveRegistration(Registration reg) {
//        create(reg);
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
