package com.example.tobias.openorder.domain;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by tobias on 19.07.17.
 */

public class Table implements Serializable {

    private String tableNr;
    LinkedList<Product> bill = new LinkedList<>();

    public Table(String tableNr) {
        this.tableNr = tableNr;
    }

    public Table(String tableNr, LinkedList bill) {
        this.tableNr = tableNr;
        this.bill = bill;
    }

    //tableNr
    public String getTableNr() {return tableNr;}
    public void setTableNr(String tableNr) {this.tableNr = tableNr;}

    //bill
    public LinkedList<Product> getBill() {return bill;}
    public void setBill(LinkedList<Product> bill) {this.bill = bill;}

    //billprice
    public double billTotalPrice()
    {
        double billprice = 0.0;

        for(int i = 0; i < bill.size(); i++){
            double currentPrice = bill.get(i).getPrice();
            billprice += (currentPrice * bill.get(i).getCount());
        }

        return billprice;
    }

    public void addProduct(Product product) {
        Product foundProductInList = null;
        for(int i = 0; i < bill.size(); i++) {
            Product currentProduct = bill.get(i);
            if (currentProduct.getName() == product.getName()) {
                foundProductInList = currentProduct;
            }
        }
        if (foundProductInList == null) {
            bill.add(product);
        } else {
            foundProductInList.addProduct();
        }
    }

    public void removeProduct(Product product){
        Product foundProductInList = null;
        for(int i = 0; i < bill.size(); i++){
            Product currentProduct = bill.get(i);
            if(currentProduct.getName() == product.getName()){
                foundProductInList = currentProduct;
            }
        }
        if(foundProductInList.getCount() > 1){
            foundProductInList.setCount(foundProductInList.getCount()-1);
        }
        else{
            bill.remove(product);
        }
    }
}
