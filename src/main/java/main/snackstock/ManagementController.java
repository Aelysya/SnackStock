package main.snackstock;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.snackstock.controllers.BaseController;
import main.snackstock.gestionStock.Item;
import main.snackstock.gestionStock.Stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ManagementController extends BaseController {
    @FXML
    private Label currentTabLabel, totalPriceLabel;

    @FXML
    private Button autresButton, boissonsButton, snacksButton, ajouterButton, sortirButton, calcButton;

    @FXML
    private DatePicker fromDate, toDate;

    @FXML
    private GridPane itemsGrid;

    public void initialize() {
        snacksButton.setOnAction(event -> showTab("snack"));
        boissonsButton.setOnAction(event -> showTab("boisson"));
        autresButton.setOnAction(event -> showTab("autre"));

        calcButton.setOnAction(event -> {
            try {
                calculateTotalPrice();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ajouterButton.setOnAction(event -> launchAddProduct());
        sortirButton.setOnAction(event -> {
            try {
                mainController.updateStockFile();
                mainController.clearCart();
                mainController.showTab("snack");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage) boissonsButton.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Lance la fenêtre pour ajouter un nouvel item
     */
    public void launchAddProduct() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-product.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 360, 330);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddProductController c = fxmlLoader.getController();
        c.setManagementController(this);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Calcule le prix des ventes sur une période choisie
     * @throws IOException Fichier de ventes introuvables
     */
    public void calculateTotalPrice() throws IOException {
        String path = "src/main/resources/main/snackstock/ventes.txt";
        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        String row;
        double finalPrice = 0;
        int l;
        String StringPrice;
        while((row = fileReader.readLine()) != null){
            String[] data = row.split(", ");
            l = data.length;
            StringPrice = data[l-1].substring(12, data[l-1].length()-2);
            finalPrice += Double.parseDouble(StringPrice);
        }

        totalPriceLabel.setText(finalPrice + " €");

        fileReader.close();
    }

    /**
     * Affiche l'onglet choisi
     * @param type Nom de l'onglet à afficher
     */
    public void showTab(String type){
        itemsGrid.getChildren().clear();

        int cpt = 0;
        List<Item> list = Stock.getListFromTypeString(type);

        for(Item i : list){
            Label name = new Label(i.getNAME());
            name.setStyle("-fx-font-size: 20px");

            Label price = new Label(i.getPRICE() + " €");
            price.setStyle("-fx-font-size: 20px");

            Label quantity = new Label(Integer.toString(i.getQuantity()));
            quantity.setStyle("-fx-font-size: 20px");
            quantity.textProperty().addListener((observableValue, s, t1) -> {
                if(Integer.parseInt(t1) <= 0){
                    Stock.supprItem(i);
                    showTab(type);
                }
            });

            Button add = new Button("+");
            add.setPrefWidth(50);
            add.setPrefHeight(20);
            add.setOnAction(event -> {
                quantity.setText(Integer.toString(Integer.parseInt(quantity.getText())+1));
                Stock.addOneToItem(i);
            });

            Button remove = new Button("-");
            remove.setPrefWidth(50);
            remove.setPrefHeight(20);
            remove.setOnAction(event -> {
                quantity.setText(Integer.toString(Integer.parseInt(quantity.getText())-1));
                Stock.removeQuantityFromItem(i, 1);
            });

            Button suppr = new Button("Supprimer");
            suppr.setPrefWidth(80);
            suppr.setPrefHeight(20);
            suppr.setOnAction(event -> {
                Stock.supprItem(i);
                showTab(type);
            });

            itemsGrid.add(suppr, 0, cpt);
            itemsGrid.add(name, 1, cpt);
            itemsGrid.add(price, 2, cpt);
            itemsGrid.add(remove, 3, cpt);
            itemsGrid.add(quantity, 4, cpt);
            itemsGrid.add(add, 5, cpt);

            cpt++;
        }

        //Update le label sous les onglets en prenant le type, ajoute un 's', et met la 1ère lettre en majuscule
        String s;
        String firstLetter = type.substring(0, 1);
        String otherLetters = type.substring(1);
        s = firstLetter.toUpperCase() + otherLetters + "s";
        currentTabLabel.setText(s);
    }
}