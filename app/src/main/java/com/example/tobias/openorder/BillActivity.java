package com.example.tobias.openorder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tobias.openorder.domain.Bill;
import com.example.tobias.openorder.domain.Product;
import com.example.tobias.openorder.domain.Table;
import com.example.tobias.openorder.helper.CurrencyHelper;
import com.example.tobias.openorder.helper.ProductAdapterInterface;
import com.example.tobias.openorder.helper.ProductListAdapter;
import com.example.tobias.openorder.helper.TableListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BillActivity extends AppCompatActivity implements ProductAdapterInterface{

    Table table;
    Bill bill;
    AlertDialog dialogBezahlen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        setTitle("Rechnung");

        Bundle extras = getIntent().getExtras();
        table = (Table) extras.getSerializable("Table");

        TextView txtBillTable = (TextView)findViewById(R.id.textview_tableBill);
        txtBillTable.setText(table.getTableNr());


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

    public void click_cash(View view){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        bill = new Bill(table.getBill(), time, table.getTableNr(), table.getId());  //Id ist derzeit TableId und muss später auf RechnungId umgeändert werden!
        addBillToBills();

        //Create Dialog
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.bill_dialog_helper, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView textView = v.findViewById(R.id.textview_summe);
        final TextView txtRueckgeld = v.findViewById(R.id.textview_rueckgeld);
        final EditText editText = v.findViewById(R.id.edittext_rueckgeld);
        Button buttonRueckgeld = v.findViewById(R.id.buttonRueckgeld);
        Button buttonBezahlen = v.findViewById(R.id.buttonBezahlen);


        textView.setText("" + CurrencyHelper.convertToEur(table.billTotalPrice()));


        buttonRueckgeld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText != null){
                    double erhalten = Double.valueOf(editText.getText().toString());
                    double rueckgeld = erhalten-(table.billTotalPrice());
                    txtRueckgeld.setText(CurrencyHelper.convertToEur(rueckgeld));
                }
            }
        });

        buttonBezahlen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeBillFromTable();
                table.setPaid(true);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        builder.setView(v);
        dialogBezahlen = builder.create();
        dialogBezahlen.show();
    }


    // Add bill to bills
    private void addBillToBills() {
        String url = "http://192.168.0.31:3000/api/Bills";

        JSONObject obj = new JSONObject();
        try {
            obj.put("dateTime", bill.getTime());
            obj.put("table", bill.getTableName());
            addProductsToBill();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Saved Bill in Bills
                            Toast.makeText(getApplicationContext(), "Rechnung wurde gespeichert", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Rechnung konnte nicht gespeichert werden", Toast.LENGTH_SHORT).show();
                }
        });
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // Add Products to bill
    int billSaveCounter = 0;
    private void addProductsToBill(){
        String url = "http://192.168.0.31:3000/api/Bills/"+bill.getBillId()+"/products";

        billSaveCounter = table.getBill().size();
        for(int i = 0; i < table.getBill().size(); i++){
            Product currentProduct = table.getBill().get(i);
            try {
                JSONObject obj = new JSONObject();
                obj.put("name", currentProduct.getName());
                obj.put("price", currentProduct.getPrice());
                obj.put("comment", currentProduct.getComment());
                obj.put("count", currentProduct.getCount());

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                billSaveCounter--;

                                if (billSaveCounter == 0) {
                                    // Saved all products.
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });
                Volley.newRequestQueue(this).add(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Remove bill from table
    int billRemoveCounter = 0;
    private void removeBillFromTable()
    {
        String url = "http://192.168.0.31:3000/api/Tables/" + table.getId() + "/bill";

        billRemoveCounter = table.getBill().size();

        for(int i = 0; i < table.getBill().size(); i++){
            JSONObject obj = new JSONObject();
            obj.remove("name");
            obj.remove("price");
            obj.remove("comment");
            obj.remove("count");


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            billRemoveCounter--;
                            if (billSaveCounter == 0) {
                                //Delete all Products
                                Toast.makeText(getApplicationContext(),"Rechnung wurde von Tisch entfernt",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Rechnung konnte nicht entfernt werden
                }
        });
                Volley.newRequestQueue(this).add(request);
        }
    }
}
