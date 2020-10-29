package com.curzar.androidkiosksample.model;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;


@Entity(tableName = "setting")
public class Setting {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name= "name")
    private String name;
    @Nullable
    @ColumnInfo(name="value")
    private String value;

    public Setting(int id, String name, String value){
        this.id = id;
        this.name = name;
        this.value = value;
    }
    @Ignore
    public Setting(String name, String value){
        this.name = name;
        this.value = value;
    }


    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getValue(){return value;}
    public void setValue(String value){this.value = value;}
}
