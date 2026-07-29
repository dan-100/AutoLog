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
    private static final String VEHICLE_OVERVIEW_FXML =
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

    @FXML
    public void initialize() {
        loadDashboardData();
    }

    public void showView() {
        loadDashboardData();
    }

    public void loadDashboardData() {
        loadVehicleInformation();

        try {
            serviceManager.loadServices();
            loadQuickSummary();
            loadRecentService(serviceManager.getMostRecentService());
        } catch (IOException | IllegalArgumentException exception) {
            showServiceLoadError();
        }
    }

    private void loadVehicleInformation() {
        Vehicle vehicle = vehicleManager.getVehicle();

        if (vehicle == null) {
            carInfoLabel.setText("No vehicle saved");
            currentMileageLabel.setText("Current mileage");
            mileageOverview.setText("Not available");
            return;
        }

        carInfoLabel.setText(
                vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel());
        currentMileageLabel.setText("Current mileage");
        mileageOverview.setText(String.format("%,d miles", vehicle.getMileage()));
    }

    private void loadQuickSummary() {
        totalServicesLabel.setText(String.valueOf(serviceManager.getTotalServices()));
        totalAmountLabel.setText(String.format("$%,.2f", serviceManager.getTotalCost()));
    }

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
        serviceMileage.setText(String.format("%,d miles", record.getMileage()));
        serviceCost.setText(String.format("%,.2f", record.getCost()));
    }

    private void showServiceLoadError() {
        totalServicesLabel.setText("0");
        totalAmountLabel.setText("$0.00");
        serviceName.setText("Unable to load services");
        serviceDate.setText("Check service_history.csv");
        serviceMileage.setText("");
        serviceCost.setText("0.00");
    }

    @FXML
    public void openVehicleProfile(ActionEvent event) {
        switchScene(event, VEHICLE_PROFILE_FXML);
    }

    @FXML
    public void openAddService(ActionEvent event) {
        switchScene(event, ADD_SERVICE_FXML);
    }

    @FXML
    public void openServiceHistory(ActionEvent event) {
        switchScene(event, SERVICE_HISTORY_FXML);
    }

    @FXML
    public void openVehicleOverview(ActionEvent event) {
        switchScene(event, VEHICLE_OVERVIEW_FXML);
    }

    public void refreshDashboard() {
        loadDashboardData();
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            var resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                throw new IllegalStateException("FXML resource not found: " + fxmlPath);
            }

            Parent root = FXMLLoader.load(resource);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to open " + fxmlPath, exception);
        }
    }
}