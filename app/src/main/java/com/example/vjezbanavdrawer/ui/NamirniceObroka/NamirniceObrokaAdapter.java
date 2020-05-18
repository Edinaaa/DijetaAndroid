package com.example.vjezbanavdrawer.ui.NamirniceObroka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vjezbanavdrawer.Data.NamirniceObroka;
import com.example.vjezbanavdrawer.R;

import java.util.List;

public class NamirniceObrokaAdapter extends BaseAdapter {

    private List<NamirniceObroka> namirniceObrokaList;

    private Context context;
    public NamirniceObrokaAdapter(Context context, List<NamirniceObroka> namirniceObrokaList){
        super();
        this.namirniceObrokaList=namirniceObrokaList;
        this.context=context;

    }
    @Override
    public int getCount() {
        return namirniceObrokaList.size();
    }

    @Override
    public Object getItem(int position) {
        return namirniceObrokaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView= LayoutInflater.from(context).inflate(R.layout.row_namirnice_obroka, parent, false);

        TextView namirnica=(TextView) rowView.findViewById(R.id.row_namirnica_obroka_textView);
        TextView doza=(TextView) rowView.findViewById(R.id.row_namirnica_obroka_doza_textView);
        TextView kal=(TextView) rowView.findViewById(R.id.row_nsmirnica_obroka_kalorije_textView);


        namirnica.setText(namirniceObrokaList.get(position).getNamirnica().getNaziv());
        doza.setText(String.valueOf(namirniceObrokaList.get(position).getKolicina()) +" " + namirniceObrokaList.get(position).getNamirnica().getJM());
        kal.setText(String.format("%.2f", namirniceObrokaList.get(position).getKalorije() )+" kcal");

        return rowView;
    }
}
