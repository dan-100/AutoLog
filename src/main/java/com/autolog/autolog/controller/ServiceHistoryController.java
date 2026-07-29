package com.autolog.autolog.controller;

import com.autolog.autolog.model.ServiceManager;
import com.autolog.autolog.model.ServiceRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
/**
 * Controls the service history screen by loading and displaying all
 * recorded maintenance services for the selected vehicle.
 *
 * @author Daniel Hernandez
 */
public class ServiceHistoryController {

    private static final String DASHBOARD_FXML =
            "/com/autolog/autolog/layouts/mobileDashboard.fxml";

    private final ServiceManager serviceManager = new ServiceManager();

    @FXML
    private VBox summaryEvents;

    @FXML
    public void initialize() {
        loadServiceHistory();
    }

    public void showView() {
        loadServiceHistory();
    }

    public void loadServiceHistory() {
        summaryEvents.getChildren().clear();

        try {
            List<ServiceRecord> records = serviceManager.loadServices();
            addTotalCostCard();

            if (records.isEmpty()) {
                addMessage("No maintenance records have been saved.");
                return;
            }

            // Display newest entries first while preserving CSV order internally.
            for (int index = records.size() - 1; index >= 0; index--) {
                summaryEvents.getChildren().add(createServiceCard(records.get(index)));
            }
        } catch (IOException | IllegalArgumentException exception) {
            addMessage("Unable to load service history: " + exception.getMessage());
        }
    }

    private void addTotalCostCard() {
        Label title = new Label("Total Spent on Maintenance");
        title.setFont(Font.font("System", FontWeight.BOLD, 17));

        Label amount = new Label(String.format("$%,.2f", serviceManager.getTotalCost()));
        amount.setFont(Font.font("System", FontWeight.BOLD, 22));

        VBox card = new VBox(6, title, amount);
        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color: #033375; -fx-background-radius: 6;" +
                "-fx-border-radius: 6;");

        title.setStyle("-fx-text-fill: white;");
        amount.setStyle("-fx-text-fill: white;");
        summaryEvents.getChildren().add(card);
    }

    private VBox createServiceCard(ServiceRecord record) {
        Label type = new Label(record.getServiceType());
        type.setFont(Font.font("System", FontWeight.BOLD, 17));

        Label date = new Label("Date: " + record.getDate());
        Label mileage = new Label(String.format("Mileage: %,d miles", record.getMileage()));
        Label cost = new Label(String.format("Cost: $%,.2f", record.getCost()));

        String notesText = record.getNotes() == null || record.getNotes().isBlank()
                ? "Notes: None"
                : "Notes: " + record.getNotes();
        Label notes = new Label(notesText);
        notes.setWrapText(true);

        VBox card = new VBox(5, type, date, mileage, cost, notes);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: white; -fx-border-color: #b8b8b8;" +
                "-fx-border-radius: 5; -fx-background-radius: 5;");
        return card;
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