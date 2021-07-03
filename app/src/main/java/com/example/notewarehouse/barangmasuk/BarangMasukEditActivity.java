package com.example.notewarehouse.barangmasuk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notewarehouse.HomeActivity;
import com.example.notewarehouse.LoginActivity;
import com.example.notewarehouse.R;
import com.example.notewarehouse.database.DBController;
import com.example.notewarehouse.database.LaporanStock;
import com.example.notewarehouse.database.User;
import com.example.notewarehouse.laporanstok.LaporanStokActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BarangMasukEditActivity extends AppCompatActivity  {
    private EditText etTanggal, etJumlah;
    private Spinner spinJenisBarang, spinRuangan;
    private Button simpanBtn, batalBtn;
    String kode_barang, kode_ruangan, tanggal;
    int success, jumlah, id_user, id_arus;
    boolean status;

    static String hostname = "http://192.168.100.8";
//    static String hostname = "http://10.20.14.90";

    private static String url_spinner_jenisbarang = hostname+"/ws-notewarehouse/master/jenis_barang";
    private static String url_spinner_ruangan = hostname+"/ws-notewarehouse/master/ruangan";
    private static String url_update = hostname+"/ws-notewarehouse/barang_masuk/update.php";
    private static final String TAG = BarangMasukEditActivity.class.getSimpleName();
    private static final String TAG_STATUS = "status";
    private static final String TAG_DATA = "data";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_kode_barang = "kode_barang";
    private static final String TAG_nama_barang = "nama_barang";
    private static final String TAG_kode_ruangan = "kode_ruangan";
    private static final String TAG_nama_ruangan = "nama_ruangan";

    ArrayList<String> TextJenisBarangs = new ArrayList<String>();
    ArrayList<String> ValueJenisBarangs = new ArrayList<String>();

    ArrayList<String> TextRuangans = new ArrayList<String>();
    ArrayList<String> ValueRuangans = new ArrayList<String>();

    DBController controller = new DBController(this);

    final Calendar myCalendar = Calendar.getInstance();

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etTanggal.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_masuk_edit);

        etTanggal = findViewById(R.id.eebmTanggal);
        etJumlah = findViewById(R.id.eebmJumlah);

        spinJenisBarang = findViewById(R.id.spinEJenisBarang);
        spinRuangan = findViewById(R.id.spinERuangan);

        simpanBtn = findViewById(R.id.btnEditBarangMasuk);
        batalBtn = findViewById(R.id.btnBatalEditBarangMasuk);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(BarangMasukEditActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DropDownListJenisBarang();
        DropDownListRuangan();

        Bundle bundle = getIntent().getExtras();
        id_arus = bundle.getInt("kunci_id_arus");
        tanggal = bundle.getString("kunci_tanggal");
        kode_barang = bundle.getString("kunci_kode_barang");
        kode_ruangan = bundle.getString("kunci_kode_ruangan");
        jumlah = bundle.getInt("kunci_jumlah");

        etTanggal.setText(tanggal);
        spinJenisBarang.setSelection(ValueJenisBarangs.indexOf(String.valueOf(kode_barang)));
        spinRuangan.setSelection(ValueRuangans.indexOf(String.valueOf(kode_ruangan)));
        etJumlah.setText(String.valueOf(jumlah));

        simpanBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SimpanData();
            }
        });

        batalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarangMasukEditActivity.this, BarangMasukActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void DropDownListJenisBarang(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jArr = new StringRequest(url_spinner_jenisbarang, new Response.Listener<String>() {
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
                            TextJenisBarangs.add(obj.getString(TAG_kode_barang)+" - "+obj.getString(TAG_nama_barang));
                            ValueJenisBarangs.add(obj.getString(TAG_kode_barang));
                        }
                        ArrayAdapter<String> arrayAdapterBarang = new ArrayAdapter<String>(BarangMasukEditActivity.this,android.R.layout.simple_spinner_item, TextJenisBarangs);
                        arrayAdapterBarang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinJenisBarang.setAdapter(arrayAdapterBarang);
                    } else {
                        Toast.makeText(BarangMasukEditActivity.this, "Gagal : "+jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error : "+error.getMessage());
                error.printStackTrace();
                Toast.makeText(BarangMasukEditActivity.this, "gagal", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jArr);
    }

    public void DropDownListRuangan(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jArr = new StringRequest(url_spinner_ruangan, new Response.Listener<String>() {
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
                            TextRuangans.add(obj.getString(TAG_kode_ruangan)+" - "+obj.getString(TAG_nama_ruangan));
                            ValueRuangans.add(obj.getString(TAG_kode_ruangan));
                        }
                        ArrayAdapter<String> arrayAdapterRuangan = new ArrayAdapter<String>(BarangMasukEditActivity.this,android.R.layout.simple_spinner_item, TextRuangans);
                        arrayAdapterRuangan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinRuangan.setAdapter(arrayAdapterRuangan);
                    } else {
                        Toast.makeText(BarangMasukEditActivity.this, "Gagal : "+jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error : "+error.getMessage());
                error.printStackTrace();
                Toast.makeText(BarangMasukEditActivity.this, "gagal", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jArr);
    }

    public void SimpanData(){
        if(etTanggal.getText().toString().equals("") || etJumlah.getText().toString().equals("") || spinJenisBarang.getSelectedItem().toString().equals("") || spinRuangan.getSelectedItem().toString().equals("")){
            Toast.makeText(BarangMasukEditActivity.this, "Semua harus diisi data", Toast.LENGTH_SHORT).show();
        }else{
            tanggal = etTanggal.getText().toString();
            kode_barang = ValueJenisBarangs.get(spinJenisBarang.getSelectedItemPosition());
            kode_ruangan = ValueRuangans.get(spinRuangan.getSelectedItemPosition());
            jumlah = Integer.valueOf(etJumlah.getText().toString());

            HashMap<String, String> loggedUser = controller.findData();
            id_user = Integer.valueOf(loggedUser.get("id_user"));

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response : " + response);
                    try {
                        JSONObject jobj = new JSONObject(response);
                        status = jobj.getInt(TAG_STATUS) == 1 ? true : false;
                        if (status) {
                            Toast.makeText(BarangMasukEditActivity.this, "sukses simpan data", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BarangMasukEditActivity.this, BarangMasukActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(BarangMasukEditActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : "+error.getMessage());
                    Toast.makeText(BarangMasukEditActivity.this, "Gagal simpan data", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("id_arus", String.valueOf(id_arus));
                    params.put("kode_barang", kode_barang);
                    params.put("kode_ruangan", kode_ruangan);
                    params.put("tanggal", tanggal);
                    params.put("jumlah", String.valueOf(jumlah));

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}