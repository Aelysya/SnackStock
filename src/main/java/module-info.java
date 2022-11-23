module main.snackstock {
    requires javafx.controls;
    requires javafx.fxml;


    opens main.snackstock to javafx.fxml;
    exports main.snackstock;
    exports main.snackstock.gestionStock;
    opens main.snackstock.gestionStock to javafx.fxml;
}