package com.travisit.travisitbusiness.model;

import java.io.Serializable;

public class MiniBranch implements Serializable {
    Integer id = null;
    String name;

    public MiniBranch(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public MiniBranch(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
