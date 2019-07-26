package com.gookkis.firebaseuas;

public class NilaiModel {
    private String id, matkul,nilai;

    public NilaiModel() {
    }

    public NilaiModel(String id, String matkul, String nilai) {
        this.id = id;
        this.matkul = matkul;
        this.nilai = nilai;
    }

    public String getId() {
        return id;
    }

    public String getMatkul() {
        return matkul;
    }

    public String getNilai() {
        return nilai;
    }
}
