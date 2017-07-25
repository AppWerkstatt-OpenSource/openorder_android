package com.example.tobias.openorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tobias.openorder.domain.Product;
import com.example.tobias.openorder.domain.Table;
import com.example.tobias.openorder.helper.CurrencyHelper;
import com.example.tobias.openorder.helper.ProductAdapterInterface;
import com.example.tobias.openorder.helper.ProductListAdapter;
import com.example.tobias.openorder.helper.TableListAdapter;

public class BillActivity extends AppCompatActivity implements ProductAdapterInterface{

    Table table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        setTitle("Rechnung");

        Bundle extras = getIntent().getExtras();
        table = (Table) extras.getSerializable("Table");

        TextView txtBillTable = (TextView)findViewById(R.id.textview_tableBill);
        txtBillTable.setText("Tisch "+table.getTableNr());


        //Add tables to xml-file
        ListView listView = (ListView)findViewById(R.id.listview_bill);

        final ListAdapter listAdapter = new ProductListAdapter(getApplicationContext(), table, BillActivity.this);
        listView.setAdapter(listAdapter);

        getBillPrice();

    }

    @Override
    public void dialogHelper(Product product) {

    }

    public void getBillPrice(){
        TextView txtSumme = (TextView)findViewById(R.id.textview_billprice1);
        txtSumme.setText("" + CurrencyHelper.convertToEur(table.billTotalPrice()));
    }
}
