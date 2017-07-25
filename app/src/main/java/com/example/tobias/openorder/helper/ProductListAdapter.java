package com.example.tobias.openorder.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.tobias.openorder.R;
import com.example.tobias.openorder.domain.Product;
import com.example.tobias.openorder.domain.Table;

/**
 * Created by tobias on 19.07.17.
 */

public class ProductListAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    Table table;
    ProductAdapterInterface productAdapterInterface;

    public ProductListAdapter(Context context, Table table, ProductAdapterInterface productAdapterInterface) {
        this.context = context;
        this.table = table;
        this.productAdapterInterface = productAdapterInterface;
    }

    @Override
    public int getCount() {
        return table.getBill().size();
    }

    @Override
    public Object getItem(int position) {
        return table.getBill().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View itemView = view;

        final Product currentProduct = table.getBill().get(i);

        //falls nötig, viewGroup bauen
        if(itemView == null) {
            //Layout entfalten
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.item_product_list, null);
        }

        TextView count = itemView.findViewById(R.id.textview_count);
        TextView name = itemView.findViewById(R.id.textview_name);
        TextView price = itemView.findViewById(R.id.textview_price);
        TextView txtComment = itemView.findViewById(R.id.textview_comment);

        //Click Event
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productAdapterInterface.dialogHelper(currentProduct);
            }
        });

        count.setText(String.valueOf(currentProduct.getCount()));
        name.setText(currentProduct.getName());
        price.setText(CurrencyHelper.convertToEur(currentProduct.getPrice()));
        String comment = currentProduct.getComment();
        if (comment == null || comment.length() == 0) {
            txtComment.setVisibility(View.GONE);
        } else {
            txtComment.setVisibility(View.VISIBLE);
            txtComment.setText(comment);
        }

        return itemView;
    }
}
