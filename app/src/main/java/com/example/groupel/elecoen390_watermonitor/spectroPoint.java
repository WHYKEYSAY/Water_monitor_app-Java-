package com.example.groupel.elecoen390_watermonitor;

public class spectroPoint {
    private int ID;
    private int measurementID;
    private int wavelength;
    private int intensity;

    public spectroPoint(int ID, int measurementID, int wavelength, int intensity) {
        this.ID = ID;
        this.measurementID = measurementID;
        this.wavelength = wavelength;
        this.intensity = intensity;
    }

    public spectroPoint(int measurementID, int wavelength, int intensity) {
        this.measurementID = measurementID;
        this.wavelength = wavelength;
        this.intensity = intensity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getWavelength() {
        return wavelength;
    }

    public void setWavelength(int wavelength) {
        this.wavelength = wavelength;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public int getMeasurementID() {
        return measurementID;
    }

    public void setMeasurementID(int measurementID) {
        this.measurementID = measurementID;
    }
}
