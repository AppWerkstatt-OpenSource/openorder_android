package com.example.tobias.openorder.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tobias on 20.07.17.
 */

public class TableGroup implements Serializable {
    private String tableGroup;
    private LinkedList<Table> tables = new LinkedList<>();
    private String id;

    public TableGroup(String tableGroup){
        this.tableGroup = tableGroup;
    }

    public TableGroup(String tableGroup, String id){
        this.tableGroup = tableGroup;
        this.id = id;
    }

    public TableGroup(String tableGroup, LinkedList tables){
        this.tableGroup = tableGroup;
        this.tables = tables;
    }

    //Tables
    public LinkedList<Table> getTables() {return tables;}
    public void setTables(LinkedList<Table> tables) {this.tables = tables;}

    //TableGroup
    public String getTableGroup() {return tableGroup;}
    public void setTableGroup(String tableGroup) {this.tableGroup = tableGroup;}

    public static int countTables(List<TableGroup> tables) {
        int tableCounter = 0;
        for (int i = 0; i < tables.size(); i++) {
            TableGroup currentTableGroup = tables.get(i);
            tableCounter += currentTableGroup.getTables().size();
        }
        return tableCounter;
    }

    //Id
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public static LinkedList<Table> getAllTables(List<TableGroup> tables) {
        LinkedList<Table> allTables = new LinkedList<>();
        for (int i = 0; i < tables.size(); i++) {
            TableGroup currentTableGroup = tables.get(i);
            for (int j = 0; j < currentTableGroup.getTables().size(); j++) {
                Table currentTable = currentTableGroup.getTables().get(j);
                allTables.add(currentTable);
            }
        }
        return allTables;
    }
}
