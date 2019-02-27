package com.example.groupel.elecoen390_watermonitor;

import java.util.Date;

public class turbidity {
    private int ID;
    private int turb;
    private Date date;

    public turbidity(int ID, int turb, Date date) {
        this.ID = ID;
        this.turb = turb;
        this.date = date;
    }

    public turbidity(int turb, Date date) {
        this.turb = turb;
        this.date = date;
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
