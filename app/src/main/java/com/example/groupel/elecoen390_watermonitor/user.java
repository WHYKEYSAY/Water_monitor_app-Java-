package com.example.groupel.elecoen390_watermonitor;

/*
* Describes an individual user's identity and configuration.
* IMPORTANT: Configuration is saved as String and should be formatted as "option1,option 2,..."
* */
public class user {
    private long ID;
    private String name;
    private String pass;
    private String config;

    //Avoid using constructors without parameters to avoid bugs
    public user(){}

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

    public long getID() {
        return ID;
    }

    //Avoid setting ID to avoid bugs
    public void setID(long ID) {
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
