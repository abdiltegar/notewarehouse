package com.example.notewarehouse.database;

public class User {
    String id_user;
    String nama;
    String email;

    public User(){

    }

    public User(String id, String nama, String email) {
        this.id_user = id;
        this.nama = nama;
        this.email = email;
    }

    public String getId() {
        return id_user;
    }

    public void setId(String id) {
        this.id_user = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String telpon) {
        this.email = email;
    }
}
