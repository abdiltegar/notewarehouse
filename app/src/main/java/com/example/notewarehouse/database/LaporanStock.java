package com.example.notewarehouse.database;

public class LaporanStock {
    String kode_barang;
    String nama_barang;
    String satuan;
    int jumlah;

    public LaporanStock(){

    }

    public LaporanStock(String kode_barang, String nama_barang, String satuan, int jumlah) {
        this.kode_barang = kode_barang;
        this.nama_barang = nama_barang;
        this.satuan = satuan;
        this.jumlah = jumlah;
    }

    public String getId() {
        return kode_barang;
    }

    public void setId(String kode_barang) {
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

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
