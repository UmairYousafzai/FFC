package com.example.ffccloud.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Classification")
public class ClassificationModel {

    @PrimaryKey(autoGenerate = true)
    int id_pk ;

    @SerializedName("Classification_Id")
    int Classification_Id;
    @SerializedName("Classification_Code")
    String Classification_Code;
    @SerializedName("Classification_Title")
    String Classification_Title;
    @SerializedName("Classification_Abbr")
    String Classification_Abbr;
    @SerializedName("Active")
    Boolean Active;

    public int getId_pk() {
        return id_pk;
    }

    public void setId_pk(int id_pk) {
        this.id_pk = id_pk;
    }

    public int getClassification_Id() {
        return Classification_Id;
    }

    public void setClassification_Id(int classification_Id) {
        Classification_Id = classification_Id;
    }

    public String getClassification_Code() {
        return Classification_Code;
    }

    public void setClassification_Code(String classification_Code) {
        Classification_Code = classification_Code;
    }

    public String getClassification_Title() {
        return Classification_Title;
    }

    public void setClassification_Title(String classification_Title) {
        Classification_Title = classification_Title;
    }

    public String getClassification_Abbr() {
        return Classification_Abbr;
    }

    public void setClassification_Abbr(String classification_Abbr) {
        Classification_Abbr = classification_Abbr;
    }

    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }
}
