package com.example.vjezbanavdrawer.ui.Dijete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vjezbanavdrawer.Data.Dijeta;
import com.example.vjezbanavdrawer.R;

import java.util.List;

public class DijetaAdapter extends BaseAdapter {


    private List<Dijeta> DijetaList;
    private Context context;

    public DijetaAdapter( Context context, List<Dijeta> dijetaList){
        super();
     this.DijetaList=dijetaList;
        this.context=context;

    }
    @Override
    public int getCount() {
        return DijetaList.size();
    }

    @Override
    public Object getItem(int position) {
        return DijetaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView= LayoutInflater.from(context).inflate(R.layout.row_dijete, parent, false);

        TextView NazivDijete=(TextView) rowView.findViewById(R.id.row_dijete_naziv_textView);
        TextView Brojdana= (TextView) rowView.findViewById(R.id.row_dijete_rbDana_textView);

        TextView BrojKalorija= (TextView) rowView.findViewById(R.id.row_dijete_kalorije_textView);


        NazivDijete.setText(DijetaList.get(position).getNaziv());
        Brojdana.setText(String.valueOf(DijetaList.get(position).getBrojDana()));
        BrojKalorija.setText(String.format("%.2f", DijetaList.get(position).getKalorije())+" kcal");

        return rowView;
    }
}

