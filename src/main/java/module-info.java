module com.example.reproductormusica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.reproductormusica to javafx.fxml;
    exports com.example.reproductormusica;
}