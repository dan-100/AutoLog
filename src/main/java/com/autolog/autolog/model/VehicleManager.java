package com.autolog.autolog.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class VehicleManager {

    private static final String CSV_HEADER = "year,make,model,currentMileage";

    private final Path filePath;
    private Vehicle vehicle;

    public VehicleManager() {
        filePath = Path.of(
                "src",
                "main",
                "resources",
                "data",
                "vehicles.csv"
        );

        vehicle = loadVehicle();
    }

    /**
     * Loads the vehicle profile from vehicles.csv.
     *
     * @return the stored vehicle, or null if no profile exists
     */
    public Vehicle loadVehicle() {
        if (!Files.exists(filePath)) {
            return null;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Skip CSV header

            String line = reader.readLine();

            if (line == null || line.isBlank()) {
                vehicle = null;
                return null;
            }

            vehicle = parseVehicle(line);
            return vehicle;

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Unable to load vehicle: " + e.getMessage());
            vehicle = null;
            return null;
        }
    }

    /**
     * Saves the vehicle profile to vehicles.csv.
     * Because AutoLog currently supports one vehicle, this replaces
     * the previous vehicle profile.
     *
     * @param vehicle vehicle to save
     */
    public void saveVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null.");
        }

        try {
            Path parentDirectory = filePath.getParent();

            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(
                    filePath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            )) {
                writer.write(CSV_HEADER);
                writer.newLine();
                writer.write(vehicle.toCSV());
                writer.newLine();
            }

            this.vehicle = vehicle;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to save vehicle to " + filePath,
                    e
            );
        }
    }

    /**
     * Replaces the current vehicle profile.
     *
     * @param vehicle updated vehicle information
     */
    public void updateVehicle(Vehicle vehicle) {
        saveVehicle(vehicle);
    }

    /**
     * Determines whether vehicles.csv contains an actual vehicle record.
     *
     * @return true when a vehicle profile exists
     */
    public boolean vehicleExists() {
        if (!Files.exists(filePath)) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Skip header

            String vehicleLine = reader.readLine();
            return vehicleLine != null && !vehicleLine.isBlank();

        } catch (IOException e) {
            System.err.println(
                    "Unable to check for vehicle profile: " + e.getMessage()
            );
            return false;
        }
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Converts one CSV record into a Vehicle object.
     *
     * @param line vehicle CSV record
     * @return parsed vehicle
     */
    private Vehicle parseVehicle(String line) {
        String[] data = line.split(",", -1);

        if (data.length != 4) {
            throw new IllegalArgumentException(
                    "Invalid vehicle CSV record: " + line
            );
        }

        try {
            int year = Integer.parseInt(data[0].trim());
            String make = data[1].trim();
            String model = data[2].trim();
            int mileage = Integer.parseInt(data[3].trim());

            if (make.isBlank() || model.isBlank()) {
                throw new IllegalArgumentException(
                        "Vehicle make and model cannot be blank."
                );
            }

            return new Vehicle(year, make, model, mileage);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Vehicle year and mileage must be valid numbers.",
                    e
            );
        }
    }
}