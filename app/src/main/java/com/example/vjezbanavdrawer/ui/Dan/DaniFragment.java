package com.example.vjezbanavdrawer.ui.Dan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.vjezbanavdrawer.Data.Dan;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.example.vjezbanavdrawer.ui.Obrok.ObrokFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUriParametar;

public class DaniFragment extends Fragment {
     private int dijetaId;
     private List<Dan> danList;
     private ListView lista;
     private View root;
     private int Pozicija;
     private String username;
     private String pass;

    public DaniFragment(int dijetaid) {
        dijetaId=dijetaid;


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           // return super.onCreateView(inflater, container, savedInstanceState);

        root = inflater.inflate(R.layout.fragment_dan, container, false);
        MainActivity a=(MainActivity) getActivity();
        a.setNav(R.id.nav_dijete, "Dani");
        lista= (ListView)root.findViewById(R.id.dan_listView);
        Pozicija=-1;
        RequestDani();

        // za detalje ithema otavara se novi fragment

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            do_OnIthemClick_DaniFragment(position);

            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                do_OnIthemLongClick_DaniFragment(position);
                return false;
            }
        });


      FloatingActionButton fab =root.findViewById(R.id.fab_fragment_dan);
       fab.setOnClickListener(new View.OnClickListener() {
         @Override
          public void onClick(View view) {
             RequestAddDan();
             RequestDani();
           }
       });
        return root;
    }

    private void do_OnIthemLongClick_DaniFragment(int position) {

        Pozicija=position;
        if (Pozicija!=-1)
        ReqestDanRemove();
    }

    private void PopuniPodatke(){



        DanAdapter daniAdapter= new DanAdapter(getActivity(),danList);

        lista.setAdapter(daniAdapter);
    }
    private void RequestAddDan(){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =GetUriParametar("Dan", "Add","Dijeta", dijetaId);

        final StringRequest srting= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                                JSONObject d=new JSONObject(response);
                                Dan dan=new Dan();
                                dan.setDanId(d.getInt("danId"));
                                dan.setBrojKalorija((float) d.getDouble("brojKalorija"));
                                dan.setDijetaId(d.getInt("dijetaId"));
                                danList.add(dan);
                            PopuniPodatke();
                        }

                        catch (JSONException error){
                            // test=error.getMessage();
                            }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandle(error);

            }
        }){
            @Override
            public Map<String, String> getHeaders()throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

               GetFromStaredPreferences();
                String creds = String.format("%s:%s",username, pass);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        queue.add(srting);






    }
    public void ReqestDanRemove(){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =GetUriParametar("Dan","Remove","danId",danList.get(Pozicija).getDanId());

        final StringRequest srting= new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Pozicija=-1;
                        RequestDani();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandle(error);

            }
        }){
            @Override
            public Map<String, String> getHeaders()throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                String creds = String.format("%s:%s",username,pass);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        queue.add(srting);


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
        for ( int i=0;i<5;i++){
            Toast.makeText(getActivity(), message,
                    Toast.LENGTH_SHORT).show();
        }

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
    public void GetFromStaredPreferences( ){


        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username=sharedPref.getString(getString(R.string.username_key),"nesto");
        pass=sharedPref.getString(getString(R.string.pass_key),"nesto pass");
    }
    private void RequestDani(){

    RequestQueue queue = Volley.newRequestQueue(getActivity());
    String url =GetUriParametar("Dan", "Index","Dijeta", dijetaId);

    final StringRequest srting= new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray listDan = new JSONArray(response);

                        danList=new ArrayList<>();
                        for (int i=0;i<listDan.length();i++){
                            JSONObject d=listDan.getJSONObject(i);
                            Dan dan=new Dan();
                            dan.setDanId(d.getInt("danId"));
                            dan.setBrojKalorija((float) d.getDouble("brojKalorija"));
                            dan.setDijetaId(d.getInt("dijetaId"));
                            danList.add(dan);
                        }


                       PopuniPodatke();


                    }
                    catch (JSONException error){
                       // test=error.getMessage();
                    }

                }
            }
            , new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            ErrorHandle(error);

        }
    }){


        @Override
        public Map<String, String> getHeaders()throws AuthFailureError {
            HashMap<String, String> params = new HashMap<String, String>();

           GetFromStaredPreferences();

            String creds = String.format("%s:%s",username, pass);
            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
            params.put("Authorization", auth);

            return params;
        }
    };
    queue.add(srting);






}
    private void do_OnIthemClick_DaniFragment(int index) {

            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment , new ObrokFragment(danList.get(index).getDanId())).addToBackStack("my_fragment").commit();


    }
}
