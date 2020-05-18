package com.example.vjezbanavdrawer.ui.PretragaNamirnica;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vjezbanavdrawer.Data.Namirnica;
import com.example.vjezbanavdrawer.R;

import java.util.List;

public class PretragaNamirnicaAdapter extends BaseAdapter {


private List<Namirnica> NamirnicaList;
    private Context context;
    public PretragaNamirnicaAdapter(Context context,List<Namirnica> namirnicaList){
        super();
        this.NamirnicaList=namirnicaList;
        this.context=context;

    }
    @Override
    public int getCount() {
        return NamirnicaList.size();
    }

    @Override
    public Object getItem(int position) {
        return NamirnicaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView= LayoutInflater.from(context).inflate(R.layout.row_pretraga_namirnica, parent, false);

        TextView namirnica=(TextView) rowView.findViewById(R.id.row_pretraga_namirnica_textView);
        TextView BrojKalorija= (TextView) rowView.findViewById(R.id.row_pretraga_kalorije_textView);


        namirnica.setText(NamirnicaList.get(position).getNaziv());
        BrojKalorija.setText(String.valueOf(NamirnicaList.get(position).getKalorije())+" kcal");
        return rowView;
    }
}
