package main.snackstock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import main.snackstock.gestionStock.Stock;

import java.io.IOException;

public class ConfirmSaleController {
    @FXML
    private Button confirmButton, annulerButton;

    @FXML
    private PasswordField passField;

    @FXML
    private Label topTextLabel;

    private MainController mainController;

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

    public void setMainController(MainController mc){
        this.mainController = mc;
    }

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
