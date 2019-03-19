package com.example.groupel.elecoen390_watermonitor;

import java.sql.Date;

public class turbidity {
    private long ID;
    private long measurementID;
    private int turb;
    private Date date;

    //Avoid using constructors without parameters to avoid bugs
    public turbidity(){}

    public turbidity(long ID, long measurementID,  int turb, Date date) {
        this.ID = ID;
        this.turb = turb;
        this.date = date;
    }

    public turbidity(long measurementID, int turb, Date date) {
        this.turb = turb;
        this.date = date;
    }

    public long getMeasurementID() {
        return measurementID;
    }

    public void setMeasurementID(long measurementID) {
        this.measurementID = measurementID;
    }

    public long getID() {
        return ID;
    }
    //Avoid setting ID to avoid bugs
    public void setID(long ID) {
        this.ID = ID;
    }

    public int getTurb() {
        return turb;
    }

    public void setTurb(int turb) {
        this.turb = turb;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
