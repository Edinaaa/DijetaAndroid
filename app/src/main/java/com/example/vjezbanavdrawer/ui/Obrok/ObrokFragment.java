package com.example.vjezbanavdrawer.ui.Obrok;

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
import com.example.vjezbanavdrawer.Data.Obrok;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.example.vjezbanavdrawer.ui.NamirniceObroka.NamirniceObrokaFragment;
import com.example.vjezbanavdrawer.ui.NoviObrok.NoviObrokFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUriParametar;

public class ObrokFragment extends Fragment {

    private int DanId;
    private String username;
    private String pass;
    private int Pozicija;
    private List<Obrok> obrokList;
    private  ListView lista;
    private  View root;
    public ObrokFragment(int danId) {
        DanId=danId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //  return super.onCreateView(inflater, container, savedInstanceState);
        MainActivity a= (MainActivity) getActivity();
        a.setNav(R.id.nav_dijete,"Obroci");
         root = inflater.inflate(R.layout.fragment_obrok, container, false);

         lista= (ListView) root.findViewById(R.id.obrok_listView);
        Pozicija=-1;
        RequestObroci();

        // za detalje ithema otavara se novi fragment
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                do_OnItemClick_ObrokFragment(position);

            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                do_OnItemLongClick_ObrokFragment(position);
                return true;
            }
        });
        FloatingActionButton fab =root.findViewById(R.id.fab_fragment_obrok);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_OnClickFab_ObrokFragment();
            }
        });


        return root;
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
    private void RequestObroci(){
    RequestQueue queue = Volley.newRequestQueue(getActivity());
    String url =GetUriParametar("Obrok", "Index", "DanId", DanId );

    final StringRequest srting= new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray listObrok = new JSONArray(response);

                        obrokList=new ArrayList<>();
                        for (int i=0;i<listObrok.length();i++){
                            JSONObject o=listObrok.getJSONObject(i);
                            Obrok obrok=new Obrok();
                            obrok.setObrokId(o.getInt("obrokId"));
                            obrok.setDanId(o.getInt("danId"));
                            obrok.setBrojKalorija((float) o.getInt("brojKalorija"));
                            obrok.setNazivObroka(o.getString("nazivObroka"));


                            obrokList.add(obrok);
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
            String creds = String.format("%s:%s",username,pass);
            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
            params.put("Authorization", auth);
            return params;
        }
    };
    queue.add(srting);





}
    private void PopuniPodatke() {
        ObrokAdapter obrokAdapter= new ObrokAdapter(getActivity(),obrokList);

        lista.setAdapter(obrokAdapter);

    }
    public void ReqestObrokRemove(){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =GetUriParametar("Obrok","Remove","obrokId",obrokList.get(Pozicija).getObrokId());

        final StringRequest srting= new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        obrokList.remove(Pozicija);
                        PopuniPodatke();
                        Pozicija=-1;
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
    private void do_OnItemLongClick_ObrokFragment(int position) {
        Pozicija=position;
        if (Pozicija!=-1)
        ReqestObrokRemove();

    }
    private void do_OnClickFab_ObrokFragment() {

        FragmentTransaction ft= getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, new NoviObrokFragment(DanId)).addToBackStack("my_fragment").commit();


    }
    private void do_OnItemClick_ObrokFragment(int position) {

     
        FragmentTransaction ft= getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, new NamirniceObrokaFragment(obrokList.get(position).getObrokId())).addToBackStack("my_fragment").commit();
      //  MyfragmentUtils.ZamjeniFragment(a,R.id.nav_host_fragment, NamirniceObrokaFragment.newInstance(), this);

    }
}
