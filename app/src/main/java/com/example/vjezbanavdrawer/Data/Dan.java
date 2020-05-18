package com.example.vjezbanavdrawer.Data;

import java.io.Serializable;

public class Dan implements Serializable {
    private int DanId ;

    private float BrojKalorija;
    private int DijetaId;
    private Dijeta Dijeta;

    public int getDijetaId() {
        return DijetaId;
    }

    public Dijeta getDijeta() {
        return Dijeta;
    }

    public float getBrojKalorija() {
        return BrojKalorija;
    }

    public int getDanId() {
        return DanId;
    }

    public void setDijetaId(int dijetaId) {
        DijetaId = dijetaId;
    }

    public void setBrojKalorija(float brojKalorija) {
        BrojKalorija = brojKalorija;
    }

    public void setDanId(int danId) {
        DanId = danId;
    }
}
