package com.autolog.autolog.controller;

import com.autolog.autolog.model.Vehicle;
import com.autolog.autolog.model.VehicleManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;

import java.io.IOException;

/**
 * Controls the vehicle setup screen.
 * Allows the user to create or update a vehicle profile
 * before entering the application's dashboard.
 *
 * @author Kellun
 */
public class VehicleSetupController {

    private final VehicleManager vehicleManager = new VehicleManager();

    @FXML
    private TextField yearField;

    @FXML
    private TextField makeField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField mileageField;

    /**
     * Loads the existing vehicle profile, if one exists.
     */
    @FXML
    public void initialize() {

        Vehicle vehicle = vehicleManager.getVehicle();

        if (vehicle != null) {
            yearField.setText(String.valueOf(vehicle.getYear()));
            makeField.setText(vehicle.getMake());
            modelField.setText(vehicle.getModel());
            mileageField.setText(String.valueOf(vehicle.getMileage()));
        }
    }

    /**
     * Creates the vehicle profile and opens the dashboard.
     */
    @FXML
    private void handleContinue() {

        if (!validateInput()) {
            return;
        }

        createVehicle();
        openDashboard();
    }

    /**
     * Validates all vehicle information entered by the user.
     *
     * @return true if all fields contain valid data
     */
    private boolean validateInput() {

        if (yearField.getText().isBlank()
                || makeField.getText().isBlank()
                || modelField.getText().isBlank()
                || mileageField.getText().isBlank()) {
            return false;
        }

        try {
            int year = Integer.parseInt(yearField.getText().trim());
            int mileage = Integer.parseInt(mileageField.getText().trim());

            if (year <= 0 || mileage < 0) {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Creates and saves the user's vehicle profile.
     */
    private void createVehicle() {

        int year = Integer.parseInt(yearField.getText().trim());
        String make = makeField.getText().trim();
        String model = modelField.getText().trim();
        int mileage = Integer.parseInt(mileageField.getText().trim());

        Vehicle vehicle = new Vehicle(year, make, model, mileage);

        vehicleManager.saveVehicle(vehicle);
    }

    /**
     * Opens the application's dashboard.
     */
    private void openDashboard() {
        String dashboardPath =
                "/com/autolog/autolog/layouts/mobileDashboard.fxml";

        URL dashboardUrl = getClass().getResource(dashboardPath);

        if (dashboardUrl == null) {
            throw new IllegalStateException(
                    "Dashboard FXML was not found at: " + dashboardPath
            );
        }

        try {
            FXMLLoader loader = new FXMLLoader(dashboardUrl);
            Parent root = loader.load();

            Stage stage = (Stage) yearField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not load the dashboard FXML.",
                    e
            );
        }
    }
}