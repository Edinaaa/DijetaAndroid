package com.example.vjezbanavdrawer.Data;

import java.io.Serializable;

public class Namirnica implements Serializable {
    private int NamirnicaId ;
    private String Naziv ;
    private String JM ;
    private float Kalorije ;
    private float Ugljikohidrati ;
    private float Masti ;
    private float Proteini ;

    private int Kolicina ;

    public String getNaziv() {
        return Naziv;
    }

    public float getKalorije() {
        return Kalorije;
    }

    public String getJM() {
        return JM;
    }

    public float getMasti() {
        return Masti;
    }

    public float getProteini() {
        return Proteini;
    }

    public float getUgljikohidrati() {
        return Ugljikohidrati;
    }

    public int getKolicina() {
        return Kolicina;
    }

    public int getNamirnicaId() {
        return NamirnicaId;
    }

    public void setNaziv(String naziv) {
        Naziv = naziv;
    }

    public void setKalorije(float kalorije) {
        Kalorije = kalorije;
    }

    public void setJM(String JM) {
        this.JM = JM;
    }

    public void setUgljikohidrati(float ugljikohidrati) {
        Ugljikohidrati = ugljikohidrati;
    }

    public void setMasti(float masti) {
        Masti = masti;
    }

    public void setKolicina(int kolicina) {
        Kolicina = kolicina;
    }

    public void setProteini(float proteini) {
        Proteini = proteini;
    }

    public void setNamirnicaId(int namirnicaId) {
        NamirnicaId = namirnicaId;
    }

}
