package com.example.vjezbanavdrawer.ui.NoviObrok;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
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
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.example.vjezbanavdrawer.ui.Obrok.ObrokFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUri;

public class NoviObrokFragment extends Fragment {

    private int DanId;
    private String username;
    private  String pass;
    private   TextView   error;
    private   AutoCompleteTextView   editTextFilledExposedDropdown;
    public NoviObrokFragment(int danId) {
        DanId=danId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_novi_obrok, container, false);
       error= root.findViewById(R.id.error_novi_obrok);
        MainActivity a=(MainActivity)getActivity();
        a.setNav(R.id.nav_dijete,"Novi obrok");
        String[] Nazivi = new String[]{"Doručak","Ručak","Večera","Užina","Dodatak"};
       ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,Nazivi);

      editTextFilledExposedDropdown = root.findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

        FloatingActionButton fab =root.findViewById(R.id.fab_fragment_novi_obrok);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_OnClick_NoviObrokFragment();
            }
        });
        return root;
    }
    private void RequestAddObrok(){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =GetUri("Obrok", "Add");

        final StringRequest srting= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PozoviActiviti();

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandle(error);

            }
        }){
            @Override
            public byte[] getBody(){
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("NazivObroka",editTextFilledExposedDropdown.getText().toString() );
                    jsonBody.put("DanId",DanId );


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
    private boolean Validate(){
        error.setText("");
        if (editTextFilledExposedDropdown.getText().length()==0)
            error.setText("Morate odabrati ili unjeti novi naziv obroka!");
        return  error.getText().length()==0;
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
    public void PozoviActiviti(){
        FragmentTransaction ft= getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment,new ObrokFragment(DanId)).addToBackStack("my_fragment").commit();

        }
    private void do_OnClick_NoviObrokFragment() {

       if (Validate())
        RequestAddObrok();

    }
}
