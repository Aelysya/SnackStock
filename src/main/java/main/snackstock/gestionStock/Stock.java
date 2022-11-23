package main.snackstock.gestionStock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Stock {

    private static final List<Item> snacksList, boissonsList, autresList;
    private static final String mdp = "mdp";

    static {
        snacksList = new ArrayList<>();
        boissonsList = new ArrayList<>();
        autresList = new ArrayList<>();
    }

    public Stock(){}

    public static void addItem(Item item, String type){
        switch (type) {
            case "snack" -> snacksList.add(item);
            case "boisson" -> boissonsList.add(item);
            case "autre" -> autresList.add(item);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static void supprItem(Item i, String type){
        switch (type) {
            case "snack" -> snacksList.removeIf(item -> (Objects.equals(item.getNAME(), i.getNAME())));
            case "boisson" -> boissonsList.removeIf(item -> (Objects.equals(item.getNAME(), i.getNAME())));
            case "autre" -> autresList.removeIf(item -> (Objects.equals(item.getNAME(), i.getNAME())));
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static void addOneToItem(Item i, String type){
        List<Item> list = switch (type) {
            case "snack" -> snacksList;
            case "boisson" -> boissonsList;
            case "autre" -> autresList;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        for(Item item : list){
            if(i.getNAME().equals(item.getNAME())){
                item.addOneToQuantity();
            }
        }
    }

    public static void removeOneToItem(Item i, String type){
        List<Item> list = switch (type) {
            case "snack" -> snacksList;
            case "boisson" -> boissonsList;
            case "autre" -> autresList;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        for(Item item : list){
            if(i.getNAME().equals(item.getNAME())){
                item.buy(1);
            }
        }
    }

    public static List<Item> getSnacksList(){
        return snacksList;
    }

    public static List<Item> getBoissonsList(){
        return boissonsList;
    }

    public static List<Item> getAutresList(){
        return autresList;
    }

    public static String getMdp(){
        return mdp;
    }
}
