package com.example.tobias.openorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import com.example.tobias.openorder.domain.Product;
import com.example.tobias.openorder.domain.Table;
import com.example.tobias.openorder.domain.TableGroup;
import com.example.tobias.openorder.helper.TableAdapterInterface;
import com.example.tobias.openorder.helper.TableGridAdapter;
import com.example.tobias.openorder.helper.TableListAdapter;
import com.example.tobias.openorder.helper.TableSelectorInterface;

import java.util.LinkedList;

public class TableActivity extends AppCompatActivity implements TableSelectorInterface{

    LinkedList<TableGroup> tableGroups = new LinkedList<>();
    ListView listView;
    boolean activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setTitle("Tische");

        Bundle extras = getIntent().getExtras();
        activity = extras.getBoolean("Bestellen");
        activity = extras.getBoolean("Bezahlen");

        //Add test tablegroups
        TableGroup tableGroup1 = new TableGroup("SAAL");
        tableGroups.add(tableGroup1);
        TableGroup tableGroup2 = new TableGroup("GASTGARTEN");
        tableGroups.add(tableGroup2);
        
        //Add test tables
        Table table1 = new Table("Tisch 1");
        tableGroup1.getTables().add(table1);
        Table table2 = new Table("Tisch 2");
        tableGroup1.getTables().add(table2);
        Table table3 = new Table("Tisch 3");
        tableGroup1.getTables().add(table3);
        Table table4 = new Table("Tisch 4");
        tableGroup1.getTables().add(table4);
        Table table5 = new Table("Tisch 5");
        tableGroup1.getTables().add(table5);
        Table table9 = new Table("Tisch 6");
        tableGroup1.getTables().add(table9);
        Table table10 = new Table("Tisch 7");
        tableGroup1.getTables().add(table10);
        Table table11 = new Table("Tisch 8");
        tableGroup1.getTables().add(table11);

        Table table6 = new Table("Tisch 9");
        tableGroup2.getTables().add(table6);
        Table table7 = new Table("Tisch 10");
        tableGroup2.getTables().add(table7);
        Table table8 = new Table("Tisch 11");
        tableGroup2.getTables().add(table8);

        //Bestellen
        //Add tables to xml-file
        listView = (ListView)findViewById(R.id.table_list);

        final TableListAdapter adapter = new TableListAdapter(getApplicationContext(),tableGroups, this);
        listView.setAdapter(adapter);

        /*
        //Bezahlen
        if(activity == false){
            //Add tables to xml-file
            listView = (ListView)findViewById(R.id.table_list);

            for(int i = 0; i < tableGroups.size(); i++){
                for(int j = 0; j < tableGroups.get(i).getTables().size(); i++){
                   billTotalPrice = tableGroups.get(i).getTables().get(j).billTotalPrice();
                    if(billTotalPrice > 0){
                        billToPay.add(tableGroups.get(i));
                    }
                }
            }
        }
        */

        /*
        //Add tables to xml-file
        gridView = (GridView)findViewById(R.id.table_GridView);

        final TableGridAdapter adapter = new TableGridAdapter(getApplicationContext(), tableGroups);
        gridView.setAdapter(adapter);
        */
    }

    @Override
    public void tableSelected(Table table) {
        Intent i = new Intent(this,OrderActivity.class);
        //i.putExtra("TableNr",table.getTableNr());
        startActivity(i);
    }

}
