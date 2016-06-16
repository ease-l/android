package com.gr2.a2016.ease_l.classes;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by Valera_alt on 16-Jun-16.
 */

public class BaseEntity {
    public ObjectId id;
    public Person author;
    public Date creationDate;
    public String version;
}
