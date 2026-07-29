module com.autolog.autolog {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.autolog.autolog to javafx.fxml;
    opens com.autolog.autolog.controller to javafx.fxml;
    opens com.autolog.autolog.model to javafx.fxml;

    exports com.autolog.autolog;
    exports com.autolog.autolog.controller;
    exports com.autolog.autolog.model;
}