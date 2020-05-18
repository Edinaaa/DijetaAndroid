package com.example.vjezbanavdrawer.ui.Dijete;

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
import com.example.vjezbanavdrawer.Data.Dijeta;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.example.vjezbanavdrawer.ui.Dan.DaniFragment;
import com.example.vjezbanavdrawer.ui.NovaDijeta.NovaDijetaFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUri;
import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUriParametar;


public class DijeteFragment extends Fragment {

    private ListView lista;
    private String  test;
    private String username;
    private int Pozicija;
    private String pass;
    private List<Dijeta> dijetaList;
    private  View root;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.fragment_dijete, container, false);
         lista= (ListView)root.findViewById(R.id.dijete_listView);

        Pozicija=-1;
         //postavlja title od tool bara na dijeta
        MainActivity a= (MainActivity) getActivity();
       a.setNav(R.id.nav_dijete,"Dijete" );
        GetFromStaredPreferences();

       ReqestDijetaIndex();


        // za detalje ithema otavara se novi fragment
       lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                do_OnItemClick_DijetaFragment(position);

            }
        });

        //za brisanje ithema
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                do_OnItemLongClick_DijetaFragment(position);
                return false;
            }
        });
        FloatingActionButton fab =root.findViewById(R.id.fab_fragment_dijete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_OnClickFab_DijetaFragment();
            }
        });
        return root;
    }

    public void ReqestDijetaIndex(){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =GetUri("Dijeta","Index");

        final StringRequest srting= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray listDijeta = new JSONArray(response);

                            dijetaList=new ArrayList<>();
                            for (int i=0;i<listDijeta.length();i++){
                                JSONObject d=listDijeta.getJSONObject(i);
                                Dijeta dijeta=new Dijeta();
                                dijeta.DijetaId =d.getInt("dijetaId");
                                dijeta.Naziv= d.getString("naziv");
                                dijeta.BrojDana=d.getInt("brojDana");

                                dijeta.Kalorije=(float) d.getDouble("kalorije");
                                dijeta.KorisnikId=d.getInt("korisnikId");
                                dijetaList.add(dijeta);
                            }


                           PopuniPodatke();


                        }
                        catch (JSONException  error){
                           test=error.getMessage();
                        }

                    }
                }
                , new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError volleyError) {
                       ErrorHandle(volleyError);
                      }
                    }){
                        @Override
                        public Map<String, String> getHeaders()throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<String, String>();
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
    public void GetFromStaredPreferences(){


        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username=sharedPref.getString(getString(R.string.username_key)," ");
        pass=sharedPref.getString(getString(R.string.pass_key)," ");
    }
    private void PopuniPodatke(){


    DijetaAdapter myAdapter= new DijetaAdapter(getActivity(),dijetaList);
    lista.setAdapter(myAdapter);

    }

    public void ReqestDijetaRemove(){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =GetUriParametar("Dijeta","Remove","dijetaId",dijetaList.get(Pozicija).getDijetaId());

        final StringRequest srting= new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            dijetaList.remove(Pozicija);
                                PopuniPodatke();
                                Pozicija=-1;
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   test=String.valueOf(error.networkResponse.statusCode);
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
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
    private void do_OnClickFab_DijetaFragment() {
        MainActivity a= (MainActivity) getActivity();
        a.setNav(R.id.nav_dijete,"Nova dijeta" );

        FragmentTransaction ft= getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,new NovaDijetaFragment()).addToBackStack("my_fragment").commit();

    }

    private void do_OnItemClick_DijetaFragment(int position) {

            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment,new DaniFragment(dijetaList.get(position).getDijetaId())).addToBackStack("my_fragment").commit();

    }
    private void do_OnItemLongClick_DijetaFragment(int position) {

        Pozicija=position;
        if (Pozicija!=-1)
            ReqestDijetaRemove();

    }

}
