package com.example.tobias.openorder.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.tobias.openorder.OrderActivity;
import com.example.tobias.openorder.R;
import com.example.tobias.openorder.domain.Product;
import com.example.tobias.openorder.domain.ProductCategory;

/**
 * Created by tobias on 19.07.17.
 */

public class GridAdapter extends BaseAdapter {

    ProductCategory products;
    private Context context;
    private LayoutInflater inflator;
    private OrderAdapterInterface orderAdapterInterface;

    public GridAdapter(Context context, ProductCategory products, OrderAdapterInterface orderAdapterInterface) {
        this.context = context;
        this.products = products;
        this.orderAdapterInterface = orderAdapterInterface;
    }

    @Override
    public int getCount() {
        return products.getProducts().size();
    }

    @Override
    public Object getItem(int position) {
        return products.getProducts().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;

        final Product currentProduct = products.getProducts().get(i);

        //falls nötig, viewGroup bauen
        if(itemView == null){
            //Layout entfalten
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflator.inflate(R.layout.product_item, null);
        }

        Button button = itemView.findViewById(R.id.product_buttons);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderAdapterInterface.addToCart(currentProduct);
            }
        });
        button.setText(currentProduct.getName());
        
        return itemView;
    }
}
