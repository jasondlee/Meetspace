/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.steeplesoft.meetspace.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jasonlee
 */
@Entity
@Table(name = "group_member")
@NamedQueries({@NamedQuery(name = "GroupMember.findAll", query = "SELECT g FROM GroupMember g"), @NamedQuery(name = "GroupMember.findById", query = "SELECT g FROM GroupMember g WHERE g.id = :id"), @NamedQuery(name = "GroupMember.findByEmailAddress", query = "SELECT g FROM GroupMember g WHERE g.emailAddress = :emailAddress"), @NamedQuery(name = "GroupMember.findByFirstName", query = "SELECT g FROM GroupMember g WHERE g.firstName = :firstName"), @NamedQuery(name = "GroupMember.findByLastName", query = "SELECT g FROM GroupMember g WHERE g.lastName = :lastName")})
public class GroupMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "emailAddress", length = 255)
    private String emailAddress;
    @Column(name = "firstName", length = 255)
    private String firstName;
    @Column(name = "lastName", length = 255)
    private String lastName;
    @OneToMany(mappedBy = "memberId")
    private Collection<Registration> registrationCollection;

    public GroupMember() {
    }

    public GroupMember(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Collection<Registration> getRegistrationCollection() {
        return registrationCollection;
    }

    public void setRegistrationCollection(Collection<Registration> registrationCollection) {
        this.registrationCollection = registrationCollection;
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
        if (!(object instanceof GroupMember)) {
            return false;
        }
        GroupMember other = (GroupMember) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.steeplesoft.meetspace.model.GroupMember[id=" + id + "]";
    }

}
