package com.example.groupel.elecoen390_watermonitor;

/*
* Describes individual chemicals in association in one to many from spectroMeasure item.
* IMPORTANT NOTE: Any decimals below 0.001 is lost to the database.
* */
public class chemical {
    private long ID;
    private long measurementID;
    private short presence; //short size int utilised as boolean -> only 0/1 allowed else its interpreted as 1
    private int type;
    private String name;
    private float concentration; //could be used to store probability, approximated by an int 1000x greater on DB

    public static final int DECIMAL_SCALE = 1000;

    //Avoid using constructors without parameters to avoid bugs
    public chemical(){}
    //IMPORTANT NOTE: Any decimals on chemicals below 0.001 is lost to the database.
    public chemical(long ID, long measurementID, boolean presence, int type, String name, float concentration) {
        this.ID = ID;
        this.measurementID = measurementID;
        if (presence == false)
            this.presence = 0;
        else
            this.presence = 1;
        this.type = type;
        this.name = name;
        this.concentration = concentration;
    }
    //IMPORTANT NOTE: Any decimals on chemicals below 0.001 is lost to the database.
    public chemical(long measurementID, boolean presence, int type, String name, float concentration) {
        this.measurementID = measurementID;
        if (presence == false)
            this.presence = 0;
        else
            this.presence = 1;
        this.type = type;
        this.name = name;
        this.concentration = concentration;
    }

    public long getID() {
        return ID;
    }
    //Avoid setting ID to avoid bugs
    public void setID(long ID) {
        this.ID = ID;
    }

    public long getMeasurementID() {
        return measurementID;
    }

    public void setMeasurementID(long measureID) {
        this.measurementID = measureID;
    }

    public boolean getPresence() {
        if (this.presence == 0)
            return false;
        else
            return true;
    }

    public void setPresence(boolean presence) {
        if (presence == false)
            this.presence = 0;
        else
            this.presence = 1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //IMPORTANT NOTE: Any decimals below 0.001 is lost to the database.
    public float getConcentration() {
        return concentration;
    }

    //IMPORTANT NOTE: Any decimals below 0.001 is lost to the database.
    public void setConcentration(float concentration) {
        this.concentration = concentration;
    }
}
