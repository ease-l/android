package com.gr2.a2016.ease_l.classes;

/**
 * Created by Usre on 27.05.2016.
 */
public class Person {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Person(String name,int id){
        this.name = name;
        this.id = id;
    }
}
