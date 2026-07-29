package com.autolog.autolog.controller;

import com.autolog.autolog.model.ServiceManager;
import com.autolog.autolog.model.ServiceRecord;
import com.autolog.autolog.model.Vehicle;
import com.autolog.autolog.model.VehicleManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
/**
 * Handles the vehicle overview screen and maintenance statistics.
 *
 * @author Daniel Hernandez
 */
public class VehicleOverviewController {

    private static final String DASHBOARD_FXML =
            "/com/autolog/autolog/layouts/mobileDashboard.fxml";

    private final VehicleManager vehicleManager = new VehicleManager();
    private final ServiceManager serviceManager = new ServiceManager();

    @FXML
    private VBox summaryEvents;

    @FXML
    public void initialize() {
        loadOverviewData();
    }

    public void showView() {
        loadOverviewData();
    }

    public void loadOverviewData() {
        summaryEvents.getChildren().clear();

        try {
            serviceManager.loadServices();
            addVehicleCard(vehicleManager.getVehicle());
            addStatisticsCard();
            addRecentServiceCard(serviceManager.getMostRecentService());
            addBreakdownCard(serviceManager.getServiceBreakdown());
        } catch (IOException | IllegalArgumentException exception) {
            addMessage("Unable to load summary data: " + exception.getMessage());
        }
    }

    private void addVehicleCard(Vehicle vehicle) {
        Label heading = createHeading("Vehicle Overview");

        VBox card = createCard();
        card.getChildren().add(heading);

        if (vehicle == null) {
            card.getChildren().add(new Label("No vehicle profile has been saved."));
        } else {
            card.getChildren().addAll(
                    new Label(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel()),
                    new Label(String.format("Current mileage: %,d miles", vehicle.getMileage())));
        }

        summaryEvents.getChildren().add(card);
    }

    private void addStatisticsCard() {
        VBox card = createCard();
        card.getChildren().add(createHeading("Maintenance Statistics"));
        card.getChildren().addAll(
                createStatisticRow("Total services", String.valueOf(serviceManager.getTotalServices())),
                createStatisticRow("Total spent", String.format("$%,.2f", serviceManager.getTotalCost())),
                createStatisticRow("Average service cost", String.format("$%,.2f", serviceManager.getAverageCost())));
        summaryEvents.getChildren().add(card);
    }

    private void addRecentServiceCard(ServiceRecord record) {
        VBox card = createCard();
        card.getChildren().add(createHeading("Most Recent Service"));

        if (record == null) {
            card.getChildren().add(new Label("No maintenance records have been saved."));
        } else {
            card.getChildren().addAll(
                    new Label(record.getServiceType()),
                    new Label("Date: " + record.getDate()),
                    new Label(String.format("Mileage: %,d miles", record.getMileage())),
                    new Label(String.format("Cost: $%,.2f", record.getCost())));
        }

        summaryEvents.getChildren().add(card);
    }

    private void addBreakdownCard(Map<String, Integer> breakdown) {
        VBox card = createCard();
        card.getChildren().add(createHeading("Service Breakdown"));

        if (breakdown.isEmpty()) {
            card.getChildren().add(new Label("No service categories to display."));
        } else {
            breakdown.forEach((type, count) ->
                    card.getChildren().add(createStatisticRow(type, String.valueOf(count))));
        }

        summaryEvents.getChildren().add(card);
    }

    private HBox createStatisticRow(String name, String value) {
        Label nameLabel = new Label(name);
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 13));

        HBox spacerRow = new HBox(nameLabel, valueLabel);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        return spacerRow;
    }

    private VBox createCard() {
        VBox card = new VBox(7);
        card.setPadding(new Insets(13));
        card.setStyle("-fx-background-color: white; -fx-border-color: #b8b8b8;" +
                "-fx-border-radius: 5; -fx-background-radius: 5;");
        return card;
    }

    private Label createHeading(String text) {
        Label heading = new Label(text);
        heading.setFont(Font.font("System", FontWeight.BOLD, 18));
        return heading;
    }

    private void addMessage(String message) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.setPadding(new Insets(15));
        summaryEvents.getChildren().add(label);
    }

    @FXML
    public void returnToDashboard(ActionEvent event) {
        switchScene(event, DASHBOARD_FXML);
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | NullPointerException exception) {
            throw new IllegalStateException("Unable to open " + fxmlPath, exception);
        }
    }
}