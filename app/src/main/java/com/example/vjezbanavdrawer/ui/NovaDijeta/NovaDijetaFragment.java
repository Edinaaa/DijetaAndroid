package com.example.vjezbanavdrawer.ui.NovaDijeta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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

public class NovaDijetaFragment extends Fragment {
    private TextView Naziv;
    private TextView BrojDana;
    private TextView error;
    private String username;
    private String pass;



    public View onCreateView( @NonNull LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {


            View root = inflater.inflate(R.layout.fragment_nova_dijeta, container, false);
        FloatingActionButton fab =root.findViewById(R.id.fab_fragment_nova_dijeta);
        Naziv=root.findViewById(R.id.nova_dijeta_naziv_textview);
        BrojDana=root.findViewById(R.id.nova_dijeta_brojDana_textview);
        error=root.findViewById(R.id.nova_dijeta_error_textview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_OnClickFab_NovaDijetaFragment();
            }
        });
            return root;
    }

    private void PozoviActiviti(){
        FragmentTransaction ft= getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,new DijeteFragment()).commit();

    }
    private boolean Validate( ) {

        error.setText("");
        if (Naziv.getText().toString().length() <= 0 || BrojDana.getText().toString().length() <= 0) {
            error.setText("Sva polja su obavezna!");
            return false;
        }
        return true;
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
    private void RequestNovaDijeta(){

    FragmentActivity f=getActivity();

        RequestQueue queue = Volley.newRequestQueue(f);
        String url =GetUri("Dijeta","Add");

        StringRequest srting= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        PozoviActiviti();
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
                   jsonBody.put("Naziv", Naziv.getText().toString());
                    jsonBody.put("BrojDana",Integer.parseInt(BrojDana.getText().toString()));




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
        queue.add(srting);

    }
    private void do_OnClickFab_NovaDijetaFragment() {
        if (Validate())
        RequestNovaDijeta();

    }
}
