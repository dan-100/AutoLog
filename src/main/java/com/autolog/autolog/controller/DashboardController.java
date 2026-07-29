package com.autolog.autolog.controller;

import com.autolog.autolog.model.ServiceManager;
import com.autolog.autolog.model.VehicleManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    private VehicleManager vehicleManager;
    private ServiceManager serviceManager;

    @FXML
    private Label vehicleLabel;

    @FXML
    private Label recentServiceLabel;

    @FXML
    public void showView() {
        loadDashboardData();
    }

    public void loadDashboardData() {
        // get vehicle from VehicleManager
        // get latest service from ServiceManager
        // update labels
    }

    @FXML
    public void openVehicleProfile() {
        // switch to vehicle profile screen
    }

    @FXML
    public void openAddService() {
        // switch to service maintenance screen
    }

    @FXML
    public void openServiceHistory() {
        // switch to service history screen
    }

    @FXML
    public void openVehicleOverview() {
        // switch to vehicle overview screen
    }

    public void refreshDashboard() {
        loadDashboardData();
    }

}

