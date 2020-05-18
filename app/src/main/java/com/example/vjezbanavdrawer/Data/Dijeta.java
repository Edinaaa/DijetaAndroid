package com.example.vjezbanavdrawer.Data;

import java.io.Serializable;

public class Dijeta implements Serializable {

    public int DijetaId ;
    public String Naziv;
    public int BrojDana;
    public float Kalorije;
    public int KorisnikId;
    public Korisnik Korisnik;

    public String getNaziv() {
        return Naziv;
    }

    public int getBrojDana() {
        return BrojDana;
    }

    public float getKalorije() {
        return Kalorije;
    }

    public int getDijetaId() {
        return DijetaId;
    }

    public int getKorisnikId() {
        return KorisnikId;
    }

    public com.example.vjezbanavdrawer.Data.Korisnik getKorisnik() {
        return Korisnik;
    }

    public void setNaziv(String naziv) {
        Naziv = naziv;
    }

    public void setKalorije(float kalorije) {
        Kalorije = kalorije;
    }

    public void setBrojDana(int brojDana) {
        BrojDana = brojDana;
    }

    public void setDijetaId(int dijetaId) {
        DijetaId = dijetaId;
    }

    public void setKorisnik(com.example.vjezbanavdrawer.Data.Korisnik korisnik) {
        Korisnik = korisnik;
    }

    public void setKorisnikId(int korisnikId) {
        KorisnikId = korisnikId;
    }
}
