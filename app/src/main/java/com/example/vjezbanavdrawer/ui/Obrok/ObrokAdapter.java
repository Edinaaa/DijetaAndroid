package com.example.vjezbanavdrawer.ui.Obrok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vjezbanavdrawer.Data.Obrok;
import com.example.vjezbanavdrawer.R;

import java.util.List;

public class ObrokAdapter extends BaseAdapter {


private List<Obrok> ObrokList;
    private Context context;
    public ObrokAdapter(Context context,List<Obrok> ObrokList){
        super();
        this.ObrokList=ObrokList;
        this.context=context;

    }
    @Override
    public int getCount() {
        return ObrokList.size();
    }

    @Override
    public Object getItem(int position) {
        return ObrokList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView= LayoutInflater.from(context).inflate(R.layout.row_obrok, parent, false);

        TextView Obrok=(TextView) rowView.findViewById(R.id.row_obrok_textView);
        TextView BrojKalorija= (TextView) rowView.findViewById(R.id.row_obrok_kalorije_textView);


        Obrok.setText( ObrokList.get(position).getNazivObroka());
        BrojKalorija.setText(String.format("%.2f", ObrokList.get(position).getBrojKalorija() )+" kcal");
        return rowView;
    }
}
