package com.gr2.a2016.ease_l.classes;

import org.bson.types.ObjectId;

public class Person {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Person(String name,String id){
        this.name = name;
        this.id = id;
    }
}
