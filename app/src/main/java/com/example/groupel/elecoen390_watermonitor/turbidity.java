package com.example.groupel.elecoen390_watermonitor;

import java.sql.Date;

public class turbidity {
    private long ID;
    private int turb;
    private Date date;

    public turbidity(){}

    public turbidity(int ID, int turb, Date date) {
        this.ID = ID;
        this.turb = turb;
        this.date = date;
    }

    public turbidity(int turb, Date date) {
        this.turb = turb;
        this.date = date;
    }

    public long getID() {
        return ID;
    }

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
