package com.gr2.a2016.ease_l.classes;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by Valera_alt on 16-Jun-16.
 */

public class BaseEntity {
    private ObjectId id;
    private Person author;
    private Date creationDate;
    private String version;

    public BaseEntity(Person author,ObjectId id,Date creationDate,String version) {
        this.author = author;
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getVersion() {
        return version;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }
}
