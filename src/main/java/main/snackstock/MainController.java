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
import main.snackstock.gestionStock.CartContent;
import main.snackstock.gestionStock.Item;
import main.snackstock.gestionStock.Stock;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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

    private String currentTab;

    public void initialize() throws IOException {
        currentTab = "None";

        readStockFile();
        /*Stock.addItem(new Item("Kit Kat", 2, "0.60"), "snack");
        Stock.addItem(new Item("Granola", 5, "0.60"), "snack");
        Stock.addItem(new Item("Skittles", 10, "0.60"), "snack");
        Stock.addItem(new Item("Nutella B-Ready", 54, "0.60"), "snack");
        Stock.addItem(new Item("Dragibus", 8, "0.60"), "snack");
        Stock.addItem(new Item("Mars", 9, "0.60"), "snack");
        Stock.addItem(new Item("Coca-cola", 2, "0.60"), "boisson");
        Stock.addItem(new Item("Ice Tea", 2, "0.60"), "boisson");
        Stock.addItem(new Item("Sprite", 2, "0.60"), "boisson");
        Stock.addItem(new Item("Bouteille d'eau", 5, "1.20"), "autre");*/

        snacksButton.setOnAction(event -> showTab("snack"));
        boissonsButton.setOnAction(event -> showTab("boisson"));
        autresButton.setOnAction(event -> showTab("autre"));

        gererButton.setOnAction(event -> launchAuthBeforeManagement());

        validerButton.setOnAction(event -> launchConfirmation());
        annulerButton.setOnAction(event -> clearCart());

        freeMenuField.textProperty().addListener((observableValue, s, t1) -> computePrice());
        freeConsoField.textProperty().addListener((observableValue, s, t1) -> computePrice());
    }

    public void readStockFile() throws IOException {
        String path = "src/main/resources/main/snackstock/stock.csv";
        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        String row;
        while((row = csvReader.readLine()) != null){
            String[] data = row.split(",");
            Item item = new Item(data[0], Integer.parseInt(data[1]), data[2]);
            Stock.addItem(item, data[3]);
        }
        csvReader.close();
    }

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
        currentTab = type;
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
            add.setOnAction(event -> {
                if(i.getQuantity() != 0){
                    boolean alreadyInCart = false;
                    List<Item> listCart = switch (type) {
                        case "snack" -> CartContent.getSnacksList();
                        case "boisson" -> CartContent.getBoissonsList();
                        case "autre" -> CartContent.getAutresList();
                        default -> new ArrayList<>();
                    };
                    for(Item item : listCart){
                        if (item.getNAME().equals(i.getNAME())) {
                            alreadyInCart = true;
                            break;
                        }
                    }
                    if(!alreadyInCart){
                        CartContent.addItem(i, type);
                        addItemToCart(i, type);
                    }
                    updateTabLabel();
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
        }
    }

    public void addItemToCart(Item item, String type){
        int newRow = cartGrid.getRowCount();

        Label name = new Label(item.getNAME());
        name.setStyle("-fx-font-size: 20px");
        Label qty = new Label("1");
        qty.setStyle("-fx-font-size: 20px");
        Button add = new Button("+");
        add.setPrefWidth(40);
        add.setPrefHeight(20);
        add.setOnAction(event -> {
            qty.setText(Integer.toString(Integer.parseInt(qty.getText()) + 1));
            if(Integer.parseInt(qty.getText()) > item.getQuantity()){
                qty.setText(Integer.toString(item.getQuantity()));
            } else {
                CartContent.addOneToItem(item, type);
                computePrice();
            }
        });
        Button remove = new Button("-");
        remove.setPrefWidth(40);
        remove.setPrefHeight(20);
        remove.setOnAction(event -> {
            qty.setText(Integer.toString(Integer.parseInt(qty.getText()) - 1));
            CartContent.removeOneToItem(item, type);
            computePrice();
        });

        qty.textProperty().addListener((observableValue, s, t1) -> {
            if(Integer.parseInt(t1) <= 0){
                removeFromCart(newRow);
                CartContent.supprItem(item, type);
                computePrice();
            }
        });

        cartGrid.add(name, 0, newRow);
        cartGrid.add(remove, 1, newRow);
        cartGrid.add(qty, 2, newRow);
        cartGrid.add(add, 3, newRow);
        computePrice();
    }

    public void removeFromCart(int row){
        cartGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == row);
    }

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

        //Les blocs catch sont vides c'est normal, c'est pour avoir un update du prix même quand on laisse un champ vide
        try{
            prix -= Double.parseDouble(freeConsoField.getText()) * 0.6;
        } catch (NumberFormatException e){
        }

        try{
            prix -= Double.parseDouble(freeMenuField.getText()) * 1;
        } catch (NumberFormatException e){
        }

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_DOWN);

        priceLabel.setText(df.format(prix) + " €");
        updateTabLabel();
    }

    public void confirmSale() throws IOException {
        for(Item i : CartContent.getSnacksList()){
            Stock.removeQuantityFromItem(i, "snack", i.getQuantity());
        }
        for(Item i : CartContent.getBoissonsList()){
            Stock.removeQuantityFromItem(i, "boisson", i.getQuantity());
        }
        for(Item i : CartContent.getAutresList()){
            Stock.removeQuantityFromItem(i, "autre", i.getQuantity());
        }
        updateStockFile();
        clearCart();
    }

    public void clearCart(){
        cartGrid.getChildren().clear();
        CartContent.clear();
        computePrice();
        showTab("snack");
    }
}