package com.example.vjezbanavdrawer.ui.Registracija;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.vjezbanavdrawer.Data.Korisnik;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUri;

public class RegistracijaFragment extends Fragment {

    private TextView Ime;
    private TextView Prezime;
    private TextView KoirsnickoIme;
    private TextView Lozinka;
    private TextView Email;
    private TextView PotvrdaLozinke;
    private TextView error;
    private Korisnik korisnik;
public  static RegistracijaFragment newInstance(){ return new RegistracijaFragment();}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_registracija, container, false);
        FloatingActionButton fab =root.findViewById(R.id.fab_fragment_registracija);
        Ime=root.findViewById(R.id.registracija_ime_textview);
        Prezime=root.findViewById(R.id.registracija_Prezime_textview);
        KoirsnickoIme=root.findViewById(R.id.registracija_korisnickoime_textview);
        Lozinka=root.findViewById(R.id.registracija_novaLozinka_textview);
        Email=root.findViewById(R.id.registracija_email_textview);
        PotvrdaLozinke=root.findViewById(R.id.registracija_potvrdaNovaLozinka_textview);
        error=root.findViewById(R.id.registracija_error_textview);
        korisnik=new Korisnik();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_OnClickFab_RegistracijaFragment();

            }
        });
        return root;
    }

    private void PozoviActiviti(){
        Login l= (Login) getActivity();
        startActivity(new Intent(l, MainActivity.class));
    }
    private boolean Validate( ) {

        error.setText("");
        if (Lozinka.getText().toString().length() <= 0 || PotvrdaLozinke.getText().toString().length() <= 0
                || Ime.getText().toString().length() <= 0|| Prezime.getText().toString().length() <= 0
                || KoirsnickoIme.getText().toString().length() <= 0|| Email.getText().toString().length() <= 0) {
            error.setText("Sva polja su obavezna!");
            return false;
        }
        else if (Lozinka.getText().toString().matches(PotvrdaLozinke.getText().toString()))
        { return true;}
        error.setText("Lozinka i potvrda lozinke se ne podudaraju.");
        return false;
    }
    private void RequestRegistracija(){


         RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =GetUri("Korisnik","Registracija");

        StringRequest srting= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                         try {
                           JSONObject korisnikJson = new JSONObject(response);
                            korisnik.KorisnikId=korisnikJson.getInt("korisnikId");
                            korisnik.Ime=korisnikJson.getString("ime");
                            korisnik.KorisnickoIme=korisnikJson.getString("korisnickoIme");
                            korisnik.Prezime=korisnikJson.getString("prezime");
                            korisnik.Lozinka=korisnikJson.getString("lozinka");
                            korisnik.Email=korisnikJson.getString("email");
                            SnimiUSharedPreferences();
                            PozoviActiviti();

                        }
                        catch (JSONException errorexc){
                            error.setText(errorexc.getMessage());
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError vollyerror) {
             ErrorHandle(vollyerror);
            }
        }){

            @Override
            public byte[] getBody(){
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("Ime", Ime.getText().toString());
                    jsonBody.put("Prezime", Prezime.getText().toString());
                    jsonBody.put("KorisnickoIme", KoirsnickoIme.getText().toString());
                    jsonBody.put("Lozinka", Lozinka.getText().toString());
                    jsonBody.put("Email", Email.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    return jsonBody.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
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




    }
    private void SnimiUSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.username_key),korisnik.KorisnickoIme);
        editor.putString(getString(R.string.pass_key),korisnik.Lozinka);

        editor.commit();

    }
    private  void do_OnClickFab_RegistracijaFragment(){
    if (Validate())
        RequestRegistracija();
    }
}
