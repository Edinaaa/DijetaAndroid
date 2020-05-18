package com.example.vjezbanavdrawer.Data;

import java.io.Serializable;

public class Obrok implements Serializable {

    private int ObrokId;

    private String NazivObroka;
    private int DanId ;
    private Dan Dan ;
    private float BrojKalorija;

    public int getDanId() {
        return DanId;
    }

    public float getBrojKalorija() {
        return BrojKalorija;
    }

    public Dan getDan() {
        return Dan;
    }



    public int getObrokId() {
        return ObrokId;
    }

    public String getNazivObroka() {
        return NazivObroka;
    }

    public void setDanId(int danId) {
        DanId = danId;
    }

    public void setBrojKalorija(float brojKalorija) {
        BrojKalorija = brojKalorija;
    }

    public void setDan(Dan dan) {
        Dan = dan;
    }

    public void setNazivObroka(String nazivObroka) {
        NazivObroka = nazivObroka;
    }



    public void setObrokId(int obrokId) {
        ObrokId = obrokId;
    }
}
