package com.example.notewarehouse.laporanstok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notewarehouse.HomeActivity;
import com.example.notewarehouse.LoginActivity;
import com.example.notewarehouse.R;
import com.example.notewarehouse.adapter.LaporanStockAdapter;
import com.example.notewarehouse.database.LaporanStock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LaporanStokActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LaporanStockAdapter adapter;
    private ArrayList<LaporanStock> laporanStockArrayList = new ArrayList<LaporanStock>();;

    static String hostname = "http://192.168.100.8";
//    static String hostname = "http://10.20.14.90";

    private static final String TAG = LaporanStokActivity.class.getSimpleName();
    private static String url_select = hostname+"/ws-notewarehouse/laporan_stok/";
//    private static String url_select = "http://10.20.14.90/ws-notewarehouse/laporan_stok/";
    public static final String TAG_STATUS = "status";
    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_KD = "kode_barang";
    public static final String TAG_NAMA = "nama_barang";
    public static final String TAG_SATUAN = "satuan";
    public static final String TAG_JUMLAH = "jumlah";

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_stok);

        recyclerView = findViewById(R.id.LaporanStockRecyclerView);
        logo = findViewById(R.id.logoLaporanStok);

        BacaData();

        adapter = new LaporanStockAdapter(laporanStockArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LaporanStokActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaporanStokActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void BacaData(){
        if(laporanStockArrayList != null && laporanStockArrayList.size() > 0){
            laporanStockArrayList.clear();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jArr = new StringRequest(url_select, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getInt(TAG_STATUS) == 1 ? true : false;
                    if (status) {
                        JSONObject arrayData = jObj.getJSONObject(TAG_DATA);
                        for (int i = 0; i < arrayData.length(); i++) {
                            JSONObject obj = arrayData.getJSONObject('a'+String.valueOf(i));

                            LaporanStock item = new LaporanStock();
                            item.setId(obj.getString(TAG_KD));
                            item.setNamaBarang(obj.getString(TAG_NAMA));
                            item.setSatuan(obj.getString(TAG_SATUAN));
                            item.setJumlah(obj.getInt(TAG_JUMLAH));

                            laporanStockArrayList.add(item);
                        }
                    } else {
                        Toast.makeText(LaporanStokActivity.this, "Gagal : "+jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error : "+error.getMessage());
                error.printStackTrace();
                Toast.makeText(LaporanStokActivity.this, "gagal", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jArr);
    }
}