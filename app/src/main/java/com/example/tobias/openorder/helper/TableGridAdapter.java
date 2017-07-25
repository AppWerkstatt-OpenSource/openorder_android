package com.example.tobias.openorder.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tobias.openorder.R;
import com.example.tobias.openorder.domain.TableGroup;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;

import java.util.LinkedList;

/**
 * Created by tobias on 20.07.17.
 */

public class TableGridAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    LinkedList<TableGroup> tablegroups;
    TableAdapterInterface tableAdapterInterface;

    public TableGridAdapter(Context context, LinkedList<TableGroup> tablegroups){
        this.context = context;
        this.tablegroups = tablegroups;
    }

    @Override
    public int getCountForHeader(int i) {
        return tablegroups.get(i).getTableGroup().length();
    }

    @Override
    public int getNumHeaders() {
        return tablegroups.size();
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {

        View headerview = view;

        if (headerview == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            headerview = inflater.inflate(R.layout.item_table_gridadapter, null);
        }

        TextView txtheader = headerview.findViewById(R.id.textview_table);
        txtheader.setText(""+tablegroups.get(i).getTableGroup());

        return headerview;
    }

    @Override
    public int getCount() {
        return tablegroups.size();
    }

    @Override
    public Object getItem(int position) {
        return tablegroups.get(position).getTables();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        View itemview = view;
        Log.d("DEBUG", String.valueOf(i));

        if (itemview == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemview = inflater.inflate(R.layout.item_table_gridadapter, null);
        }


        ImageView imageView = itemview.findViewById(R.id.imageview_table);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(view.toString());
                //tableAdapterInterface.addToCart();
            }
        });

        imageView.setImageResource(R.mipmap.table_icon);

        return itemview;
    }
}
