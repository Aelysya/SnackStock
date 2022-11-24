package main.snackstock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import main.snackstock.controllers.BaseController;
import main.snackstock.gestionStock.Stock;

public class AuthManagementController extends BaseController {
    @FXML
    private Button confirmButton, annulerButton;

    @FXML
    private PasswordField passField;

    @FXML
    private Label topTextLabel;

    public void initialize() {
        confirmButton.setOnAction(event -> checkPassword());
        annulerButton.setOnAction(event -> {
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Vérifie le mot de passe entré par l'utilisateur, si correct lance la fenêtre de gestion
     */
    public void checkPassword(){
        if(passField.getText().equals(Stock.getMdp())){
            this.mainController.launchNewWindow("management");
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } else {
            topTextLabel.setText("Mot de passe erroné");
        }
    }
}
