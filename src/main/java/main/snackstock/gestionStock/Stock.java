package main.snackstock.gestionStock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Stock {

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

    /**
     * Mot de passe administrateur
     */
    private static final String mdp = "mdp";

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
        getListFromTypeString(item.getTYPE()).add(item);
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
     * @param qty La quantité à enlever au stock
     */
    public static void removeQuantityFromItem(Item item, int qty){
        for(Item i : getListFromTypeString(item.getTYPE())){
            if(i.getNAME().equals(item.getNAME())){
                i.removeFromQuantity(qty);
            }
        }
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

    public static String getMdp(){
        return mdp;
    }
}
