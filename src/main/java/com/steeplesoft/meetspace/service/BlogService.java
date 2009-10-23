/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service;

import com.steeplesoft.meetspace.model.BlogEntry;
import java.util.List;
/**
 *
 * @author jasonlee
 */
public interface BlogService {
    public List<BlogEntry> getMostRecent();
}
