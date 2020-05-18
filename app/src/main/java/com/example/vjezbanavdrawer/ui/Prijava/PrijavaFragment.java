package com.example.vjezbanavdrawer.ui.Prijava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.vjezbanavdrawer.Helper.MyfragmentUtils;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.example.vjezbanavdrawer.ui.Registracija.RegistracijaFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUri;

public class PrijavaFragment extends androidx.fragment.app.Fragment {

    private EditText username;
    private TextView error;
    private EditText pass;
    private  Korisnik korisnik;
    public static Fragment newInstance(){ return new PrijavaFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_prijava, container, false);

        Button login = root.findViewById(R.id.Login_button);
        Button registracija = root.findViewById(R.id.Registracija_button);
        username=root.findViewById(R.id.login_korisnickoime_textview);
        pass=root.findViewById(R.id.login_lozinka_textview);

        error=root.findViewById(R.id.Login_error_textview);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_OnClickLogin_PrijavaFragment();
            }
        });
        registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_OnClickRegistracija_PrijavaFragment();
            }

        });
        return root;
    }
    private boolean Validate( ){

          error.setText("");
          if (username.getText().length()<=0 || pass.getText().length()<=0)
          {
              error.setText("Username i pass moraju biti uneseni!");
              return false;
          }
        return true;
    }
    private void PozoviActiviti(){
        Login l= (Login) getActivity();
        startActivity(new Intent(l, MainActivity.class));
    }
    private void RequestPrijava(){


    RequestQueue queue = Volley.newRequestQueue(getActivity());
    String url =GetUri("Korisnik","Index");

    StringRequest srting= new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject korisnikJson = new JSONObject(response);
                        korisnik=new Korisnik();
                        korisnik.KorisnikId=korisnikJson.getInt("korisnikId");
                        korisnik.Ime=korisnikJson.getString("ime");
                        korisnik.KorisnickoIme=korisnikJson.getString("korisnickoIme");
                        korisnik.Prezime=korisnikJson.getString("prezime");
                        korisnik.Lozinka=korisnikJson.getString("lozinka");
                        korisnik.Email=korisnikJson.getString("email");

                     SnimiUSharedPreferences();

                        PozoviActiviti();
                      //  error.setText("");
                    }
                    catch (JSONException errorexc){
                        error.setText("json");

                        //validno=errorexc.getMessage();

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
        public Map<String, String> getHeaders()throws AuthFailureError {
            HashMap<String, String> params = new HashMap<String, String>();
            String creds = String.format("%s:%s", username.getText().toString(), pass.getText().toString());
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




    }
    public void SnimiUSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.username_key),username.getText().toString());
        editor.putString(getString(R.string.pass_key),pass.getText().toString());

        editor.commit();

    }
    private void  do_OnClickLogin_PrijavaFragment(){


        if(Validate()) {

            RequestPrijava();
        }


    }
    private void do_OnClickRegistracija_PrijavaFragment(){
        Login a= (Login) getActivity();
        a.setToolbar("Registracija");
        //FragmentTransaction ft=getFragmentManager().beginTransaction();
       // ft.replace(R.id.login_host_fragment,  RegistracijaFragment.newInstance()).commit();
        MyfragmentUtils.DodajFragment(a,R.id.login_host_fragment, RegistracijaFragment.newInstance());
    }
}
