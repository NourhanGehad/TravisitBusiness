package com.travisit.travisitbusiness.model;

import java.io.Serializable;

public class Branch extends MiniBranch implements Serializable {
    Double latitute;
    Double longitude;

    public Branch(Integer id, String name, Double latitude, Double longitude) {
        super(id, name);
        this.latitute = latitude;
        this.longitude = longitude;
    }
    public Branch(String name, Double latitude, Double longitude) {
        super(name);
        this.latitute = latitude;
        this.longitude = longitude;
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

    public Double getLatitude() {
        return latitute;
    }

    public void setLatitude(Double latitude) {
        this.latitute = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
