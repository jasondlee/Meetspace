/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.service;

import com.steeplesoft.meetspace.model.Registration;
import com.steeplesoft.meetspace.model.Sponsor;
import java.io.Serializable;

/**
 *
 * @author jasonlee
 */
public interface MainService extends Serializable {
    public Registration saveRegistration(Registration reg);
    public Sponsor getRandomSponsor();
}
