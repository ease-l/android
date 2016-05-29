package com.gr2.a2016.ease_l.classes;

import org.bson.types.ObjectId;

/**
 * Created by Usre on 27.05.2016.
 */
public class Person {
    private String name;
    private ObjectId id;

    public String getName() {
        return name;
    }

    public ObjectId getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    public Person(String name,ObjectId id){
        this.name = name;
        this.id = id;
    }
}
