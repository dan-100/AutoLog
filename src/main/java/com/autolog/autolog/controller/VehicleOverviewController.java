package com.autolog.autolog.controller;

import com.autolog.autolog.model.ServiceManager;
import com.autolog.autolog.model.VehicleManager;
import javafx.fxml.FXML;

public class VehicleOverviewController {

    private VehicleManager vehicleManager;
    private ServiceManager serviceManager;

    @FXML
    public void showView() {
        loadOverviewData();
    }

    public void loadOverviewData() {
        // Load vehicle information from VehicleManager
        // Load service statistics from ServiceManager
    }

    @FXML
    public void returnToDashboard() {
        // Switch back to dashboard
    }
}