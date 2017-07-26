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
import java.util.LinkedList;

public class OrderActivity extends AppCompatActivity implements OrderAdapterInterface, ProductAdapterInterface{

    ListView listView;
    Table table;

    GridView gridView;
    LinkedList<ProductCategory> productCategories = new LinkedList<>();
    AlertDialog editProductDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setTitle("Bestellung");

        //get Table name
        //Bundle extras = getIntent().getExtras();
        //table.setTableNr(extras.getString("TableNr"));

        gridView = (GridView) findViewById(R.id.productsGridView);

        // Table
        table = new Table("12");

        // Add Demo ProductCategories
        ProductCategory getränke = new ProductCategory("Getränke", R.mipmap.juice_icon);
        productCategories.add(getränke);
        ProductCategory vorspeisen = new ProductCategory("Vorspeisen", R.mipmap.salat_icon);
        productCategories.add(vorspeisen);
        ProductCategory hauptspeisen = new ProductCategory("Hauptspeisen", R.mipmap.burger_icon);
        productCategories.add(hauptspeisen);
        ProductCategory nachspeisen = new ProductCategory("Nachspeisen", R.mipmap.ice_icon);
        productCategories.add(nachspeisen);
        ProductCategory coffee = new ProductCategory("Coffee", R.mipmap.coffee_icon2);
        productCategories.add(coffee);

        //Add Demo Products
        Product cola = new Product("Cola",2.50);
        getränke.getProducts().add(cola);
        Product sprite = new Product("Sprite",2.50);
        getränke.getProducts().add(sprite);

        Product salat = new Product("Salat",4.90);
        vorspeisen.getProducts().add(salat);
        Product suppe = new Product("Suppe",4.80);
        vorspeisen.getProducts().add(suppe);

        Product hamburger = new Product("Hamburger",9.80);
        hauptspeisen.getProducts().add(hamburger);
        Product schnitzel = new Product("Schnitzel",11.50);
        hauptspeisen.getProducts().add(schnitzel);
        Product toast = new Product("Toast",11.20);
        hauptspeisen.getProducts().add(toast);
        Product steak = new Product("Toast", 7.60);
        hauptspeisen.getProducts().add(steak);
        Product pizza = new Product("Pizza", 8.50);
        hauptspeisen.getProducts().add(pizza);
        Product nudeln = new Product("Nudeln",13.20);
        hauptspeisen.getProducts().add(nudeln);
        Product huhn = new Product("Huhn", 3.40);
        hauptspeisen.getProducts().add(huhn);
        Product spaghetti = new Product("Spaghetti", 7.90);
        hauptspeisen.getProducts().add(spaghetti);
        Product zackzack = new Product("ZackZack", 3.50);
        hauptspeisen.getProducts().add(zackzack);
        Product wurst = new Product("Wurst", 3.90);
        hauptspeisen.getProducts().add(wurst);
        Product burger = new Product("Burger", 6.80);
        hauptspeisen.getProducts().add(burger);
        Product test = new Product("Fleisch",5);
        hauptspeisen.getProducts().add(test);
        Product test2 = new Product("Fisch",5);
        hauptspeisen.getProducts().add(test2);
        Product test3 = new Product("Dorsch",5);
        hauptspeisen.getProducts().add(test3);
        Product test4 = new Product("Curry",5);
        hauptspeisen.getProducts().add(test4);
        Product test5 = new Product("Veggi",5);
        hauptspeisen.getProducts().add(test5);
        Product test6 = new Product("Brot",5);
        hauptspeisen.getProducts().add(test6);
        Product test7 = new Product("Sonstiges",5);
        hauptspeisen.getProducts().add(test7);
        Product test8 = new Product("Tomaten",5);
        hauptspeisen.getProducts().add(test8);
        Product test9 = new Product("LOL",5);
        hauptspeisen.getProducts().add(test9);

        Product eis = new Product("Eis",5.20);
        nachspeisen.getProducts().add(eis);
        Product kuchen = new Product("Kuchen",3.20);
        nachspeisen.getProducts().add(kuchen);

        Product kaffee = new Product("Kaffee",2.50);
        coffee.getProducts().add(kaffee);
        Product tee = new Product("Tee", 2.50);
        coffee.getProducts().add(tee);


        //Add ProductCategories to xml-file
        LinearLayout categoryLayout = (LinearLayout) findViewById(R.id.categoryLayout);

        for (int i = 0; i < productCategories.size(); i++) {
            ProductCategory currentCategory = productCategories.get(i);

            final ProductCategoryImageView imageView = new ProductCategoryImageView(this);
            imageView.setProductCategory(currentCategory);
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,PixelHelper.dpToPixel(70.0f, getApplicationContext()), 1.0f);
            imageView.setLayoutParams(layout);
            imageView.setImageResource(currentCategory.getProductImage());

            //OnClick event ProductCategories
            imageView.setOnClickListener(new TextView.OnClickListener(){
                public void onClick(View v){
                    Toast.makeText(getApplicationContext(),imageView.getProductCategory().getName(),Toast.LENGTH_SHORT).show();

                    //Add Products to xml-file
                    final GridAdapter adapter = new GridAdapter(OrderActivity.this, imageView.getProductCategory(), OrderActivity.this);
                    gridView.setAdapter(adapter);
                }
            });

            categoryLayout.addView(imageView);
        }
    }

    //Click Event ProductCategory
    @Override
    public void addToCart(Product product) {

        //Add product to Table
        table.addProduct(product);


        //Add to list after Click event
        listView = (ListView)findViewById(R.id.productsListView);

        getBillPrice();

        refreshBillList();
    }

    private void refreshBillList() {
        final ListAdapter listAdapter = new ProductListAdapter(getApplicationContext(), table, OrderActivity.this);
        listView.setAdapter(listAdapter);
    }

    public void getBillPrice(){
        TextView txtBillprice = (TextView)findViewById(R.id.textview_billprice);
        txtBillprice.setText("" + CurrencyHelper.convertToEur(table.billTotalPrice()));
    }

    //Button Abbrechen
    public void click_Abbrechen(View view){
        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(i);
    }

    //Button Bestellen
    public void click_Bestellen(View view){
        Intent i = new Intent(getApplicationContext(),BillActivity.class);
        i.putExtra("Table", table);
        startActivity(i);
    }

    //Click Event ProductList
    @Override
    public void dialogHelper(final Product product) {

        final Product currentProduct = product;

        //Create Dialog
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
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


        button_plus.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                table.addProduct(product);
                textView.setText(String.valueOf(currentProduct.getCount()));
                refreshBillList();
                getBillPrice();
            }
        });

        button_minus.setOnClickListener(new Button.OnClickListener(){
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
}
