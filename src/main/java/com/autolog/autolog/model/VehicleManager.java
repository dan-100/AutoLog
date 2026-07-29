package com.autolog.autolog.model;

import java.io.*;

public class VehicleManager {

    private String fileName;
    private Vehicle vehicle;

    //multiple vehicles or not????

    public VehicleManager() {
        fileName = "vehicles.csv";
        vehicle = loadVehicle();
    }

    public Vehicle loadVehicle() {
        if (!vehicleExists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // Skip header

            String line = reader.readLine();
            if (line != null) {
                vehicle = parseVehicle(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vehicle;
    }

    public void saveVehicle(Vehicle vehicle) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("year,make,model,currentMileage");
            writer.println(vehicle.toCSV());
            this.vehicle = vehicle;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateVehicle(Vehicle vehicle) {
        saveVehicle(vehicle);
        this.vehicle = vehicle;
    }

    public boolean vehicleExists() {
        File file = new File(fileName);
        return file.exists() && file.length() > 0;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    private Vehicle parseVehicle(String line) {
        String[] data = line.split(",");

        int year = Integer.parseInt(data[0]);
        String make = data[1];
        String model = data[2];
        int mileage = Integer.parseInt(data[3]);

        return new Vehicle(year, make, model, mileage);
    }
}