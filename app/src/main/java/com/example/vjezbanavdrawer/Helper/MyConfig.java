package com.example.vjezbanavdrawer.Helper;

public class MyConfig {
    public  static  String uri="http://s90.wrd.app.fit.ba/";

    public static String GetUri(String kontroler, String akcija){ return  uri+kontroler+"/"+akcija;}
    public static String GetUriParametar(String kontroler, String akcija, String naziv, int id){


        String parametar="?"+naziv+"="+id;
        return  uri+kontroler+"/"+akcija+parametar;}
    public static String GetUriParametar(String kontroler, String akcija, String naziv, String text){


        String parametar="?"+naziv+"="+text;
        return  uri+kontroler+"/"+akcija+parametar;}
}
