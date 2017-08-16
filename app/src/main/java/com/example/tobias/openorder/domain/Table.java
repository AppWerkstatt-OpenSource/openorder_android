package com.example.tobias.openorder.domain;

import android.support.v7.app.AlertDialog;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by tobias on 19.07.17.
 */

public class Table implements Serializable {

    private String tableNr;
    private String id;
    private Boolean isPaid;
    LinkedList<Product> bill = new LinkedList<>();

    public Table(String tableNr, String id) {
        this.tableNr = tableNr;
        this.id = id;
    }

    public Table(String tableNr, String id, Boolean isPaid){
        this.tableNr = tableNr;
        this.id = id;
        this.isPaid = isPaid;
    }

    public Table(String tableNr, LinkedList bill, String id) {
        this.tableNr = tableNr;
        this.bill = bill;
        this.id = id;
    }

    //tableNr
    public String getTableNr() {return tableNr;}
    public void setTableNr(String tableNr) {this.tableNr = tableNr;}

    //id
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    //bill
    public LinkedList<Product> getBill() {return bill;}
    public void setBill(LinkedList<Product> bill) {this.bill = bill;}

    //isPaid
    public Boolean getPaid() {return isPaid;}
    public void setPaid(Boolean paid) {isPaid = paid;}

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

    public boolean removeProduct(Product product) {
        Product foundProductInList = null;
        for(int i = 0; i < bill.size(); i++) {
            Product currentProduct = bill.get(i);
            if(currentProduct.getName() == product.getName()){
                foundProductInList = currentProduct;
            }
        }
        if(foundProductInList.getCount() > 1) {
            foundProductInList.setCount(foundProductInList.getCount()-1);
        } else {
            bill.remove(product);
            return true;
        }
        return false;
    }
}
