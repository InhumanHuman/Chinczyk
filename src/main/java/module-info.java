module com.example.chinczyk {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.chinczyk to javafx.fxml;
    exports com.example.chinczyk;
}