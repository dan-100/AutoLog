package com.autolog.autolog.model;

public class ServiceRecord {

    private String serviceType;
    private String date;
    private int mileage;
    private double cost;
    private String notes;

    public ServiceRecord(String serviceType,
                         String date,
                         int mileage,
                         double cost,
                         String notes) {

        this.serviceType = serviceType;
        this.date = date;
        this.mileage = mileage;
        this.cost = cost;
        this.notes = notes;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        if(mileage >= 0)
            this.mileage = mileage;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {

        if(cost >= 0)
            this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {

        if(notes != null)
            this.notes = notes;
    }

    public String toCSV() {

        return serviceType + "," +
                date + "," +
                mileage + "," +
                cost + "," +
                notes;
    }

    @Override
    public String toString() {

        return "Service Type: " + serviceType +
                "\nDate: " + date +
                "\nMileage: " + mileage +
                "\nCost: $" + String.format("%.2f", cost) +
                "\nNotes: " + notes;
    }

}