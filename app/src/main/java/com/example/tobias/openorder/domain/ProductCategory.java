package com.example.tobias.openorder.domain;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by tobias on 18.07.17.
 */

public class ProductCategory implements Serializable {
    private String name;
    private int productImage;

    private LinkedList<Product> products = new LinkedList<>();

    public ProductCategory(String name, int productImage) {
        this.name = name;
        this.productImage = productImage;
    }

    //Name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Products
    public LinkedList<Product> getProducts() {
        return products;
    }
    public void setProducts(LinkedList<Product> products) {
        this.products = products;
    }

    //ProductImage
    public int getProductImage() {
        return productImage;
    }
    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

}
