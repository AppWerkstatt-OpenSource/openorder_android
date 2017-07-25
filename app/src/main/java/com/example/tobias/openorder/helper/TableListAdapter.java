package com.example.tobias.openorder.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tobias.openorder.OrderActivity;
import com.example.tobias.openorder.R;
import com.example.tobias.openorder.TableActivity;
import com.example.tobias.openorder.domain.Table;
import com.example.tobias.openorder.domain.TableGroup;

import java.util.LinkedList;

/**
 * Created by tobias on 21.07.17.
 */

public class TableListAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    LinkedList<TableGroup>tableGroups;
    LinkedList<Table> allTables;
    TableSelectorInterface tableAdapterInterface;

    public TableListAdapter(Context context, LinkedList tableGroups, TableSelectorInterface tableAdapterInterface){
        this.context = context;
        this.tableGroups = tableGroups;
        this.allTables = TableGroup.getAllTables(tableGroups);
        this.tableAdapterInterface = tableAdapterInterface;
    }

    @Override
    public int getCount() {
        return allTables.size();
    }

    @Override
    public Object getItem(int i) {
        return allTables.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;

        final Table currentTable = allTables.get(i);

        //falls nötig, viewGroup bauen
        if(itemView == null){
            //Layout entfalten
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.item_table_list, null);
        }


        TextView txtTableList = itemView.findViewById(R.id.textview_tablelist);

        //Click event
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableAdapterInterface.tableSelected(currentTable);
            }
        });

        //Wenn noch nicht bezahlt wurde soll ListItem auf rot gesetzt werden
        if(currentTable.billTotalPrice() > 1){
            itemView.setBackgroundColor(Color.RED);
        }

        txtTableList.setText(""+ currentTable.getTableNr());
        return itemView;
    }
}
