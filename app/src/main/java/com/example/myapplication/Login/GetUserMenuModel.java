package com.example.myapplication.Login;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "User_Menu")
public class GetUserMenuModel {

    @Nullable
    @ColumnInfo(name = "menuTittle")
    @SerializedName("Menu_Tittle")
    @Expose
    private String menuTittle;

    @PrimaryKey(autoGenerate = true)
    private int menu_id_pk;

    @SerializedName("Menu_Order")
    private int menu_order;

    @SerializedName("Menu_State")
    private String menuState;

    public String getMenuState() {
        return menuState;
    }

    public void setMenuState(String menuState) {
        this.menuState = menuState;
    }

    public int getMenu_order() {
        return menu_order;
    }

    public void setMenu_order(int menu_order) {
        this.menu_order = menu_order;
    }

    public int getMenu_id_pk() {
        return menu_id_pk;
    }

    public void setMenu_id_pk(int menu_id_pk) {
        this.menu_id_pk = menu_id_pk;
    }

    public String getMenuTittle() {
        return menuTittle;
    }

    public void setMenuTittle(String menuTittle) {
        this.menuTittle = menuTittle;
    }


}
