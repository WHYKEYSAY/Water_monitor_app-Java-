package com.example.groupel.elecoen390_watermonitor;

import java.sql.Date;

public class spectroMeasure {
    private long ID;
    private Date date;

    public spectroMeasure(){}

    public spectroMeasure(long ID, Date date) {
        this.ID = ID;
        this.date = date;
    }

    public spectroMeasure(Date date) {
        this.date = date;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
