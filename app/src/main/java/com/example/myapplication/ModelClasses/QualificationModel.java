package com.example.myapplication.ModelClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Qualification")
public class QualificationModel {

    @PrimaryKey(autoGenerate = true)
    int id_pk ;

    @SerializedName("Qualification_Id")
    int Qualification_Id;
    @SerializedName("Qualification_Code")
    String Qualification_Code;
    @SerializedName("Qualification_Title")
    String Qualification_Title;
    @SerializedName("Qualification_Abbr")
    String Qualification_Abbr;
    @SerializedName("Active")
    Boolean Active;

    public int getId_pk() {
        return id_pk;
    }

    public void setId_pk(int id_pk) {
        this.id_pk = id_pk;
    }

    public int getQualification_Id() {
        return Qualification_Id;
    }

    public void setQualification_Id(int qualification_Id) {
        Qualification_Id = qualification_Id;
    }

    public String getQualification_Code() {
        return Qualification_Code;
    }

    public void setQualification_Code(String qualification_Code) {
        Qualification_Code = qualification_Code;
    }

    public String getQualification_Title() {
        return Qualification_Title;
    }

    public void setQualification_Title(String qualification_Title) {
        Qualification_Title = qualification_Title;
    }

    public String getQualification_Abbr() {
        return Qualification_Abbr;
    }

    public void setQualification_Abbr(String qualification_Abbr) {
        Qualification_Abbr = qualification_Abbr;
    }

    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }
}
