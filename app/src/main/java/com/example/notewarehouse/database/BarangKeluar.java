package com.example.notewarehouse.database;

public class BarangKeluar {
    int id_arus;
    int id_user;
    String kode_barang;
    String nama_barang;
    String satuan;
    String kode_ruangan;
    String nama_ruangan;
    int jumlah;
    String tanggal;
    boolean apakah_masuk;

    public BarangKeluar(){

    }

    public BarangKeluar(int id_arus, int id_user, String kode_barang, String nama_barang, String satuan, String kode_ruangan, String nama_ruangan, int jumlah, String tanggal, boolean apakah_masuk) {
        this.id_arus = id_arus;
        this.id_user = id_user;
        this.kode_barang = kode_barang;
        this.nama_barang = nama_barang;
        this.satuan = satuan;
        this.kode_ruangan = kode_ruangan;
        this.nama_ruangan = nama_ruangan;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.apakah_masuk = apakah_masuk;
    }

    public int getId() {
        return id_arus;
    }

    public void setId(int id) {
        this.id_arus = id;
    }

    public int getIdUser() {
        return id_user;
    }

    public void setIdUser(int id) {
        this.id_user = id;
    }

    public String getKodeBarang() {
        return kode_barang;
    }

    public void setKodeBarang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public String getNamaBarang() {
        return nama_barang;
    }

    public void setNamaBarang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) { this.satuan = satuan; }

    public String getKodeRuangan() {
        return kode_ruangan;
    }

    public void setKodeRuangan(String kode_ruangan) {
        this.kode_ruangan = kode_ruangan;
    }

    public String getNamaRuangan() {
        return nama_ruangan;
    }

    public void setNamaRuangan(String nama_ruangan) {
        this.nama_ruangan = nama_ruangan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
    public boolean getApakahMasuk(){return apakah_masuk;}
    public void setApakahMasuk(boolean apakah_masuk){this.apakah_masuk = apakah_masuk;}
}
