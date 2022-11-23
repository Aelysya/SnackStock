package main.snackstock;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.snackstock.gestionStock.Item;
import main.snackstock.gestionStock.Stock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private Label priceLabel, currentTabLabel;

    @FXML
    private Button validerButton, annulerButton, autresButton, boissonsButton, snacksButton, gererButton;

    @FXML
    private GridPane itemsGrid, cartGrid;

    @FXML
    private TextField freeConsoField, freeMenuField;

    public void initialize() {
        Stock.addItem(new Item("Kit Kat", 2, "0.60"), "snack");
        Stock.addItem(new Item("Granola", 5, "0.60"), "snack");
        Stock.addItem(new Item("Skittles", 10, "0.60"), "snack");
        Stock.addItem(new Item("Nutella B-Ready", 54, "0.60"), "snack");
        Stock.addItem(new Item("Dragibus", 8, "0.60"), "snack");
        Stock.addItem(new Item("Mars", 9, "0.60"), "snack");
        Stock.addItem(new Item("Coca-cola", 2, "0.60"), "boisson");
        Stock.addItem(new Item("Ice Tea", 2, "0.60"), "boisson");
        Stock.addItem(new Item("Sprite", 2, "0.60"), "boisson");
        Stock.addItem(new Item("Bouteille d'eau", 5, "1.20"), "autre");

        snacksButton.setOnAction(event -> showTab("snack"));
        boissonsButton.setOnAction(event -> showTab("boisson"));
        autresButton.setOnAction(event -> showTab("autre"));

        gererButton.setOnAction(event -> launchAuthBeforeManagement());

        validerButton.setOnAction(event -> launchConfirmation());
        annulerButton.setOnAction(event -> clearCart());
    }

    public void launchAuthBeforeManagement() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("auth-management.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 360, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AuthManagementController c = fxmlLoader.getController();
        c.setMainController(this);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public void launchStockManagement() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("management-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1280, 720);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ManagementController c = fxmlLoader.getController();
        c.setMainController(this);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public void launchConfirmation() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("confirm-sale.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 360, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConfirmSaleController c = fxmlLoader.getController();
        c.setMainController(this);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public void showTab(String type){
        itemsGrid.getChildren().clear();
        int cpt = 0;
        List<Item> list = switch (type) {
            case "snack" -> Stock.getSnacksList();
            case "boisson" -> Stock.getBoissonsList();
            case "autre" -> Stock.getAutresList();
            default -> new ArrayList<>();
        };

        for(Item i : list){
            Label name = new Label(i.getNAME());
            name.setStyle("-fx-font-size: 20px");
            Label price = new Label(i.getPRICE() + " €");
            price.setStyle("-fx-font-size: 20px");
            Label quantity = new Label(Integer.toString(i.getQuantity()));
            quantity.setStyle("-fx-font-size: 20px");
            Button add = new Button("Ajouter");
            add.setPrefWidth(100);
            add.setPrefHeight(30);
            add.setOnAction(event -> addItemToCart(i));

            itemsGrid.add(name, 0, cpt);
            itemsGrid.add(price, 1, cpt);
            itemsGrid.add(quantity, 2, cpt);
            itemsGrid.add(add, 3, cpt);

            cpt++;
        }

        currentTabLabel.setText(switch (type) {
            case "snack" -> "Snacks";
            case "boisson" -> "Boissons";
            case "autre" -> "Autres";
            default -> "Aucun onglet sélectionné";
        });
    }

    public void addItemToCart(Item item){
        int newRow = cartGrid.getRowCount();

        Label name = new Label(item.getNAME());
        name.setStyle("-fx-font-size: 20px");
        Label qty = new Label("1");
        qty.setStyle("-fx-font-size: 20px");
        Button add = new Button("+");
        add.setPrefWidth(40);
        add.setPrefHeight(20);
        add.setOnAction(event -> qty.setText(Integer.toString(Integer.parseInt(qty.getText())+1)));
        Button remove = new Button("-");
        remove.setPrefWidth(40);
        remove.setPrefHeight(20);
        remove.setOnAction(event -> qty.setText(Integer.toString(Integer.parseInt(qty.getText())-1)));

        qty.textProperty().addListener((observableValue, s, t1) -> {
            if(Integer.parseInt(t1) <= 0){
                removeFromCart(newRow);
            }
        });

        cartGrid.add(name, 0, newRow);
        cartGrid.add(remove, 1, newRow);
        cartGrid.add(qty, 2, newRow);
        cartGrid.add(add, 3, newRow);
    }

    public void removeFromCart(int row){
        cartGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == row);
    }

    public void clearCart(){
        cartGrid.getChildren().clear();
        priceLabel.setText("0,00 €");
    }
}