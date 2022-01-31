package com.example.ffccloud.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Grade")
public class GradingModel {

    @PrimaryKey(autoGenerate = true)
    int id_pk;

    @SerializedName("Grade_Id")
    int Grade_Id;
    @SerializedName("Grade_Code")
    String Grade_Code;
    @SerializedName("Grade_Title")
    String Grade_Title;
    @SerializedName("Active")
    Boolean Active;

    public int getId_pk() {
        return id_pk;
    }

    public void setId_pk(int id_pk) {
        this.id_pk = id_pk;
    }

    public int getGrade_Id() {
        return Grade_Id;
    }

    public void setGrade_Id(int grade_Id) {
        Grade_Id = grade_Id;
    }

    public String getGrade_Code() {
        return Grade_Code;
    }

    public void setGrade_Code(String grade_Code) {
        Grade_Code = grade_Code;
    }

    public String getGrade_Title() {
        return Grade_Title;
    }

    public void setGrade_Title(String grade_Title) {
        Grade_Title = grade_Title;
    }

    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }
}
