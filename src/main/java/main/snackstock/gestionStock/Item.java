package main.snackstock.gestionStock;

public class Item {

    /**
     * Nom de l'item
     */
    private final String NAME;
    /**
     * Quantité en stock de l'item
     */
    private int quantity;
    /**
     * Prix de l'item
     */
    private final String PRICE;
    private final String TYPE;

    public Item(String name, int quantity, String price, String type) {
        this.NAME = name;
        this.quantity = quantity;
        this.PRICE = price;
        this.TYPE = type;
    }

    public Item(Item i){
        this.NAME = i.getNAME();
        this.quantity = i.getQuantity();
        this.PRICE = i.getPRICE();
        this.TYPE = i.getTYPE();
    }

    /**
     * Augmente la quantité en stock de l'item de 1
     */
    public void addOneToQuantity(){
        this.quantity ++;
    }

    /**
     * Retire une quantité en stock de l'item
     * @param amount Le montant à retirer du stock
     */
    public void removeFromQuantity(int amount){
        this.quantity -= amount;
    }

    /**
     * Modifie la quantité en stock de l'item
     * @param quantity La quantité à attribuer au stock de l'item
     */
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String getNAME(){
        return this.NAME;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public String getPRICE(){
        return this.PRICE;
    }

    public String getTYPE(){
        return this.TYPE;
    }

}
