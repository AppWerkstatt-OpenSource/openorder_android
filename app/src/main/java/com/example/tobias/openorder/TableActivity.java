package com.example.tobias.openorder;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tobias.openorder.domain.Product;
import com.example.tobias.openorder.domain.Table;
import com.example.tobias.openorder.domain.TableGroup;
import com.example.tobias.openorder.helper.TableAdapterInterface;
import com.example.tobias.openorder.helper.TableGridAdapter;
import com.example.tobias.openorder.helper.TableListAdapter;
import com.example.tobias.openorder.helper.TableSelectorInterface;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class TableActivity extends AppCompatActivity implements TableSelectorInterface{

    LinkedList<TableGroup> tableGroups = new LinkedList<>();

    ListView listView;

    boolean bestellen = false;
    boolean bezahlen = true;

    TableGroup newTableGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setTitle("Tische");

        //Add tables to xml-file
        listView = (ListView)findViewById(R.id.table_list);

        //Get extras
        Bundle extras = getIntent().getExtras();
        bestellen = extras.getBoolean("Bestellen");
        bezahlen = extras.getBoolean("Bezahlen");

        //Get tableGroups from Server
        final String URL = "http://192.168.0.31:3000/api/TableGroups";

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        tableGroups.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject currentTableGroup = response.getJSONObject(i);
                                String tableGroupName = currentTableGroup.getString("name");
                                String tableGroupId = currentTableGroup.getString("id");

                                loadTable(tableGroupId, i);
                                newTableGroup = new TableGroup(tableGroupName, tableGroupId);
                                tableGroups.add(newTableGroup);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // Reload table
                        //reloadTable();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    //Get Tables from Server --> Bestellen
    private void loadTable(final String tableGroupId, final int tableGroupPos) {
        final AVLoadingIndicatorView loadingBar = (AVLoadingIndicatorView)findViewById(R.id.progressBar);
        loadingBar.show();

        String url = "";
        if (bestellen) {
            url = "http://192.168.0.31:3000/api/TableGroups/" + tableGroupId + "/tables";
        } else {
            url = "http://192.168.0.31:3000/api/TableGroups/" + tableGroupId + "/tables?filter[where][isPaid]=false";
        }
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject currentTable = response.getJSONObject(i);
                                String tableName = currentTable.getString("name");
                                String tableId = currentTable.getString("id");
                                tableGroups.get(tableGroupPos).getTables().add(new Table(tableName,tableId));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        loadingBar.hide();
                        reloadTable();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(req);
    }


    private void reloadTable() {
        //Add tablegroups to TableListAdapter
        final TableListAdapter adapter = new TableListAdapter(getApplicationContext(), tableGroups, this);
        listView.setAdapter(adapter);
    }


    @Override
    public void tableSelected(Table table) {
        Intent i = new Intent(this,OrderActivity.class);
        i.putExtra("TableID", table.getId());
        startActivity(i);
    }
}
