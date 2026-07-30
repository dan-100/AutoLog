package com.autolog.autolog.controller;

import com.autolog.autolog.model.ServiceManager;
import com.autolog.autolog.model.ServiceRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Controls the Add Service screen.
 */
public class AddServiceController {

    private static final String DASHBOARD_FXML =
            "/com/autolog/autolog/layouts/mobileDashboard.fxml";

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

    private final ServiceManager serviceManager = new ServiceManager();

    /**
     * Loads the existing service history when the screen opens.
     */
    @FXML
    private void initialize() {
        try {
            serviceManager.loadServices();
        } catch (IOException | IllegalArgumentException exception) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Service Data Error",
                    "The existing service history could not be loaded."
            );
        }
    }

    /**
     * Matches onAction="#handleSaveService" in mobileService.fxml.
     */
    @FXML
    private void handleSaveService(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        ServiceRecord record = createServiceRecord();

        try {
            serviceManager.addService(record);

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Service Saved",
                    "The service record was saved successfully."
            );

            clearFields();

        } catch (Exception exception) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Save Error",
                    "The service record could not be saved."
            );
        }
    }

    /**
     * Matches onAction="#returnToDashboard" in mobileService.fxml.
     */
    @FXML
    private void returnToDashboard(ActionEvent event) {
        try {
            URL resource = getClass().getResource(DASHBOARD_FXML);

            if (resource == null) {
                throw new IllegalStateException(
                        "FXML resource not found: " + DASHBOARD_FXML
                );
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to return to the dashboard.",
                    exception
            );
        }
    }

    /**
     * Validates all required service fields.
     */
    private boolean validateInput() {
        String serviceType = serviceTypeField.getText().trim();
        String serviceDate = serviceDateField.getText().trim();
        String mileageText = serviceMileageField.getText().trim();
        String costText = costField.getText().trim();

        if (serviceType.isEmpty()) {
            showValidationError("Enter a service type.");
            return false;
        }

        if (serviceDate.isEmpty()) {
            showValidationError("Enter a service date.");
            return false;
        }

        if (mileageText.isEmpty()) {
            showValidationError("Enter the vehicle mileage.");
            return false;
        }

        if (costText.isEmpty()) {
            showValidationError("Enter the service cost.");
            return false;
        }

        try {
            int mileage = Integer.parseInt(mileageText);

            if (mileage < 0) {
                showValidationError("Mileage cannot be negative.");
                return false;
            }
        } catch (NumberFormatException exception) {
            showValidationError("Mileage must be a whole number.");
            return false;
        }

        try {
            double cost = Double.parseDouble(costText);

            if (cost < 0) {
                showValidationError("Cost cannot be negative.");
                return false;
            }
        } catch (NumberFormatException exception) {
            showValidationError("Cost must be a valid number.");
            return false;
        }

        return true;
    }

    /**
     * Creates a service record from the form fields.
     */
    private ServiceRecord createServiceRecord() {
        return new ServiceRecord(
                serviceTypeField.getText().trim(),
                serviceDateField.getText().trim(),
                Integer.parseInt(serviceMileageField.getText().trim()),
                Double.parseDouble(costField.getText().trim()),
                notesField.getText().trim()
        );
    }

    /**
     * Clears the form after a successful save.
     */
    private void clearFields() {
        serviceTypeField.clear();
        serviceDateField.clear();
        serviceMileageField.clear();
        costField.clear();
        notesField.clear();
    }

    private void showValidationError(String message) {
        showAlert(
                Alert.AlertType.WARNING,
                "Invalid Input",
                message
        );
    }

    private void showAlert(
            Alert.AlertType alertType,
            String title,
            String message
    ) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}