package com.gr2.a2016.ease_l.classes;


import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Usre on 27.05.2016.
 */
public class Image extends BaseEntity{
    private ArrayList<String> comments;
    private String data;

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public String getData() {
        return data;
    }

    public Image(ArrayList<String> comments, String data,Person author,String id,Date creationDate,String version,String name) {
        super(author,id,creationDate,version,name);
        this.comments = comments;
        this.data = data;
    }

}
