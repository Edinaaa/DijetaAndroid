package com.example.vjezbanavdrawer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
private   NavigationView navigationView;
private Toolbar toolbar;
private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
             R.id.nav_dijete, R.id.nav_odjava, R.id.nav_nova_dijeta,
              R.id.nav_nova_namirnica, R.id.nav_postavke)
              .setDrawerLayout(drawerLayout)
                .build();
       NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
       NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void setNav(int id, String s){

  navigationView.getMenu().findItem(id).setChecked(true);
  getSupportActionBar().setTitle(s);

  }

    public void setNav2(int id, String s, View v){

        navigationView.getMenu().findItem(id).setChecked(true);
        getSupportActionBar().setTitle(s);
        navigationView.removeView(v);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
