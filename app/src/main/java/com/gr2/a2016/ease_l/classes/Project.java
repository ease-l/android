package com.gr2.a2016.ease_l.classes;

import java.util.Date;

/**
 * Created by Usre on 27.05.2016.
 */
public class Project extends BaseEntity{
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

    public Project(Person author,String id,Date creationDate,String version, Comment[] comments,Project[] projects,Image[] images){
        super(author,id,creationDate,version);
        this.comments = comments;
        this.projects = projects;
        this.images = images;
    }
}
