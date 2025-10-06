module com.falkknudsen.regless {
    requires javafx.controls;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires javafx.graphics;
    requires javafx.web;
    requires org.jsoup;
    requires org.jspecify;
    requires java.desktop;

    opens com.falkknudsen.regless;
    exports com.falkknudsen.regless;
}