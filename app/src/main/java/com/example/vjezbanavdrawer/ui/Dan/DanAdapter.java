package com.example.vjezbanavdrawer.ui.Dan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vjezbanavdrawer.Data.Dan;
import com.example.vjezbanavdrawer.R;

import java.util.List;

public class DanAdapter extends BaseAdapter {


private List<Dan> danList;
    private Context context;


    public DanAdapter(Context context,List<Dan> danList){
        super();
        this.danList=danList;
        this.context=context;

    }
    @Override
    public int getCount() {
        return danList.size();
    }

    @Override
    public Object getItem(int position) {
        return danList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView= LayoutInflater.from(context).inflate(R.layout.row_dan, parent, false);

        TextView rbDana=(TextView) rowView.findViewById(R.id.row_dan_textView);
        TextView BrojKalorija= (TextView) rowView.findViewById(R.id.row_dan_kalorije_textView);


        rbDana.setText((position+1)+". Dan");
        BrojKalorija.setText(String.format("%.2f", danList.get(position).getBrojKalorija())+" kcal");
        return rowView;
    }
}
