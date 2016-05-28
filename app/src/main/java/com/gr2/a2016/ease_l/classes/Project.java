package com.gr2.a2016.ease_l.classes;

import java.util.Date;

/**
 * Created by Usre on 27.05.2016.
 */
public class Project {
    private Person author;
    private int id;
    private Date creationDate;
    private String version;
    private Comment[] comments;
    private Project[] projects;
    private Image[] images;

    public void setImages(Image[] images) {
        this.images = images;
    }

    public Image[] getImages() {
        return images;
    }

    public void setProjects(Project[] projects) {
        this.projects = projects;
    }

    public Project[] getProjects() {
        return projects;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public Comment[] getComments() {
        return comments;
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
    public Project(Person author,int id,Date creationDate,String version,Comment[] comments,Project[] projects,Image[] images){
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
        this.author = author;
        this.id = id;
        this.projects = projects;
        this.images = images;
    }
}
