/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jasonlee
 */
@Entity
@Table(name = "registration")
@NamedQueries({@NamedQuery(name = "Registration.findAll", query = "SELECT r FROM Registration r"), @NamedQuery(name = "Registration.findById", query = "SELECT r FROM Registration r WHERE r.id = :id"), @NamedQuery(name = "Registration.findByEmailAddress", query = "SELECT r FROM Registration r WHERE r.emailAddress = :emailAddress"), @NamedQuery(name = "Registration.findByFullName", query = "SELECT r FROM Registration r WHERE r.fullName = :fullName")})
public class Registration implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "emailAddress", length = 255)
    private String emailAddress;
    @Column(name = "fullName", length = 255)
    private String fullName;
    @JoinColumn(name = "meeting_id", referencedColumnName = "id")
    @ManyToOne
    private Meeting meeting;
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @ManyToOne
    private GroupMember memberId;

    public Registration() {
    }

    public Registration(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public GroupMember getMemberId() {
        return memberId;
    }

    public void setMemberId(GroupMember memberId) {
        this.memberId = memberId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Registration)) {
            return false;
        }
        Registration other = (Registration) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.steeplesoft.meetspace.model.Registration[id=" + id + "]";
    }

}
