/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jasonlee
 */
@Named("test")
@SessionScoped
public class DataAccessController implements Serializable {
    @PersistenceContext(name = "em")
    protected EntityManager em;

    public <T> void create(T entity) {
        em.persist(entity);
    }

    public <T> void edit(T entity) {
        em.merge(entity);
    }

    public <T> void remove(T entity) {
        em.remove(em.merge(entity));
    }

    public <T> T find(Class<T> clazz, Object id) {
        return em.find(clazz, id);
    }

    public Query createQuery(String query) {
        return em.createNamedQuery(query);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(clazz));
        return em.createQuery(cq).getResultList();
    }

    public <T> List<T> findRange(Class<T> clazz, int begin, int end) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(clazz));
        Query q = em.createQuery(cq);
        q.setMaxResults(end - begin);
        q.setFirstResult(begin);
        return q.getResultList();
    }

    public <T> int count(Class<T> clazz) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(clazz);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
