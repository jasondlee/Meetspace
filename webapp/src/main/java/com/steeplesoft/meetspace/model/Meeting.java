/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steeplesoft.meetspace.model;


import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jasonlee
 */
@Entity
@Table(name = "meeting")
@NamedQueries({
    @NamedQuery(name="getMeeting", query="SELECT m FROM Meeting m WHERE m.id = :meetingId"),
    @NamedQuery(name="nextMeeting", query="SELECT m FROM Meeting m WHERE m.startDate >= CURRENT_DATE"),
    @NamedQuery(name = "Meeting.findAll", query = "SELECT m FROM Meeting m"),
    @NamedQuery(name = "Meeting.findById", query = "SELECT m FROM Meeting m WHERE m.id = :id"),
    @NamedQuery(name = "Meeting.findByDescription", query = "SELECT m FROM Meeting m WHERE m.description = :description"),
    @NamedQuery(name = "Meeting.findByEndDate", query = "SELECT m FROM Meeting m WHERE m.endDate = :endDate"),
    @NamedQuery(name = "Meeting.findByEndTime", query = "SELECT m FROM Meeting m WHERE m.endTime = :endTime"),
    @NamedQuery(name = "Meeting.findByName", query = "SELECT m FROM Meeting m WHERE m.name = :name"),
    @NamedQuery(name = "Meeting.findByStartDate", query = "SELECT m FROM Meeting m WHERE m.startDate = :startDate"),
    @NamedQuery(name = "Meeting.findByStartTime", query = "SELECT m FROM Meeting m WHERE m.startTime = :startTime")})
public class Meeting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "description", length = 32864) // Just some really large, mostly "random" number
    private String description;

    @Column
    private String topic;

    @Column
    private String speaker;

    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "endTime")
    @Temporal(TemporalType.TIME)
    private Date endTime;

    @Column(name = "name", length = 255)
    @NotNull @Size(min=1, max=255)
    private String name;

    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "startTime")
    @Temporal(TemporalType.TIME)
    private Date startTime;

    @Column
    private String address;

    @Column
    private String moreInfo;
    
    @OneToMany(mappedBy = "meeting")
    private Collection<Registration> registrations;

    public Meeting() {
    }

    public Meeting(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Collection<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Collection<Registration> registrations) {
        this.registrations = registrations;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
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
        if (!(object instanceof Meeting)) {
            return false;
        }
        Meeting other = (Meeting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.steeplesoft.meetspace.model.Meeting[id=" + id + "]";
    }
}
