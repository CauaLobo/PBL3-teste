module com.projeto.jogouefs {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.projeto.jogouefs.view;
    opens com.projeto.jogouefs.view to javafx.fxml;
}