package com.example.vjezbanavdrawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.vjezbanavdrawer.ui.Prijava.PrijavaFragment;

public class Login extends AppCompatActivity {

    String username;
    String pass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        GetFromStaredPreferences(this);
        if (username.length()> 1)
        {
            startActivity(new Intent(this, MainActivity.class));
        }

        FragmentTransaction ft=this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.login_host_fragment,PrijavaFragment.newInstance()).commit();
    }

    public void GetFromStaredPreferences(Context context){


        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username=sharedPref.getString(getString(R.string.username_key)," ");
        pass=sharedPref.getString(getString(R.string.pass_key)," ");
    }
   public void setToolbar(String s){

       getSupportActionBar().setTitle(s);

}
}
