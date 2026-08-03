package com.autolog.autolog.model;

/**
 * Represents a single vehicle maintenance or service record.
 * Stores information such as the service type, date performed,
 * vehicle mileage, service cost, and additional notes.
 *
 * @author Ean
 */

public class ServiceRecord {

    private String serviceType;
    private String date;
    private int mileage;
    private double cost;
    private String notes;

    /**
     * Creates a new service record.
     *
     * @param serviceType the type of service performed
     * @param date the date the service was completed
     * @param mileage the vehicle mileage at the time of service
     * @param cost the cost of the service
     * @param notes additional notes about the service
     */

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

    /**
     * General Getters and Setters needed for the class
     */
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

    /**
     * Converts this service record into CSV format for file storage.
     *
     * @return a comma-separated representation of the service record
     */

    public String toCSV() {

        return serviceType + "," +
                date + "," +
                mileage + "," +
                cost + "," +
                notes;
    }

    /**
     * Returns a formatted string representation of the service record.
     *
     * @return the formatted service record
     */

    @Override
    public String toString() {

        return "Service Type: " + serviceType +
                "\nDate: " + date +
                "\nMileage: " + mileage +
                "\nCost: $" + String.format("%.2f", cost) +
                "\nNotes: " + notes;
    }

}