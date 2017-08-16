package com.example.tobias.openorder.domain;

import java.text.DateFormat;
import java.util.LinkedList;
import java.util.zip.DataFormatException;

/**
 * Created by tobias on 31.07.17.
 */

public class Bill {
    LinkedList<Product>bill = new LinkedList<>();
    String time;
    String tableName;
    String billId;

    public Bill(LinkedList bill, String time, String tableName, String billId){
        this.bill = bill;
        this.time = time;
        this.tableName = tableName;
        this.billId = billId;
    }

    // Bill
    public LinkedList<Product> getBill() {return bill;}
    public void setBill(LinkedList<Product> bill) {this.bill = bill;}

    // Date & Time
    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

    // Table
    public String getTableName() {return tableName;}
    public void setTableName(String tableName) {this.tableName = tableName;}

    // BillId
    public String getBillId() {return billId;}
    public void setBillId(String billId) {this.billId = billId;}
}
