package main.snackstock.gestionStock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartContent {

    /**
     * Liste des item de type snack
     */
    private static final List<Item> snacksList;

    /**
     * Liste des item de type boisson
     */
    private static final List<Item> boissonsList;

    /**
     * Liste des item de type autre
     */
    private static final List<Item> autresList;

    static {
        snacksList = new ArrayList<>();
        boissonsList = new ArrayList<>();
        autresList = new ArrayList<>();
    }

    /**
     * Ajoute un item au stock
     * @param item L'item à ajouter
     */
    public static void addItem(Item item){
        Item newItem = new Item(item);
        newItem.setQuantity(1);
        getListFromTypeString(item.getTYPE()).add(newItem);
    }

    /**
     * Supprime un item du stock
     * @param item L'item à supprimer
     */
    public static void supprItem(Item item){
        getListFromTypeString(item.getTYPE()).removeIf(i -> (Objects.equals(item.getNAME(), i.getNAME())));
    }

    /**
     * Ajoute 1 à la quantité d'un item en stock
     * @param item L'item dont il faut augmenter la quantité
     */
    public static void addOneToItem(Item item){
        for(Item i : getListFromTypeString(item.getTYPE())){
            if(i.getNAME().equals(item.getNAME())){
                i.addOneToQuantity();
            }
        }
    }

    /**
     * Retire un certaine quantité d'un item en stock
     * @param item L'item dont il faut réduire la quantité
     */
    public static void removeOneFromItem(Item item){
        for(Item i : getListFromTypeString(item.getTYPE())){
            if(i.getNAME().equals(item.getNAME())){
                i.removeFromQuantity(1);
            }
        }
    }

    /**
     * Vide le panier
     */
    public static void clear(){
        snacksList.clear();
        boissonsList.clear();
        autresList.clear();
    }

    /**
     * Donne la liste contenant le type d'item demandé
     * @param type Type de la liste demandée
     * @return La liste demandée
     */
    public static List<Item> getListFromTypeString(String type){
        return switch (type) {
            case "snack" -> snacksList;
            case "boisson" -> boissonsList;
            case "autre" -> autresList;
            default -> throw new IllegalArgumentException();
        };
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
}
