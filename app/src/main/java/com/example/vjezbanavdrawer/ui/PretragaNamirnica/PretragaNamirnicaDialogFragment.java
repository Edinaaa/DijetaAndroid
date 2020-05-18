package com.example.vjezbanavdrawer.ui.PretragaNamirnica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
import com.example.vjezbanavdrawer.Helper.MyRunable;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.vjezbanavdrawer.Helper.MyConfig.GetUriParametar;

public class PretragaNamirnicaDialogFragment extends DialogFragment {

    private List<Namirnica> namirnicaList;
    private ListView lista;
    private  EditText trazi;
    private  EditText kolicina;
    private  EditText jm;
    private String username;
    private String pass;
    private Namirnica namirnica;
    private  FloatingActionButton fab ;
    private MyRunable<Namirnica> callback;
    public static final String DialogKey="nekiDialog";

    public PretragaNamirnicaDialogFragment(){
    }

    public static PretragaNamirnicaDialogFragment newInstance(MyRunable callback) {
        PretragaNamirnicaDialogFragment pretragaNamirnicaDialogFragment=new PretragaNamirnicaDialogFragment();
        Bundle args=new Bundle();
        args.putSerializable(DialogKey,callback);
        pretragaNamirnicaDialogFragment.setArguments(args);
        return pretragaNamirnicaDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null)
            callback=(MyRunable<Namirnica>) getArguments().getSerializable(DialogKey);
        setStyle(STYLE_NORMAL, R.style.TemaZaDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.dialog_fragment_pretraga_namirnica, container, false);
         lista= view.findViewById(R.id.pretraga_namirnica_lista);
       fab =view.findViewById(R.id.pretraga_namirnica_btn_trazi);
        trazi=view.findViewById(R.id.pretraga_namirnica_edittext);
        jm=view.findViewById(R.id.pretraga_jm_edinttext);
        kolicina=view.findViewById(R.id.pretraga_kolicina_edinttext);
        trazi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextChangedl();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fabDodaj();

            }
        });


        return view;
    }
    public void GetFromStaredPreferences(){


        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username=sharedPref.getString(getString(R.string.username_key),"nesto");
        pass=sharedPref.getString(getString(R.string.pass_key),"nesto pass");
    }
    private  void onTextChangedl(){

        RequestPretraga();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                namirnica= namirnicaList.get(position);
                trazi.setText(namirnica.getNaziv());
                jm.setText(namirnica.getJM());


            }
        });
    }
    private void  fabDodaj(){

        if (Validate()) {
            namirnica.setKolicina(Integer.parseInt(kolicina.getText().toString()) );
            callback.Run(namirnica);
            getDialog().dismiss();

        }

    }
    private boolean Validate(){
        if ( kolicina.getText().length()>0)
            return true;
        return false;


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

    private void RequestPretraga(){

        String url=GetUriParametar("Namirnica","Pretraga","Naziv",trazi.getText().toString());
        RequestQueue queue= Volley.newRequestQueue(getActivity());
     final    StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray namirniceO= new JSONArray(response);
                    namirnicaList=new ArrayList<>();
                    for (int i=0;i<namirniceO.length();i++){
                        JSONObject namirnica=namirniceO.getJSONObject(i);

                        Namirnica n=new Namirnica();
                        n.setNaziv(namirnica.getString("naziv"));
                        n.setJM(namirnica.getString("jm"));
                        n.setKalorije((float)namirnica.getInt("kalorije"));
                        n.setKolicina(namirnica.getInt("kolicina"));
                        n.setMasti(namirnica.getInt("masti"));
                        n.setNamirnicaId(namirnica.getInt("namirnicaId"));
                        n.setProteini(namirnica.getInt("proteini"));
                        n.setUgljikohidrati(namirnica.getInt("ugljikohidrati"));

                        namirnicaList.add(n);
                    }
                    PopuniPodatke();
                } catch (JSONException e) {
                    e.printStackTrace();
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
    private void PopuniPodatke(){


        PretragaNamirnicaAdapter pretragaNamirnicaAdapter= new PretragaNamirnicaAdapter(getActivity(),namirnicaList);

        lista.setAdapter(pretragaNamirnicaAdapter);


    }
}
