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
    TextView tvNama;
    ImageView btnBarangMasuk, btnBarangKeluar, btnLaporanStok, btnLogout;
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvNama = findViewById(R.id.tvHomeNama);
        btnBarangMasuk = findViewById(R.id.btnHomeBarangMasuk);
        btnBarangKeluar = findViewById(R.id.btnHomeBarangKeluar);
        btnLaporanStok = findViewById(R.id.btnHomeLaporanStock);
        btnLogout = findViewById(R.id.btnLogout);

        HashMap<String, String> loggedUser = controller.findData();

        User user = new User();
        user.setId(loggedUser.get("id_user").toString());
        user.setNama(loggedUser.get("nama").toString());
        user.setEmail(loggedUser.get("email").toString());

        tvNama.setText(user.getNama().toString());

        btnBarangMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BarangMasukActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        btnBarangKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BarangKeluarActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        btnLaporanStok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LaporanStokActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.deleteData(user.getId());

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}