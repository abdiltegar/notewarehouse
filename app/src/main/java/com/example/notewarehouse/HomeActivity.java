package com.example.notewarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notewarehouse.barangkeluar.BarangKeluarActivity;
import com.example.notewarehouse.barangmasuk.BarangMasukActivity;
import com.example.notewarehouse.database.DBController;
import com.example.notewarehouse.database.User;
import com.example.notewarehouse.laporanstok.LaporanStokActivity;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    TextView tvNama;//deklarasi variable TextView
    ImageView btnBarangMasuk, btnBarangKeluar, btnLaporanStok, btnLogout;//deklarasi variable ImageView
    DBController controller = new DBController(this);//deklarasi variable controller menginisialisasi DBController

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //menghubungakan dengan view {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvNama = findViewById(R.id.tvHomeNama);
        btnBarangMasuk = findViewById(R.id.btnHomeBarangMasuk);
        btnBarangKeluar = findViewById(R.id.btnHomeBarangKeluar);
        btnLaporanStok = findViewById(R.id.btnHomeLaporanStock);
        btnLogout = findViewById(R.id.btnLogout);
        // }

        HashMap<String, String> loggedUser = controller.findData();//mengambil data user yang login

        //memasukkan data user yang login ke variable User {
        User user = new User();
        user.setId(loggedUser.get("id_user").toString());
        user.setNama(loggedUser.get("nama").toString());
        user.setEmail(loggedUser.get("email").toString());
        /// }

        tvNama.setText(user.getNama().toString());//menampilkan nama user yang login di TextView Nama

        btnBarangMasuk.setOnClickListener(new View.OnClickListener() {//event ketika barang masuk diklik
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BarangMasukActivity.class);//membuat intent ke halaman Barang Masuk
                startActivity(intent);//memulai intent
//                finish();
            }
        });

        btnBarangKeluar.setOnClickListener(new View.OnClickListener() {//event ketika barang keluar diklik
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BarangKeluarActivity.class);//membuat intent ke halaman Barang Keluar
                startActivity(intent);//memulai intent
//                finish();
            }
        });

        btnLaporanStok.setOnClickListener(new View.OnClickListener() {//event ketika laporan stok diklik
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LaporanStokActivity.class);//membuat intent ke halaman Laporan Stok
                startActivity(intent);//memulai intent
//                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {//event ketika logout diklik
            @Override
            public void onClick(View v) {
                controller.deleteData(user.getId());//menghapus data login user di SQLite

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);//membuat intent ke halaman Login
                startActivity(intent);//memulai intent
                finish();
            }
        });

    }
}