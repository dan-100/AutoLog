package com.autolog.autolog.controller;//need to add this as the controller for the mobileService.fxml file

import com.autolog.autolog.model.ServiceManager;
import com.autolog.autolog.model.ServiceRecord;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AddServiceController {

    @FXML
    private TextField serviceTypeField;

    @FXML
    private TextField serviceDateField;

    @FXML
    private TextField serviceMileageField;

    @FXML
    private TextField costField;

    @FXML
    private TextField notesField;

    @FXML
    private Text statusLabel;

    private ServiceManager serviceManager;

    @FXML
    public void initialize() {

        serviceManager = new ServiceManager();

        try {
            serviceManager.loadServices();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void saveServiceRecord() {
        if (!validateInput()) {
            return;
        }

        ServiceRecord record = createServiceRecord();

        try {
            serviceManager.addService(record);
            statusLabel.setStyle("-fx-fill: green;");
            statusLabel.setText("Service saved successfully.");
            clearFields();

        }
        catch (Exception e) {
            statusLabel.setStyle("-fx-fill: red;");
            statusLabel.setText("Unable to save service.");
        }
    }

    private boolean validateInput() {

        if (serviceTypeField.getText().trim().isEmpty()) {
            statusLabel.setText("Enter a service type.");
            return false;
        }

        if (serviceDateField.getText().trim().isEmpty()) {
            statusLabel.setText("Enter a service date.");
            return false;
        }

        if (serviceMileageField.getText().trim().isEmpty()) {
            statusLabel.setText("Enter mileage.");
            return false;
        }

        if (costField.getText().trim().isEmpty()) {
            statusLabel.setText("Enter cost.");
            return false;
        }

        try {
            Integer.parseInt(serviceMileageField.getText().trim());
            Double.parseDouble(costField.getText().trim());
        }
        catch (NumberFormatException e) {
            statusLabel.setText("Mileage or cost is invalid.");
            return false;
        }
        return true;
    }

    private ServiceRecord createServiceRecord() {
        return new ServiceRecord(
                serviceTypeField.getText().trim(),
                serviceDateField.getText().trim(),
                Integer.parseInt(serviceMileageField.getText().trim()),
                Double.parseDouble(costField.getText().trim()),
                notesField.getText().trim()

        );
    }

    private void clearFields() {

        serviceTypeField.clear();
        serviceDateField.clear();
        serviceMileageField.clear();
        costField.clear();
        notesField.clear();
    }

    @FXML
    private void returnToDashboard() {

        System.out.println("Back button pressed.");

        //need to add in what we are using for going back to the dashboard


    }
}