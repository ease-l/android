package com.gr2.a2016.ease_l.classes;

import java.io.File;
import java.util.Date;

/**
 * Created by Usre on 27.05.2016.
 */
public class Comment {
    private String text;
    private File[] attachments;
    private Person author;
    private int id;
    private Date creationDate;
    private String version;

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

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Person getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public File[] getAttachments() {
        return attachments;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAttachments(File[] attachments) {
        this.attachments = attachments;
    }

    public Comment(String text, File[] attachments,Person author,int id,Date creationDate,String version) {
        this.text = text;
        this.author = author;
        this.attachments = attachments;
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
    }
}
