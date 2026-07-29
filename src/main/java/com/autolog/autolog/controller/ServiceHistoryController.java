package com.autolog.autolog.controller;

import com.autolog.autolog.model.ServiceManager;
import javafx.fxml.FXML;

public class ServiceHistoryController {

    private ServiceManager serviceManager;

    @FXML
    public void showView() {
        loadServiceHistory();
    }

    public void loadServiceHistory() {
        // Load service history from ServiceManager
    }

    @FXML
    public void returnToDashboard() {
        // Switch back to dashboard
    }
}