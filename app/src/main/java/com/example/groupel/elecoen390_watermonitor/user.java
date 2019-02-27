package com.example.groupel.elecoen390_watermonitor;

public class user {
    private int ID;
    private String name;
    private String pass;
    private String config;

    public user(int ID, String name, String pass, String config) {
        this.ID = ID;
        this.name = name;
        this.pass = pass;
        this.config = config;
    }

    public user(String name, String pass, String config) {
        this.name = name;
        this.pass = pass;
        this.config = config;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
