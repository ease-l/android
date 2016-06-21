package com.gr2.a2016.ease_l.classes;

import java.io.File;
import java.util.Date;

/**
 * Created by Usre on 27.05.2016.
 */
public class Comment extends BaseEntity{
    private String text;
    private File[] attachments;

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

    public Comment(String text, File[] attachments,Person author,String id,Date creationDate,String version,String name) {
        super(author,id,creationDate,version,name);
        this.text = text;
        this.attachments = attachments;
    }
}
