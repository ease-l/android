package com.gr2.a2016.ease_l.classes;


import java.util.Date;

/**
 * Created by Usre on 27.05.2016.
 */
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

    public Image(Comment[] comments, byte[] data,Person author,String id,Date creationDate,String version) {
        super(author,id,creationDate,version);
        this.comments = comments;
        this.data = data;
    }

}
