package com.gr2.a2016.ease_l.classes;


import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by Usre on 27.05.2016.
 */
public class Image extends BaseEntity{
    private Comment[] comments;
    private byte[] data;

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

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Comment[] getComments() {
        return comments;
    }

    public byte[] getData() {
        return data;
    }

    public Image(Comment[] comments, byte[] data,Person author,ObjectId id,Date creationDate,String version) {
        this.comments = comments;
        this.data = data;
        this.author = author;
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
    }

}
