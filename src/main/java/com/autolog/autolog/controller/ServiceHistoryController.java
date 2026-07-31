package com.autolog.autolog.controller;

import com.autolog.autolog.model.ServiceManager;
import com.autolog.autolog.model.ServiceRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
    private Label totalAmountLabel;

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

            totalAmountLabel.setText(
                    String.format("$%,.2f", serviceManager.getTotalCost())
            );

            if (records.isEmpty()) {
                addMessage("No maintenance records have been saved.");
                return;
            }

            // Display newest service records first.
            for (int index = records.size() - 1; index >= 0; index--) {
                VBox card = createServiceCard(records.get(index));
                card.setMaxWidth(Double.MAX_VALUE);
                summaryEvents.getChildren().add(card);
            }

        } catch (IOException | IllegalArgumentException exception) {
            totalAmountLabel.setText("$0.00");
            addMessage(
                    "Unable to load service history: "
                            + exception.getMessage()
            );
        }
    }

    private VBox createServiceCard(ServiceRecord record) {
        Label serviceTypeLabel =
                new Label(record.getServiceType());

        serviceTypeLabel.setFont(
                Font.font("System", FontWeight.BOLD, 17)
        );

        Label dateLabel =
                new Label(record.getDate());

        dateLabel.setStyle(
                "-fx-text-fill: #555555;"
        );

        Label mileageLabel = new Label(
                String.format("%,d miles", record.getMileage())
        );

        Label costLabel = new Label(
                String.format("$%,.2f", record.getCost())
        );

        costLabel.setFont(
                Font.font("System", FontWeight.BOLD, 14)
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox informationRow = new HBox(
                10,
                mileageLabel,
                spacer,
                costLabel
        );

        informationRow.setAlignment(Pos.CENTER_LEFT);

        String notesText =
                record.getNotes() == null
                        || record.getNotes().isBlank()
                        ? "No notes"
                        : record.getNotes();

        Label notesLabel = new Label(notesText);
        notesLabel.setWrapText(true);
        notesLabel.setMaxWidth(Double.MAX_VALUE);
        notesLabel.setStyle(
                "-fx-text-fill: #555555;"
        );

        VBox card = new VBox(
                6,
                serviceTypeLabel,
                dateLabel,
                informationRow,
                notesLabel
        );

        card.setPadding(new Insets(12));
        card.setMaxWidth(Double.MAX_VALUE);

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-border-color: #c8c8c8;"
                        + "-fx-border-width: 1;"
                        + "-fx-background-radius: 5;"
                        + "-fx-border-radius: 5;"
        );

        return card;
    }

    private void addMessage(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setPadding(new Insets(15));

        summaryEvents.getChildren().add(messageLabel);
    }

    @FXML
    public void returnToDashboard(ActionEvent event) {
        switchScene(event, DASHBOARD_FXML);
    }

    private void switchScene(
            ActionEvent event,
            String fxmlPath
    ) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource(fxmlPath)
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException | NullPointerException exception) {
            throw new IllegalStateException(
                    "Unable to open " + fxmlPath,
                    exception
            );
        }
    }
}