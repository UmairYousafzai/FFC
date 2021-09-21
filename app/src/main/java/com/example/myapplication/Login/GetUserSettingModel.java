package com.example.myapplication.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "User_Settings")
public class GetUserSettingModel {

    @PrimaryKey
    @NonNull
    @SerializedName("Configuration_Id")
    @Expose
    private int configurationId;


    @Nullable
    @ColumnInfo(name = "Configuration_Name")
    @SerializedName("Configuration_Name")
    @Expose
    private String configurationName;

    @Nullable
    @ColumnInfo(name = "value")
    @SerializedName("value")
    @Expose
    private String value;

    public int getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(int configurationId) {
        this.configurationId = configurationId;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}