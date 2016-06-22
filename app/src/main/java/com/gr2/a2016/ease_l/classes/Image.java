package com.gr2.a2016.ease_l.classes;


import java.util.Date;


public class Image extends BaseEntity{
    private Comment[] comments;
    private byte[] data;

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

    public Image(Comment[] comments, byte[] data,Person author,String id,Date creationDate,String version,String name) {
        super(author,id,creationDate,version,name);
        this.comments = comments;
        this.data = data;
    }

}
