package com.autolog.autolog.model;

public class Vehicle {

    private int year;
    private String make;
    private String model;
    private int mileage;

    public Vehicle(int year, String make, String model, int mileage) {
        this.year = year;
        this.make = make;
        this.model = model;
        this.mileage = mileage;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String toCSV() {
        return year + "," + make + "," + model + "," + mileage;
    }
}
