package com.example.vjezbanavdrawer.Data;

import java.io.Serializable;

public class Korisnik implements Serializable {

    public int KorisnikId ;
    public String Ime ;
    public String Prezime ;
    public String Lozinka ;
    public String KorisnickoIme ;
    public String Email ;
    public boolean isEmpty(){if (Ime==null) return true ; return false;}

    public void setKorisnikId(int korisnikId) {
        KorisnikId = korisnikId;
    }

    public void setLozinka(String lozinka) {
        Lozinka = lozinka;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        KorisnickoIme = korisnickoIme;
    }

    public void setIme(String ime) {
        Ime = ime;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPrezime(String prezime) {
        Prezime = prezime;
    }

    public String getEmail() {
        return Email;
    }

    public int getKorisnikId() {
        return KorisnikId;
    }

    public String getPrezime() {
        return Prezime;
    }

    public String getLozinka() {
        return Lozinka;
    }

    public String getKorisnickoIme() {
        return KorisnickoIme;
    }

    public String getIme() {
        return Ime;
    }


}
