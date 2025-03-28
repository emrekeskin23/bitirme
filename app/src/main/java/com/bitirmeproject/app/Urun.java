package com.bitirmeproject.app;

public class Urun {
    private String urun_id;
    private String urun_adi;
    private String urun_fotograf;
    private String urun_aciklama;
    private long urun_fiyat;
    private String urun_sahibi_id;
    private String urun_lokasyon;
    private String urun_yuklenme_tarih;


    public Urun(String urun_id, String urun_adi, String urun_fotograf, String urun_aciklama, long urun_fiyat, String urun_sahibi_id, String urun_lokasyon, String urun_yuklenme_tarih) {
        this.urun_id = urun_id;
        this.urun_adi = urun_adi;
        this.urun_fotograf = urun_fotograf;
        this.urun_aciklama = urun_aciklama;
        this.urun_fiyat = urun_fiyat;
        this.urun_sahibi_id = urun_sahibi_id;
        this.urun_lokasyon = urun_lokasyon;
        this.urun_yuklenme_tarih = urun_yuklenme_tarih;
    }

    public Urun() {
    }

    public String getUrun_yuklenme_tarih() {
        return urun_yuklenme_tarih;
    }

    public void setUrun_yuklenme_tarih(String urun_yuklenme_tarih) {
        this.urun_yuklenme_tarih = urun_yuklenme_tarih;
    }

    public String getUrun_lokasyon() {
        return urun_lokasyon;
    }

    public void setUrun_lokasyon(String urun_lokasyon) {
        this.urun_lokasyon = urun_lokasyon;
    }

    public String getUrun_sahibi_id() {
        return urun_sahibi_id;
    }

    public void setUrun_sahibi_id(String urun_sahibi_id) {
        this.urun_sahibi_id = urun_sahibi_id;
    }

    public String getUrun_id() {
        return urun_id;
    }

    public void setUrun_id(String urun_id) {
        this.urun_id = urun_id;
    }

    public String getUrun_adi() {
        return urun_adi;
    }

    public void setUrun_adi(String urun_adi) {
        this.urun_adi = urun_adi;
    }

    public String getUrun_fotograf() {
        return urun_fotograf;
    }

    public void setUrun_fotograf(String urun_fotograf) {
        this.urun_fotograf = urun_fotograf;
    }

    public String getUrun_aciklama() {
        return urun_aciklama;
    }

    public void setUrun_aciklama(String urun_aciklama) {
        this.urun_aciklama = urun_aciklama;
    }

    public long getUrun_fiyat() {
        return urun_fiyat;
    }

    public void setUrun_fiyat(long urun_fiyat) {
        this.urun_fiyat = urun_fiyat;
    }
}

