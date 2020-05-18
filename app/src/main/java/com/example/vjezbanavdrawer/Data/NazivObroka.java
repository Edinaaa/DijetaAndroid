package com.example.vjezbanavdrawer.Data;

import java.io.Serializable;

public class NazivObroka implements Serializable {
    private int NazivObrokaId ;
    private String Naziv;

    public int getNazivObrokaId() {
        return NazivObrokaId;
    }

    public String getNaziv() {
        return Naziv;
    }

    public void setNazivObrokaId(int nazivObrokaId) {
        NazivObrokaId = nazivObrokaId;
    }

    public void setNaziv(String naziv) {
        Naziv = naziv;
    }
}
