package com.example.vjezbanavdrawer.Data;

import java.io.Serializable;

public class NamirniceObroka implements Serializable {

    private int NamirniceObrokaId;
    private int Kolicina;
    private float Kalorije;
    private int ObrokId;
    private Obrok Obrok;

    private int NamirnicaId;
    private Namirnica Namirnica;

    public int getNamirnicaId() {
        return NamirnicaId;
    }

    public int getKolicina() {
        return Kolicina;
    }

    public float getKalorije() {
        return Kalorije;
    }

    public int getObrokId() {
        return ObrokId;
    }

    public int getNamirniceObrokaId() {
        return NamirniceObrokaId;
    }

    public Namirnica getNamirnica() {
        return Namirnica;
    }

    public Obrok getObrok() {
        return Obrok;
    }

    public void setNamirnicaId(int namirnicaId) {
        NamirnicaId = namirnicaId;
    }

    public void setKolicina(int kolicina) {
        Kolicina = kolicina;
    }

    public void setKalorije(float kalorije) {
        Kalorije = kalorije;
    }

    public void setObrokId(int obrokId) {
        ObrokId = obrokId;
    }

    public void setObrok(Obrok obrok) {
        Obrok = obrok;
    }

    public void setNamirnica(Namirnica namirnica) {
        Namirnica = namirnica;
    }

    public void setNamirniceObrokaId(int namirniceObrokaId) {
        NamirniceObrokaId = namirniceObrokaId;
    }

}

