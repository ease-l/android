package com.gr2.a2016.ease_l.classes;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usre on 27.05.2016.
 */
public class Project extends BaseEntity{
    private ArrayList<String>[] comments;
    private ArrayList<String> projects;
    private ArrayList<String> images;

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setProjects(ArrayList<String> projects) {
        this.projects = projects;
    }

    public ArrayList<String> getProjects() {
        return projects;
    }

    public void setComments(ArrayList<String>[] comments) {
        this.comments = comments;
    }

    public ArrayList<String>[] getComments() {
        return comments;
    }

    public Project(Person author,String id,Date creationDate,String version, ArrayList<String>[] comments,ArrayList<String> projects,ArrayList<String> images,String name){
        super(author,id,creationDate,version,name);
        this.comments = comments;
        this.projects = projects;
        this.images = images;
    }
}
