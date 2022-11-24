package main.snackstock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.snackstock.gestionStock.Item;
import main.snackstock.gestionStock.Stock;

public class AddProductController {
    @FXML
    private Button confirmButton, annulerButton;

    @FXML
    private TextField nameField, priceField, quantityField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Label topTextLabel;

    private ManagementController managementController;

    public void initialize() {
        typeComboBox.getItems().addAll(
                "snack",
                "boisson",
                "autre"
        );
        confirmButton.setOnAction(event -> checkProduct());
        annulerButton.setOnAction(event -> {
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.close();
        });
    }

    public void setManagementController(ManagementController mc){
        this.managementController = mc;
    }

    public void checkProduct() throws NumberFormatException{
        int qty;
        try{
            if(Integer.parseInt(quantityField.getText()) <=0){
                throw new NumberFormatException();
            } else {
                qty = Integer.parseInt(quantityField.getText());
            }
        } catch (NumberFormatException e) {
            topTextLabel.setText("Quantité invalide");
            return;
        }

        try{
            Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            topTextLabel.setText("Prix invalide");
            return;
        }

        if(typeComboBox.getValue() == null){
            topTextLabel.setText("Choisissez un type");
            return;
        }

        Item i = new Item(nameField.getText(), qty, priceField.getText(), typeComboBox.getValue());
        Stock.addItem(i);
        managementController.showTab(typeComboBox.getValue());
        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }
}
