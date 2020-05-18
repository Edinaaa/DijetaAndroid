package com.example.vjezbanavdrawer.Helper;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MyfragmentUtils {

    public static void DodajFragment(AppCompatActivity activity, int id, Fragment fragment){

        FragmentTransaction ft=activity.getSupportFragmentManager().beginTransaction();

        ft.replace(id, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public static void ZamjeniFragment(AppCompatActivity activity, int id, Fragment fragment, Fragment stari){

        FragmentTransaction ft=activity.getSupportFragmentManager().beginTransaction();
        ft.remove(stari);
        ft.replace(id,fragment);
       ft.addToBackStack(null);
      ft.commit();
    }
}
