package com.example.tobias.openorder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.tobias.openorder.domain.Product;
import com.example.tobias.openorder.domain.ProductCategory;
import com.example.tobias.openorder.domain.Table;
import com.example.tobias.openorder.helper.CurrencyHelper;
import com.example.tobias.openorder.helper.GridAdapter;
import com.example.tobias.openorder.helper.OrderAdapterInterface;
import com.example.tobias.openorder.helper.PixelHelper;
import com.example.tobias.openorder.helper.ProductAdapterInterface;
import com.example.tobias.openorder.helper.ProductCategoryImageView;
import com.example.tobias.openorder.helper.ProductListAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class OrderActivity extends AppCompatActivity implements OrderAdapterInterface, ProductAdapterInterface {
    ListView listView;
    Table table;

    GridView gridView;
    LinkedList<ProductCategory> productCategories = new LinkedList<>();
    AlertDialog editProductDialog = null;
    ProductCategory selecteCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Get view components
        listView = (ListView) findViewById(R.id.productsListView);
        gridView = (GridView) findViewById(R.id.productsGridView);

        setTitle("Bestellung");

        //get Table name
        Intent intent = getIntent();
        String tableID = intent.getStringExtra("TableID");
        loadTableFromServer(tableID);
    }

    //Load Table from Server
    private void loadTableFromServer(String tableID) {
        String url = "http://192.168.0.31:3000/api/Tables/" + tableID;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String tableId = response.getString("id");
                            String tableName = response.getString("name");
                            Boolean isPaid = response.getBoolean("isPaid");
                            table = new Table(tableName, tableId, isPaid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Add ProductCategories to xml-file
                        loadProductCatergorys();
                        getBill();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(req);
    }

    //Click Event ProductCategory
    @Override
    public void addToCart(Product product) {

        //Add product to Table
        table.addProduct(product);

        getBillPrice();

        refreshBillList();
    }

    private void refreshBillList() {
        final ListAdapter listAdapter = new ProductListAdapter(getApplicationContext(), table, OrderActivity.this);
        listView.setAdapter(listAdapter);
    }

    public void getBillPrice() {
        TextView txtBillprice = (TextView) findViewById(R.id.textview_billprice);
        txtBillprice.setText("" + CurrencyHelper.convertToEur(table.billTotalPrice()));
    }

    //Button Abbrechen
    public void click_Abbrechen(View view) {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(i);
    }

    //Button Bestellen
    public void click_Bestellen(View view) {
        addBillToServer();
        table.setPaid(false);
        Intent i = new Intent(getApplicationContext(), BillActivity.class);
        i.putExtra("Table", table);
        startActivity(i);
    }

    //Click Event ProductList
    @Override
    public void dialogHelper(final Product product) {

        final Product currentProduct = product;

        //Create Dialog
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.dialog_items, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ImageView button_minus = view.findViewById(R.id.imageview_minus);
        ImageView button_plus = view.findViewById(R.id.imageview_plus);
        final TextView textView = view.findViewById(R.id.textview_stück_dialog);
        final TextView textViewTitle = view.findViewById(R.id.textview_dialog_name);
        final EditText editText = view.findViewById(R.id.edit_text_dialog);

        textViewTitle.setText(currentProduct.getName());
        textView.setText(String.valueOf(currentProduct.getCount()));
        editText.setText(currentProduct.getComment()); //falls schon ein Kommentar vorhanden ist


        button_plus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                table.addProduct(product);
                textView.setText(String.valueOf(currentProduct.getCount()));
                refreshBillList();
                getBillPrice();
            }
        });

        button_minus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean productDeleted = table.removeProduct(product);
                textView.setText(String.valueOf(currentProduct.getCount()));
                refreshBillList();
                getBillPrice();

                if (productDeleted) {
                    editProductDialog.hide();
                }
            }
        });

        builder.setView(view);

        builder.setNegativeButton("ABBRECHEN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close Dialog
            }
        });
        builder.setPositiveButton("BESTÄTIGEN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String comment = String.valueOf(editText.getText());
                currentProduct.setComment(comment);
                refreshBillList();
            }
        });

        editProductDialog = builder.create();
        editProductDialog.show();
    }

    //Refresh ProductCategorys
    private void refreshProductCategorys() {
        LinearLayout categoryLayout = (LinearLayout) findViewById(R.id.categoryLayout);

        categoryLayout.removeAllViews();

        for (int i = 0; i < productCategories.size(); i++) {
            ProductCategory currentCategory = productCategories.get(i);

            final ProductCategoryImageView imageView = new ProductCategoryImageView(this);
            imageView.setProductCategory(currentCategory);
            //LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,PixelHelper.dpToPixel(70.0f, getApplicationContext()), 1.0f);
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(PixelHelper.dpToPixel(80, getApplicationContext()), PixelHelper.dpToPixel(70, getApplicationContext()));
            imageView.setLayoutParams(layout);
            imageView.setImageResource(R.mipmap.burger_icon);

            //OnClick event ProductCategories
            imageView.setOnClickListener(new TextView.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), imageView.getProductCategory().getName(), Toast.LENGTH_SHORT).show();
                    //Add Products to xml-file
                    refreshProducts(imageView);
                }
            });

            categoryLayout.addView(imageView);
        }
    }

    //Refresh Products
    private void refreshProducts(ProductCategoryImageView imageView) {
        selecteCategory = imageView.getProductCategory();
        updateProductCategory();
    }

    private void updateProductCategory() {
        if (selecteCategory != null) {
            GridAdapter adapter = new GridAdapter(OrderActivity.this, selecteCategory, OrderActivity.this);
            gridView.setAdapter(adapter);
        }
    }


    //Get ProductCategorys from Server
    private void loadProductCatergorys() {
        final AVLoadingIndicatorView loadingBar = (AVLoadingIndicatorView)findViewById(R.id.loadingBar);
        loadingBar.show();

        String url = "http://192.168.0.31:3000/api/ProductCategories";

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        productCategories.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject currentProductCategory = response.getJSONObject(i);
                                String tableGroupName = currentProductCategory.getString("name");
                                String productImage = currentProductCategory.getString("productImage");
                                String productCategoryId = currentProductCategory.getString("id");
                                ProductCategory currentProductCatergory = new ProductCategory(tableGroupName, R.mipmap.juice_icon, productCategoryId);  //Server bringt nur image name und nicht R.id.testPicture
                                productCategories.add(currentProductCatergory);
                                loadProducts(currentProductCatergory);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        loadingBar.hide();
                        // Refresh productCategorys
                        refreshProductCategorys();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(req);
    }


    // Get Products from server
    private void loadProducts(final ProductCategory productCategory) {
        final AVLoadingIndicatorView loadingBar = (AVLoadingIndicatorView)findViewById(R.id.loadingBar);
        loadingBar.show();
        String url = "http://192.168.0.31:3000/api/ProductCategories/" + productCategory.getId() + "/products";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject currentProduct = response.getJSONObject(i);
                                String productName = currentProduct.getString("name");
                                Double price = currentProduct.getDouble("price");
                                String comment = "";
                                if (currentProduct.has("comment")) {
                                    comment = currentProduct.getString("comment");
                                }
                                Integer count = currentProduct.getInt("count");

                                Product currentProductObj = new Product(productName, price, comment, count);
                                productCategory.getProducts().add(currentProductObj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        loadingBar.hide();
                        refreshProductCategorys();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    int billSaveCounter = 0;
    //Add Product to Bill
    private void addBillToServer() {
        String url = "http://192.168.0.31:3000/api/Tables/" + table.getId() + "/bill";

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

    //Get Bill from Server
    private void getBill(){
        String url = "http://192.168.0.31:3000/api/Tables/"+table.getId()+"/bill";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        table.getBill().clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject currentBill = response.getJSONObject(i);
                                String name = currentBill.getString("name");
                                Double price = currentBill.getDouble("price");
                                int count = currentBill.getInt("count");
                                String comment = currentBill.getString("comment");
                                Product currentProduct = new Product(name, price, comment, count);
                                //LinkedList<Product> bill = new LinkedList<>();
                                //bill.add(currentProduct);
                                table.getBill().add(currentProduct);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        refreshBillList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);
    }

}

