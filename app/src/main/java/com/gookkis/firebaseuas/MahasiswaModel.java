package com.gookkis.firebaseuas;

public class MahasiswaModel {
    private String id, nama, prodi, urlGambar;

    public MahasiswaModel() {
    }

    public MahasiswaModel(String id, String nama, String prodi, String urlGambar) {
        this.id = id;
        this.nama = nama;
        this.prodi = prodi;
        this.urlGambar = urlGambar;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public void setUrlGambar(String urlGambar) {
        this.urlGambar = urlGambar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }
}
