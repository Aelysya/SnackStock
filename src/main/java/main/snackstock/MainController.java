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
import main.snackstock.controllers.BaseController;
import main.snackstock.gestionStock.CartContent;
import main.snackstock.gestionStock.Item;
import main.snackstock.gestionStock.Stock;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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

    private String currentTab;

    public void initialize() throws IOException {
        currentTab = "None";

        readStockFile();

        snacksButton.setOnAction(event -> showTab("snack"));
        boissonsButton.setOnAction(event -> showTab("boisson"));
        autresButton.setOnAction(event -> showTab("autre"));

        gererButton.setOnAction(event -> launchNewWindow("auth"));

        validerButton.setOnAction(event -> launchNewWindow("confirm"));
        annulerButton.setOnAction(event -> {
            freeMenuField.setText("0");
            freeConsoField.setText("0");
            clearCart();
        });

        freeMenuField.textProperty().addListener((observableValue, s, t1) -> computePrice());
        freeConsoField.textProperty().addListener((observableValue, s, t1) -> computePrice());
    }

    /**
     * Lit le fichier de stock au lancement de l'application
     * @throws IOException Fichier de stock introuvable
     *
     */
    public void readStockFile() throws IOException {
        String path = "src/main/resources/main/snackstock/stock.csv";
        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        String row;
        while((row = csvReader.readLine()) != null){
            String[] data = row.split(",");
            Item item = new Item(data[0], Integer.parseInt(data[1]), data[2], data[3]);
            Stock.addItem(item);
        }
        csvReader.close();
    }

    /**
     * Met à jour le stock
     * @throws IOException Fichier de stock introuvable
     */
    public void updateStockFile() throws IOException {
        FileWriter csvWriter = new FileWriter("src/main/resources/main/snackstock/stock.csv");
        for(Item i : Stock.getSnacksList()){
            csvWriter.append(i.getNAME()).append(",");
            csvWriter.append(Integer.toString(i.getQuantity())).append(",");
            csvWriter.append(i.getPRICE()).append(",");
            csvWriter.append("snack\n");
        }

        for(Item i : Stock.getBoissonsList()){
            csvWriter.append(i.getNAME()).append(",");
            csvWriter.append(Integer.toString(i.getQuantity())).append(",");
            csvWriter.append(i.getPRICE()).append(",");
            csvWriter.append("boisson\n");
        }

        for(Item i : Stock.getAutresList()){
            csvWriter.append(i.getNAME()).append(",");
            csvWriter.append(Integer.toString(i.getQuantity())).append(",");
            csvWriter.append(i.getPRICE()).append(",");
            csvWriter.append("autre\n");
        }

        csvWriter.flush();
        csvWriter.close();
    }

    /**
     * Lance une nouvelle fenêtre
     * @param controllerName Nom du controller pour déterminer les paramètres de lancement de la nouvelle fenêtre
     */
    public void launchNewWindow(String controllerName){
        String fxmlName;
        int width, height;
        switch(controllerName){
            case "auth" -> {
                width = 360;
                height = 200;
                fxmlName = "auth-management.fxml";
            }
            case "management" -> {
                width = 1280;
                height = 720;
                fxmlName = "management-view.fxml";
            }
            case "confirm" -> {
                width = 360;
                height = 200;
                fxmlName = "confirm-sale.fxml";
            }
            default -> throw new IllegalStateException("Unexpected value: " + controllerName);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BaseController c = fxmlLoader.getController();
        c.setMainController(this);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Affiche l'onglet choisi
     * @param type Nom de l'onglet à afficher
     */
    public void showTab(String type){
        currentTab = type;
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

            Button add = new Button("Ajouter");
            add.setPrefWidth(100);
            add.setPrefHeight(30);
            add.setOnAction(event -> {
                if(i.getQuantity() != 0){
                    boolean alreadyInCart = false;
                    List<Item> listCart = CartContent.getListFromTypeString(type);
                    for(Item item : listCart){
                        if (item.getNAME().equals(i.getNAME())) {
                            alreadyInCart = true;
                            break;
                        }
                    }
                    if(!alreadyInCart){
                        CartContent.addItem(i);
                        addItemToCart(i);
                    }
                }
            });

            itemsGrid.add(name, 0, cpt);
            itemsGrid.add(type.equals("autre") ? price : new Label(""), 1, cpt);
            itemsGrid.add(quantity, 2, cpt);
            itemsGrid.add(add, 3, cpt);

            cpt++;
        }
        updateTabLabel();
    }

    /**
     * Met à jour le label situé en dessous des onglets avec le prix d'ajout d'un item dans le panier
     */
    public void updateTabLabel(){
        int nbSnacks = 0;
        for (Item i : CartContent.getSnacksList()){
            nbSnacks += i.getQuantity();
        }

        int nbBoissons = 0;
        for (Item i : CartContent.getBoissonsList()){
            nbBoissons += i.getQuantity();
        }

        switch (currentTab) {
            case "snack" -> currentTabLabel.setText(nbBoissons > nbSnacks ? "Snacks - 0.40 €" : "Snacks - 0.60 €");
            case "boisson" -> currentTabLabel.setText(nbSnacks > nbBoissons ? "Boissons - 0.40 €" : "Boissons - 0.60 €");
            case "autre" -> currentTabLabel.setText("Autres");
            default -> throw new IllegalStateException("Unexpected value: " + currentTab);
        }
    }

    /**
     * Ajoute un item au panier
     * @param item L'item à ajouter
     */
    public void addItemToCart(Item item){
        int newRow = cartGrid.getRowCount();

        Label name = new Label(item.getNAME());
        name.setStyle("-fx-font-size: 20px");

        Label qty = new Label("1");
        qty.setStyle("-fx-font-size: 20px");
        qty.textProperty().addListener((observableValue, s, t1) -> {
            if(Integer.parseInt(t1) <= 0){
                cartGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == newRow);
                CartContent.supprItem(item);
                computePrice();
            }
        });

        Button add = new Button("+");
        add.setPrefWidth(40);
        add.setPrefHeight(20);
        add.setOnAction(event -> {
            qty.setText(Integer.toString(Integer.parseInt(qty.getText()) + 1));
            if(Integer.parseInt(qty.getText()) > item.getQuantity()){
                qty.setText(Integer.toString(item.getQuantity()));
            } else {
                CartContent.addOneToItem(item);
                computePrice();
            }
        });

        Button remove = new Button("-");
        remove.setPrefWidth(40);
        remove.setPrefHeight(20);
        remove.setOnAction(event -> {
            qty.setText(Integer.toString(Integer.parseInt(qty.getText()) - 1));
            CartContent.removeOneFromItem(item);
            computePrice();
        });

        cartGrid.add(name, 0, newRow);
        cartGrid.add(remove, 1, newRow);
        cartGrid.add(qty, 2, newRow);
        cartGrid.add(add, 3, newRow);
        computePrice();
    }

    /**
     * Calcule le prix selon le contenu du panier
     */
    public void computePrice(){
        List<Item> snackList = CartContent.getSnacksList();
        List<Item> boissonList = CartContent.getBoissonsList();
        List<Item> autreList = CartContent.getAutresList();

        double prixSnacks = 0.0;
        int nbSnacks = 0;
        for (Item i : snackList){
            prixSnacks += i.getQuantity() * Double.parseDouble(i.getPRICE());
            nbSnacks += i.getQuantity();
        }

        double prixBoissons = 0.0;
        int nbBoissons = 0;
        for (Item i : boissonList){
            prixBoissons += i.getQuantity() * Double.parseDouble(i.getPRICE());
            nbBoissons += i.getQuantity();
        }

        double prixAutres = 0.0;
        for (Item i : autreList){
            prixAutres += i.getQuantity() * Double.parseDouble(i.getPRICE());
        }

        double prix = prixSnacks + prixBoissons + prixAutres;

        while(nbSnacks > 0 && nbBoissons > 0){
            prix -= 0.2;
            nbSnacks--;
            nbBoissons--;
        }

        try{
            prix -= Double.parseDouble(freeConsoField.getText()) * 0.6;
        } catch (NumberFormatException ignored) {
        }

        try{
            prix -= Double.parseDouble(freeMenuField.getText()) * 1;
        } catch (NumberFormatException ignored) {
        }

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_DOWN);

        priceLabel.setText(df.format(prix) + " €");
        updateTabLabel();
    }

    /**
     * Confirme l'achat des items du panier
     * @throws IOException Fichier de stock introuvable
     */
    public void confirmSale() throws IOException {
        for(Item i : CartContent.getSnacksList()){
            Stock.removeQuantityFromItem(i, i.getQuantity());
        }

        for(Item i : CartContent.getBoissonsList()){
            Stock.removeQuantityFromItem(i, i.getQuantity());
        }

        for(Item i : CartContent.getAutresList()){
            Stock.removeQuantityFromItem(i, i.getQuantity());
        }

        freeMenuField.setText("0");
        freeConsoField.setText("0");
        updateStockFile();
        clearCart();
    }

    /**
     * Vide le panier et reset le compteur de prix
     */
    public void clearCart(){
        cartGrid.getChildren().clear();
        CartContent.clear();
        computePrice();
        showTab("snack");
    }
}