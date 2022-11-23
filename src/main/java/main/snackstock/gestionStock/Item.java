package main.snackstock.gestionStock;

public class Item {

    private final String NAME;
    private int quantity;
    private final String PRICE;

    public Item(String name, int quantity, String price) {
        NAME = name;
        this.quantity = quantity;
        PRICE = price;
    }

    public void addOneToQuantity(){
        this.quantity ++;
    }

    public int buy(int amount){
        this.quantity -= amount;
        return this.quantity; //TODO make sure to handle negative returned values
    }

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

}
