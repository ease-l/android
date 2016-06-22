package com.gr2.a2016.ease_l.classes;

import java.util.Date;

/**
 * Created by Valera_alt on 16-Jun-16.
 */

public abstract class BaseEntity {
    protected String id;
    protected Person author;
    protected Date creationDate;
    protected String version;
    protected String name;

    public BaseEntity(Person author,String id,Date creationDate,String version,String name) {
        this.author = author;
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }
}
