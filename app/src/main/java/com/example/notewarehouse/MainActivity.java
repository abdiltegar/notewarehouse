package com.example.notewarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.notewarehouse.database.DBController;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {//membuat delay
            @Override
            public void run() {
                HashMap<String, String> loggedUser = controller.findData();//mencari data user yang login di DB SQLite
                if(loggedUser.isEmpty()){//jika kosong maka ke halaman login
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{//jika ditemukan maka ke halaman home
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);//3 detik
    }
}