package main.snackstock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import main.snackstock.controllers.BaseController;
import main.snackstock.gestionStock.Stock;

import java.io.IOException;

public class ConfirmSaleController extends BaseController {
    @FXML
    private Button confirmButton, annulerButton;

    @FXML
    private PasswordField passField;

    @FXML
    private Label topTextLabel;

    public void initialize() {
        confirmButton.setOnAction(event -> {
            try {
                checkPassword();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        annulerButton.setOnAction(event -> {
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Vérifie le mot de passe entré par l'utilisateur, lance la confirmation de l'achat
     * @throws IOException  Fichier de stock introuvable
     */
    public void checkPassword() throws IOException {
        if(passField.getText().equals(Stock.getMdp())){
            mainController.confirmSale();
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } else {
            topTextLabel.setText("Mot de passe erroné");
        }
    }
}
