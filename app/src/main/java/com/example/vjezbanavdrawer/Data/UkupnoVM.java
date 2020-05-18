package com.example.vjezbanavdrawer.Data;

import java.util.ArrayList;
import java.util.List;

public class UkupnoVM {
    String Naziv;
    float Broj;
    public  UkupnoVM(String naziv, float broj){
        Naziv=naziv;
        Broj=broj;

    }

    public String getNaziv() {
        return Naziv;
    }

    public float getBroj() {
        return Broj;
    }

    public void setNaziv(String naziv) {
        Naziv = naziv;
    }

    public void setBroj(float broj) {
        Broj = broj;
    }

    public static List<UkupnoVM> getUkupnoVMList(List<NamirniceObroka> namirniceObrokaS) {
        List<UkupnoVM> ukupnoVMList;
        ukupnoVMList=new ArrayList<>();
        ukupnoVMList.add(new UkupnoVM("Ukupno",0));

        ukupnoVMList.add(new UkupnoVM("Kalorije",getNamirniceObrokaList_Zbir(namirniceObrokaS,"Kalorije")));
        ukupnoVMList.add(new UkupnoVM("Ugljikohidrati",getNamirniceObrokaList_Zbir(namirniceObrokaS,"Ugljikohidrati")));
        ukupnoVMList.add(new UkupnoVM("Proteini",getNamirniceObrokaList_Zbir(namirniceObrokaS,"Proteini")));
        ukupnoVMList.add(new UkupnoVM("Masti",getNamirniceObrokaList_Zbir(namirniceObrokaS,"Masti")));

        return ukupnoVMList;
    }

    public  static float getNamirniceObrokaList_Zbir(List<NamirniceObroka> namirniceObrokaS, String naziv){
        float ukupno=0;
        if (namirniceObrokaS==null)
        {return ukupno;}
        for (NamirniceObroka x:namirniceObrokaS
        ) {

            switch (naziv){
                case "Kalorije":
                    //kalorije su vec izracunate
                    ukupno+=x.getKalorije();
                    break;
                case "Ugljikohidrati":
                    ukupno+=(x.getNamirnica().getUgljikohidrati()/x.getNamirnica().getKolicina())*x.getKolicina();

                    break;
                case "Masti":
                    ukupno+=(x.getNamirnica().getMasti()/x.getNamirnica().getKolicina())*x.getKolicina();

                    break;
                case "Proteini":
                    ukupno+=(x.getNamirnica().getProteini()/x.getNamirnica().getKolicina())*x.getKolicina();

                    break;
            }

        }
        return ukupno;
    }
}
