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
        priceField.setDisable(true);
        typeComboBox.getItems().addAll(
                "snack",
                "boisson",
                "autre"
        );
        typeComboBox.valueProperty().addListener((observableValue, s, t1) -> priceField.setDisable(!t1.equals("autre")));
        confirmButton.setOnAction(event -> checkProduct());
        annulerButton.setOnAction(event -> {
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Vérifie les valeurs entrées dans les champs avant d'ajouter l'item dans le stock
     * @throws NumberFormatException La quantité ou le prix ont un format invalide
     */
    public void checkProduct() throws NumberFormatException{
        int qty;
        String selectedType = typeComboBox.getValue();
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

        if(selectedType == null){
            topTextLabel.setText("Choisissez un type");
            return;
        }

        String price;
        if(selectedType.equals("snack") || selectedType.equals("boisson")){
            price = "0.60";
        } else {
            try {
                Double.parseDouble(priceField.getText());
                price = priceField.getText();
            } catch (NumberFormatException e) {
                topTextLabel.setText("Prix invalide");
                return;
            }
        }


        Item i = new Item(nameField.getText(), qty, price, selectedType);
        Stock.addItem(i);
        managementController.showTab(selectedType);
        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }

    public void setManagementController(ManagementController mc){
        this.managementController = mc;
    }
}
