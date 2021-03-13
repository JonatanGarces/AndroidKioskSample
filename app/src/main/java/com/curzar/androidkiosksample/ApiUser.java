package com.curzar.androidkiosksample;
import com.google.gson.annotations.SerializedName;

public class ApiUser {
    @SerializedName("id")
    public int id;
    @SerializedName("time")
    public int time;
    @SerializedName("typeofchange")
    public String typeofchange;
    @SerializedName("ipclient")
    public String ipclient;
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;
    @SerializedName("money")
    public Double money;

    public ApiUser(int id, int time, String typeofchange, String ipclient, String username, String password, double  money) {
        this.id = id;
        this.time = time;
        this.typeofchange = typeofchange;
        this.ipclient = ipclient;
        this.username = username;
        this.password = password;
        this.money = money;
    }

    /*public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getTime(){
        return time;
    }
    public void setTime(int time){
        this.time = time;
    }
    public String getTypeofchange(){
        return typeofchange;
    }
    public void setTypeofchange(String typeofchange){
        this.typeofchange = typeofchange;
    }
    public String getIpclient(){
        return ipclient;
    }
    public void setIpclient(String ipclient){
        this.ipclient = ipclient;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

     */
}
