/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service.impl;

import com.steeplesoft.meetspace.service.BlogService;
import com.steeplesoft.meetspace.model.BlogEntry;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jasonlee
 */
public class BlogServiceImpl implements BlogService {
    @PersistenceContext(name="em")
    EntityManager em;

    public List<BlogEntry> getMostRecent() {
        return (List<BlogEntry>)em.createQuery("SELECT b FROM BlogEntry b")
                .setMaxResults(5)
                .getResultList();
    }
}
