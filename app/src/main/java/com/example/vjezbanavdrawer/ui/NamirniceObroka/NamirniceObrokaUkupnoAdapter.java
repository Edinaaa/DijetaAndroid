package com.example.vjezbanavdrawer.ui.NamirniceObroka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vjezbanavdrawer.Data.UkupnoVM;
import com.example.vjezbanavdrawer.R;

import java.util.List;

public class NamirniceObrokaUkupnoAdapter extends BaseAdapter {
private List<UkupnoVM> UkupnoVMList;

    private Context context;
    public NamirniceObrokaUkupnoAdapter(Context context, List<UkupnoVM> ukupnoVMList){
        super();
        this.UkupnoVMList=ukupnoVMList;
        this.context=context;

    }
    @Override
    public int getCount() {
        return UkupnoVMList.size();
    }

    @Override
    public Object getItem(int position) {
        return UkupnoVMList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView= LayoutInflater.from(context).inflate(R.layout.row_ukupno_namirnice, parent, false);

        TextView ukupno=(TextView) rowView.findViewById(R.id.row_namirnice_obroka_ukupno_textView);
        TextView broj=(TextView) rowView.findViewById(R.id.row_namirnice_obroka_broj_textView);



        ukupno.setText(UkupnoVMList.get(position).getNaziv());
        String s;

       if (UkupnoVMList.get(position).getBroj()==0.0)
            s="";
        else{ if (position==1)
           s=String.format("%.2f", UkupnoVMList.get(position).getBroj())+" kcal";
       else
           s=String.format("%.2f", UkupnoVMList.get(position).getBroj())+" g";}
        broj.setText(s);

        return rowView;
    }
}
