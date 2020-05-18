package com.example.vjezbanavdrawer.ui.Postavke;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.vjezbanavdrawer.Data.Korisnik;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.example.vjezbanavdrawer.ui.Dijete.DijeteFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUri;

public class PostavkeFragment extends Fragment {

    private String username;
    private String pass;
    private EditText Ime;
    private EditText Email;
    private EditText KorisnickoIme;
    private EditText Lozinka;
    private EditText LozinkaPotvrda;
    private  View root;
    private Korisnik korisnik;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_postavke, container, false);
        RequestIndex();

        FloatingActionButton fab =root.findViewById(R.id.fab_fragment_postavke);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_OnClick_PostavkeFragment();


            }
        });
        return root;
    }
    private void PopuniPodatke(){


        Ime=root.findViewById(R.id.postavke_ime_textview);
        Email=root.findViewById(R.id.postavke_email_textview);
        KorisnickoIme=root.findViewById(R.id.postavke_korisnickoime_textview);
        Lozinka=root.findViewById(R.id.postavke_novaLozinka_textview);
        LozinkaPotvrda=root.findViewById(R.id.postavke_potvrdaNovaLozinka_textview);
        String imep=korisnik.getIme()+" "+korisnik.getPrezime();

        Ime.setText(imep);
        Lozinka.setText(korisnik.getLozinka());
        LozinkaPotvrda.setText(korisnik.getLozinka());
        KorisnickoIme.setText(korisnik.getKorisnickoIme());
        Email.setText(korisnik.getEmail());
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

    private void RequestIndex(){

        String url= GetUri("Korisnik","Index");
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        final StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                        JSONObject k=new JSONObject(response);
                     korisnik=new Korisnik();
                     korisnik.setKorisnikId(k.getInt("korisnikId"));
                     korisnik.setIme(k.getString("ime"));
                    korisnik.setPrezime(k.getString("prezime"));
                    korisnik.setEmail(k.getString("email"));
                    korisnik.setKorisnickoIme(k.getString("korisnickoIme"));
                    korisnik.setLozinka(k.getString("lozinka"));
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                GetFromStaredPreferences();
                String creds = String.format("%s:%s",username, pass);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

        };
        queue.add(stringRequest);
    }
    public void SnimiUSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.username_key),KorisnickoIme.getText().toString());
        editor.putString(getString(R.string.pass_key),Lozinka.getText().toString());

        editor.apply();

    }
    private void RequestPostavke(){

        String url=GetUri("Korisnik","Postavke");
        final RequestQueue queue= Volley.newRequestQueue(getActivity());
        final StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SnimiUSharedPreferences();
                PozioviActiviti();

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
                    String imep=Ime.getText().toString();
                    String[] splited = imep.split("\\s+");
                    jsonBody.put("Ime",splited[0] );
                    jsonBody.put("Prezime",splited[1] );
                    jsonBody.put("Lozinka",Lozinka.getText().toString() );
                    jsonBody.put("KorisnickoIme",KorisnickoIme.getText().toString() );
                    jsonBody.put("Email",Email.getText().toString() );

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
    private boolean Validate(){

        if (Ime.getText().length()<=0 || KorisnickoIme.getText().length()<=0
                || Lozinka.getText().length()<=0 || LozinkaPotvrda.getText().length()<=0
                || Email.getText().length()<=0  )
        return false;
        String imep=Ime.getText().toString();
        String[] splited = imep.split("\\s+");
        if (splited[0].length()==0  || splited[0].length()==0 )
            return false;
        if (LozinkaPotvrda.getText().toString().equals(Lozinka.getText().toString()))
        return true;
        return false;
    }
    private void PozioviActiviti(){

        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, new DijeteFragment()).commit();

    }
    private void do_OnClick_PostavkeFragment() {

        if (Validate())
            RequestPostavke();

    }
}
