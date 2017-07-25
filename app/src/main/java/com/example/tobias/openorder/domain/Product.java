package com.example.tobias.openorder.domain;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by tobias on 18.07.17.
 */

public class Product implements Serializable {
    private String name;
    private double price;
    private String comment;
    private int count = 1;

    public Product(String name, double price) {
        this.name=name;
        this.price=price;
    }

    //Name
    public String getName() {
        return name;
    }
    public void setName(String name) {this.name = name;}

    //Price
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    //Count
    public int getCount() {return count;}
    public void setCount(int count) {this.count = count;}
    public void addProduct() {
        this.count++;
    }
    public void removeProduct() {
        this.count--;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
