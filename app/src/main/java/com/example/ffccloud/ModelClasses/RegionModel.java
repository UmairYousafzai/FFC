package com.example.ffccloud.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class RegionModel {
    @SerializedName("id")
    private Integer regionId;
    @SerializedName("Name")
    private String name;

    public  RegionModel() {

    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setId(Integer id) {
        this.regionId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
