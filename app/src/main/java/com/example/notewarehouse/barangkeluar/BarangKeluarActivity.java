package com.example.notewarehouse.barangkeluar;

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
import com.example.notewarehouse.R;
import com.example.notewarehouse.adapter.BarangKeluarAdapter;
import com.example.notewarehouse.adapter.LaporanStockAdapter;
import com.example.notewarehouse.database.BarangKeluar;
import com.example.notewarehouse.database.LaporanStock;
import com.example.notewarehouse.laporanstok.LaporanStokActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BarangKeluarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BarangKeluarAdapter adapter;
    private ArrayList<BarangKeluar> barangKeluarArrayList = new ArrayList<BarangKeluar>();;
    //    private ListView list;
    private FloatingActionButton fab;

    static String hostname = "http://192.168.100.8";
//    static String hostname = "http://10.20.14.90";

    private static final String TAG = BarangKeluar.class.getSimpleName();
    private static String url_select = hostname+"/ws-notewarehouse/barang_keluar/";
    //    private static String url_select = "http://10.20.14.90/ws-notewarehouse/barang_keluar/";
    public static final String TAG_STATUS = "status";
    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_id_arus = "id_arus";
    public static final String TAG_id_user = "id_user";
    public static final String TAG_kode_barang = "kode_barang";
    public static final String TAG_nama_barang = "nama_barang";
    public static final String TAG_satuan = "satuan";
    public static final String TAG_kode_ruangan = "kode_ruangan";
    public static final String TAG_nama_ruangan = "nama_ruangan";
    public static final String TAG_jumlah = "jumlah";
    public static final String TAG_tanggal = "tanggal";
    public static final String TAG_apakah_masuk = "apakah_masuk";

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_keluar);

        recyclerView = findViewById(R.id.BarangKeluarRecyclerView);
        fab = findViewById(R.id.fabTambahBarangKeluar);
        logo = findViewById(R.id.logoBarangKeluar);

        BacaData();

        recyclerView = findViewById(R.id.BarangKeluarRecyclerView);
        adapter = new BarangKeluarAdapter(barangKeluarArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BarangKeluarActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarangKeluarActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarangKeluarActivity.this,BarangKeluarTambahActivity.class);
                startActivity(intent);
            }
        });
    }

    public void BacaData(){
        if(barangKeluarArrayList != null && barangKeluarArrayList.size() > 0){
            barangKeluarArrayList.clear();
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

                            BarangKeluar item = new BarangKeluar();
                            item.setId(obj.getInt(TAG_id_arus));
                            item.setIdUser(obj.getInt(TAG_id_user));
                            item.setKodeBarang(obj.getString(TAG_kode_barang));
                            item.setNamaBarang(obj.getString(TAG_nama_barang));
                            item.setSatuan(obj.getString(TAG_satuan));
                            item.setKodeRuangan(obj.getString(TAG_kode_ruangan));
                            item.setNamaRuangan(obj.getString(TAG_nama_ruangan));
                            item.setJumlah(obj.getInt(TAG_jumlah));
                            item.setTanggal(obj.getString(TAG_tanggal));
                            item.setApakahMasuk(obj.getInt(TAG_apakah_masuk) == 1 ? true : false);

                            barangKeluarArrayList.add(item);
                        }

                    } else {
                        Toast.makeText(BarangKeluarActivity.this, "Gagal : "+jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BarangKeluarActivity.this, "gagal", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jArr);
    }
}