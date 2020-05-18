package com.example.vjezbanavdrawer.ui.NamirniceObroka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.example.vjezbanavdrawer.Data.Namirnica;
import com.example.vjezbanavdrawer.Data.NamirniceObroka;
import com.example.vjezbanavdrawer.Data.UkupnoVM;
import com.example.vjezbanavdrawer.Helper.MyRunable;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.example.vjezbanavdrawer.ui.DetaljiNamirnice.DetaljiNamirniceFragment;
import com.example.vjezbanavdrawer.ui.PretragaNamirnica.PretragaNamirnicaDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUri;
import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUriParametar;

public class NamirniceObrokaFragment extends Fragment {

    private int ObrokId;
    private Namirnica namirnica;
    private String username;
    private String pass;
    private int Pozicija;
    private List<NamirniceObroka> namirniceObrokaList;
    private View root;
    private ListView lista;

    public NamirniceObrokaFragment(int obrokId) {
       ObrokId=obrokId;


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        MainActivity a=(MainActivity)getActivity();
        a.setNav(R.id.nav_dijete,"Namirnice");
         root = inflater.inflate(R.layout.fragment_namirnice_obroka, container, false);

         lista= (ListView)root.findViewById(R.id.namirnice_obroka_listView);
        Pozicija=-1;
        RequestNamirniceObroka();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                do_OnItemClick_NamirniceObrokaFragment(position);

            }
        });
        //za brisanje ithema
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                do_OnItemLongClick_NamirniceObrokaFragment(position);
                return false;
            }
        });
        FloatingActionButton fab =root.findViewById(R.id.fab_fragment_namirnice_obroka);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                do_OnClickFab_NamirniceObrokaFragment();

            }
        });

        return root;}

    private void PopuniPodatke() {

        NamirniceObrokaAdapter namirniceObrokaAdapter= new NamirniceObrokaAdapter(getActivity(), namirniceObrokaList);
        lista.setAdapter(namirniceObrokaAdapter);

        List<UkupnoVM>  ukupnoVMList=UkupnoVM.getUkupnoVMList(namirniceObrokaList);
        NamirniceObrokaUkupnoAdapter namirniceObrokaUkupnoAdapter=new NamirniceObrokaUkupnoAdapter(getActivity(),ukupnoVMList);
        ListView ListaUkupno= (ListView) root.findViewById(R.id.namirnice_obroka_ukupno_listView) ;
        ListaUkupno.setAdapter(namirniceObrokaUkupnoAdapter);
    }
    private void RequestNamirniceObroka(){

        String url=GetUriParametar("NamirniceObrok","Index", "ObrokId",ObrokId);
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        final   StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONArray namirniceO= new JSONArray(response);
                namirniceObrokaList=new ArrayList<>();
                for (int i=0;i<namirniceO.length();i++){
                    JSONObject namirnica=namirniceO.getJSONObject(i);
                    NamirniceObroka namirniceObroka=new NamirniceObroka();
                    namirniceObroka.setNamirniceObrokaId(namirnica.getInt("namirniceObrokaId"));
                    namirniceObroka.setObrokId(namirnica.getInt("obrokId"));
                    namirniceObroka.setKalorije((float) namirnica.getInt("kalorije"));
                    namirniceObroka.setKolicina(namirnica.getInt("kolicina"));
                    namirniceObroka.setNamirnicaId(namirnica.getInt("namirnicaId"));
                    Namirnica n=new Namirnica();
                    n.setNaziv(namirnica.getJSONObject("namirnica").getString("naziv"));
                    n.setJM(namirnica.getJSONObject("namirnica").getString("jm"));
                    n.setKalorije((float)namirnica.getJSONObject("namirnica").getInt("kalorije"));
                    n.setKolicina(namirnica.getJSONObject("namirnica").getInt("kolicina"));
                    n.setMasti(namirnica.getJSONObject("namirnica").getInt("masti"));
                    n.setNamirnicaId(namirnica.getJSONObject("namirnica").getInt("namirnicaId"));
                    n.setProteini(namirnica.getJSONObject("namirnica").getInt("proteini"));
                    n.setUgljikohidrati(namirnica.getJSONObject("namirnica").getInt("ugljikohidrati"));
                    namirniceObroka.setNamirnica(n);
                    namirniceObrokaList.add(namirniceObroka);
                }
                PopuniPodatke();
            } catch (JSONException e) {

                String s=e.getMessage();


            }

        }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandle(error);


            }
        }) {
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
    queue.add(stringRequest);
}
    private void RequestNamirniceObrokaAdd(){

        String url=GetUri("NamirniceObrok","Add");
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        final   StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray namirniceO= new JSONArray(response);
                    namirniceObrokaList=new ArrayList<>();
                    for (int i=0;i<namirniceO.length();i++){
                        JSONObject namirnica=namirniceO.getJSONObject(i);
                        NamirniceObroka namirniceObroka=new NamirniceObroka();
                        namirniceObroka.setNamirniceObrokaId(namirnica.getInt("namirniceObrokaId"));
                        namirniceObroka.setObrokId(namirnica.getInt("obrokId"));
                        namirniceObroka.setKalorije((float) namirnica.getInt("kalorije"));
                        namirniceObroka.setKolicina(namirnica.getInt("kolicina"));
                        namirniceObroka.setNamirnicaId(namirnica.getInt("namirnicaId"));
                        Namirnica n=new Namirnica();
                        n.setNaziv(namirnica.getJSONObject("namirnica").getString("naziv"));
                        n.setJM(namirnica.getJSONObject("namirnica").getString("jm"));
                        n.setKalorije((float)namirnica.getJSONObject("namirnica").getInt("kalorije"));
                        n.setKolicina(namirnica.getJSONObject("namirnica").getInt("kolicina"));
                        n.setMasti(namirnica.getJSONObject("namirnica").getInt("masti"));
                        n.setNamirnicaId(namirnica.getJSONObject("namirnica").getInt("namirnicaId"));
                        n.setProteini(namirnica.getJSONObject("namirnica").getInt("proteini"));
                        n.setUgljikohidrati(namirnica.getJSONObject("namirnica").getInt("ugljikohidrati"));
                        namirniceObroka.setNamirnica(n);
                        namirniceObrokaList.add(namirniceObroka);
                    }
                    PopuniPodatke();
                } catch (JSONException e) {

                    String s=e.getMessage();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandle(error);

            }
        }) {
            @Override
            public byte[] getBody(){
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("Kolicina",namirnica.getKolicina() );
                    jsonBody.put("NamirnicaId",namirnica.getNamirnicaId() );
                    jsonBody.put("ObrokId",ObrokId);




                } catch (JSONException e) {
                    e.printStackTrace();

                }


                try {
                    Log.e("D",jsonBody.toString());
                    return jsonBody.toString().getBytes("utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();

                }
                Log.e("D","Outside try catch");
                return null;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
               GetFromStaredPreferences();
                String creds = String.format("%s:%s",username, pass);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

        };
        queue.add(stringRequest);
    }
    public void ReqestNamirniceObrokaRemove(){


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =GetUriParametar("NamirniceObrok","Remove","namirniceObrokaId",namirniceObrokaList.get(Pozicija).getNamirniceObrokaId());

        final StringRequest srting= new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        namirniceObrokaList.remove(Pozicija);
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
    private void do_OnClickFab_NamirniceObrokaFragment() {
        MyRunable callback= new MyRunable<Namirnica>() {
            @Override
            public void Run(Namirnica n) {

                namirnica=n;
                if (namirnica!= null)
                RequestNamirniceObrokaAdd();
            }
        };


        FragmentManager fm=getFragmentManager();
        PretragaNamirnicaDialogFragment dialog=  PretragaNamirnicaDialogFragment.newInstance(callback);
        dialog.show(fm,"nekiDialog");
    }
    private void do_OnItemLongClick_NamirniceObrokaFragment(int position){
            Pozicija=position;
        if (Pozicija!=-1)
            ReqestNamirniceObrokaRemove();
    }
    private void do_OnItemClick_NamirniceObrokaFragment(int position) {
        if (Pozicija==-1){

            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment,new DetaljiNamirniceFragment(namirniceObrokaList.get(position).getNamirnicaId())).addToBackStack("my_fragment").commit();


        }

        }

}
