package com.example.vjezbanavdrawer.ui.Odjava;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.vjezbanavdrawer.Login;
import com.example.vjezbanavdrawer.MainActivity;
import com.example.vjezbanavdrawer.R;
import com.example.vjezbanavdrawer.ui.Dijete.DijeteFragment;

public class OdjavaFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_odjava, container, false);
        Button da=root.findViewById(R.id.Odjava_da_button);
        Button ne=root.findViewById(R.id.Odjava_ne_button);
        da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ObrisiSharedPreferences();
                MainActivity a= (MainActivity) getActivity();
                startActivity(new Intent(a, Login.class));

            }
        });
        ne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft=getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new DijeteFragment()).commit();
            }
        });
        return root;
    }

    public void ObrisiSharedPreferences(){

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.username_key)," ");
        editor.putString(getString(R.string.pass_key)," ");

        editor.commit();

    }
}
