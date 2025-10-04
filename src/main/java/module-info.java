module com.falkknudsen.regless {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires javafx.graphics;
    requires javafx.web;

    opens com.falkknudsen.regless to javafx.fxml;
    exports com.falkknudsen.regless;
}