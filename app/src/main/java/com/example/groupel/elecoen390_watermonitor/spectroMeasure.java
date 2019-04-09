package com.example.groupel.elecoen390_watermonitor;

import java.util.Date;

/*
* Describes any one water sample and provides ID to get all points(wavelength,absorbtion), turbidity
*   and chemicals in one to many, one to one and one to many relationship respectively.
* */
public class spectroMeasure {
    private long ID;
    private Date date;

    //Avoid using constructors without parameters to avoid bugs
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
    //Avoid setting ID to avoid bugs
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
