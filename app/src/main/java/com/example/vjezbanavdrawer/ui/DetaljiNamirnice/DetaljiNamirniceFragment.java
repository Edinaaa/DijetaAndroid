package com.example.vjezbanavdrawer.ui.DetaljiNamirnice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vjezbanavdrawer.Data.Namirnica;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUriParametar;

public class DetaljiNamirniceFragment extends Fragment {

    private int NamirnicaId;
    private View root;
    private String username;
    private String pass;
    private Namirnica Namirnica;
    public DetaljiNamirniceFragment(int namirnicaId) {
        this.NamirnicaId=namirnicaId;
    }
    public static DetaljiNamirniceFragment newInstance(int namirnicaId){ return new DetaljiNamirniceFragment(namirnicaId);}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  return super.onCreateView(inflater, container, savedInstanceState);
        MainActivity a=(MainActivity)getActivity();
        a.setNav(R.id.nav_dijete,"Detalji namirnice");
        root = inflater.inflate(R.layout.fragment_detalji_namirnice, container, false);
        RequestNamirnica();


        return root;
    }
    private void RequestNamirnica(){
        String url=GetUriParametar("Namirnica","Index","namirnica",NamirnicaId);
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        final StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                        JSONObject namirnica=new JSONObject(response);

                         Namirnica=new Namirnica();
                    Namirnica.setNaziv(namirnica.getString("naziv"));
                    Namirnica.setJM(namirnica.getString("jm"));
                    Namirnica.setKalorije((float)namirnica.getInt("kalorije"));
                    Namirnica.setKolicina(namirnica.getInt("kolicina"));
                    Namirnica.setMasti(namirnica.getInt("masti"));
                    Namirnica.setNamirnicaId(namirnica.getInt("namirnicaId"));
                    Namirnica.setProteini(namirnica.getInt("proteini"));
                    Namirnica.setUgljikohidrati(namirnica.getInt("ugljikohidrati"));
                    PopuniPodatke();
                    } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                PopuniPodatke();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandle(error);

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
             GetFromStaredPreferences();
                String creds = String.format("%s:%s",username,pass);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

        };
        queue.add(stringRequest);
    }
    public void GetFromStaredPreferences(){


        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username=sharedPref.getString(getString(R.string.username_key),"nesto");
        pass=sharedPref.getString(getString(R.string.pass_key),"nesto pass");
    }
    public  void ErrorHandle(VolleyError volleyError){
        NetworkResponse networkResponse = volleyError.networkResponse;

        String poruka = new String(networkResponse.data);
        String message="";
        if (!poruka.equals(""))
            message+=poruka;

        if (volleyError instanceof NetworkError || volleyError instanceof AuthFailureError || volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError) {
            message += "\n Nemate konekcije na internet.";
        } else if (volleyError instanceof ServerError) {
            message += "\n Server nije pronađen, pokušajte kasnije.";
        }
        Toast.makeText(getActivity(), message,
                Toast.LENGTH_SHORT).show();
        if (message.contains("Nemate pravo pristupa!"))
        {   ObrisiSharedPreferences();
            MainActivity a= (MainActivity) getActivity();
            startActivity(new Intent(a, Login.class));}



    }
    public void ObrisiSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.username_key)," ");
        editor.putString(getString(R.string.pass_key)," ");

        editor.commit();

    }
    private void PopuniPodatke() {

        TextView   naziv=root.findViewById(R.id.detalji_namirnica_naziv_textview);
        TextView    brojKalorija=root.findViewById(R.id.detalji_namirnica_brojKalorija_textview);
        TextView   masti=root.findViewById(R.id.detalji_namirnica_masti_textview);
        TextView   proteini=root.findViewById(R.id.detalji_namirnica_proteini_textview);
        TextView  ugljikohidrati=root.findViewById(R.id.detalji_namirnica_ugljikohidrati_textview);


        naziv.setText(Namirnica.getNaziv());
        String s=Namirnica.getKalorije() +" kcal";
        brojKalorija.setText(s);
        s=Namirnica.getMasti() +" g";
        masti.setText(s);
        s= Namirnica.getProteini()+" g";
        proteini.setText(s);
        s= Namirnica.getUgljikohidrati()+" g";
        ugljikohidrati.setText(s);

    }

}
