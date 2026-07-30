package com.autolog.autolog.controller;

import com.autolog.autolog.model.ServiceManager;
import com.autolog.autolog.model.ServiceRecord;
import com.autolog.autolog.model.Vehicle;
import com.autolog.autolog.model.VehicleManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Controls the application's main dashboard by displaying the current
 * vehicle information, recent service activity, and navigation between
 * the different AutoLog screens.
 *
 * @author Daniel Hernandez
 */
public class DashboardController {

    private static final String VEHICLE_PROFILE_FXML =
            "/com/autolog/autolog/layouts/mobileProfile.fxml";

    private static final String ADD_SERVICE_FXML =
            "/com/autolog/autolog/layouts/mobileService.fxml";

    private static final String SERVICE_HISTORY_FXML =
            "/com/autolog/autolog/layouts/mobileHistory.fxml";

    private static final String SUMMARY_FXML =
            "/com/autolog/autolog/layouts/mobileSummary.fxml";

    private final VehicleManager vehicleManager = new VehicleManager();
    private final ServiceManager serviceManager = new ServiceManager();

    @FXML
    private Text carInfoLabel;

    @FXML
    private Text currentMileageLabel;

    @FXML
    private Text mileageOverview;

    @FXML
    private Text serviceName;

    @FXML
    private Text serviceDate;

    @FXML
    private Text serviceMileage;

    @FXML
    private Text serviceCost;

    @FXML
    private Text totalAmountLabel;

    @FXML
    private Text totalServicesLabel;

    /**
     * Runs automatically after the dashboard FXML has been loaded.
     */
    @FXML
    private void initialize() {
        loadDashboardData();
    }

    /**
     * Loads all vehicle and service information displayed on the dashboard.
     */
    public void loadDashboardData() {
        loadVehicleInformation();
        loadServiceInformation();
    }

    /**
     * Displays the current vehicle on the dashboard.
     */
    private void loadVehicleInformation() {
        Vehicle vehicle = vehicleManager.getVehicle();

        if (vehicle == null) {
            carInfoLabel.setText("No vehicle saved");
            currentMileageLabel.setText("Current mileage");
            mileageOverview.setText("Not available");
            return;
        }

        carInfoLabel.setText(
                vehicle.getYear()
                        + " "
                        + vehicle.getMake()
                        + " "
                        + vehicle.getModel()
        );

        currentMileageLabel.setText("Current mileage");

        mileageOverview.setText(
                String.format("%,d miles", vehicle.getMileage())
        );
    }

    /**
     * Loads service-history information and summary values.
     */
    private void loadServiceInformation() {
        try {
            serviceManager.loadServices();

            loadQuickSummary();
            loadRecentService(serviceManager.getMostRecentService());

        } catch (IOException | IllegalArgumentException exception) {
            showServiceLoadError();
        }
    }

    /**
     * Displays the total number of services and total amount spent.
     */
    private void loadQuickSummary() {
        totalServicesLabel.setText(
                String.valueOf(serviceManager.getTotalServices())
        );

        totalAmountLabel.setText(
                String.format("$%,.2f", serviceManager.getTotalCost())
        );
    }

    /**
     * Displays the most recently recorded service.
     */
    private void loadRecentService(ServiceRecord record) {
        if (record == null) {
            serviceName.setText("No services saved");
            serviceDate.setText("Date unavailable");
            serviceMileage.setText("Mileage unavailable");
            serviceCost.setText("0.00");
            return;
        }

        serviceName.setText(record.getServiceType());
        serviceDate.setText(record.getDate());

        serviceMileage.setText(
                String.format("%,d miles", record.getMileage())
        );

        serviceCost.setText(
                String.format("%,.2f", record.getCost())
        );
    }

    /**
     * Displays fallback text if service_history.csv cannot be loaded.
     */
    private void showServiceLoadError() {
        totalServicesLabel.setText("0");
        totalAmountLabel.setText("$0.00");
        serviceName.setText("Unable to load services");
        serviceDate.setText("Check service_history.csv");
        serviceMileage.setText("");
        serviceCost.setText("0.00");
    }

    /*
     * These method names match the On Action values entered
     * in Scene Builder.
     */

    @FXML
    private void handleVehicleProfile(ActionEvent event) {
        switchScene(event, VEHICLE_PROFILE_FXML);
    }

    @FXML
    private void handleAddService(ActionEvent event) {
        switchScene(event, ADD_SERVICE_FXML);
    }

    @FXML
    private void handleServiceHistory(ActionEvent event) {
        switchScene(event, SERVICE_HISTORY_FXML);
    }

    @FXML
    private void handleSummary(ActionEvent event) {
        switchScene(event, SUMMARY_FXML);
    }

    /**
     * Reloads dashboard values when needed.
     */
    public void refreshDashboard() {
        loadDashboardData();
    }

    /**
     * Changes the current JavaFX scene.
     */
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            URL resource = getClass().getResource(fxmlPath);

            if (resource == null) {
                throw new IllegalStateException(
                        "FXML resource not found: " + fxmlPath
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
                    "Unable to open FXML file: " + fxmlPath,
                    exception
            );
        }
    }
}