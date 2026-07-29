package com.autolog.autolog.controller;

import com.autolog.autolog.model.Vehicle;
import com.autolog.autolog.model.VehicleManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class VehicleSetupController {

    private VehicleManager vehicleManager;

    @FXML
    private TextField yearField;

    @FXML
    private TextField makeField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField mileageField;

    public VehicleSetupController() {
        vehicleManager = new VehicleManager();
    }

    public void showView() {
        //@fxml initialize??
        Vehicle vehicle = vehicleManager.getVehicle();

        if (vehicle != null) {
            yearField.setText(String.valueOf(vehicle.getYear()));
            makeField.setText(vehicle.getMake());
            modelField.setText(vehicle.getModel());
            mileageField.setText(String.valueOf(vehicle.getMileage()));
        }
    }

    public void handleContinue() {
        if (!validateInput()) {
            return;
        }
        createVehicle();

        // return to dashboard
    }


    public boolean validateInput() {
        if (yearField.getText().isBlank()
                || makeField.getText().isBlank()
                || modelField.getText().isBlank()
                || mileageField.getText().isBlank()) {
            return false;
        }

        try {
            Integer.parseInt(yearField.getText());
            Integer.parseInt(mileageField.getText());
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public void createVehicle() {
        int year = Integer.parseInt(yearField.getText());
        String make = makeField.getText();
        String model = modelField.getText();
        int mileage = Integer.parseInt(mileageField.getText());

        Vehicle vehicle = new Vehicle(year, make, model, mileage);
        vehicleManager.saveVehicle(vehicle);
    }
}