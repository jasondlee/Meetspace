/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steeplesoft.meetspace.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jasonlee
 */
@Entity
@Table(name = "blog_entry")
@NamedQueries({
        @NamedQuery(name = "BlogEntry.findAll", query = "SELECT be FROM BlogEntry be"),
        @NamedQuery(name = "BlogEntry.findById", query = "SELECT be FROM BlogEntry be WHERE be.id = :id"),
        @NamedQuery(name = "BlogEntry.findSticky", query = "SELECT be FROM BlogEntry be where be.isSticky = true ORDER by be.postedDate desc"),
        @NamedQuery(name = "BlogEntry.mostRecent", query = "SELECT be FROM BlogEntry be where be.isSticky = false ORDER by be.postedDate desc ")
})
//@EntityListeners({BlogEntryListener.class})
public class BlogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String body;

    @Column
    private Boolean isSticky;

    @ManyToOne()
    private BlogEntry postedBy;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date postedDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public BlogEntry getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(BlogEntry postedBy) {
        this.postedBy = postedBy;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsSticky() {
        return isSticky;
    }

    public void setIsSticky(Boolean sticky) {
        this.isSticky = sticky;
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
        if (!(object instanceof BlogEntry)) {
            return false;
        }
        BlogEntry other = (BlogEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.steeplesoft.meetspace.model.BlogEntry[id=" + id + "]";
    }

    @PrePersist
    public void prePersist() {
        Date date = new Date();
        setPostedDate(date);
        setModifiedDate(date);
    }

    @PreUpdate
    public void preUpdate() {
        setModifiedDate(new Date());
    }
}
