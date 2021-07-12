package com.example.notewarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notewarehouse.database.DBController;
import com.example.notewarehouse.database.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText etLoginEmail, etLoginPassword;//deklarasi variable EditText
    Button btnLogin;//deklarasi variable Button
    String email, password, nama;//deklarasi variable String
    int id_user;//deklarasi variable int
    boolean statusLogin = false;//deklarasi variable boolean
    DBController controller = new DBController(this);//deklarasi variable controller menginisialisasi DBController

    static String hostname = "http://192.168.100.8";//variable hostname untuk menyimpan alamat ip web service
//    static String hostname = "http://10.20.14.90";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String url_select = hostname+"/ws-notewarehouse/akun/login.php";//alamat webservice untuk login
//    private static String url_select = "http://10.20.14.90/ws-notewarehouse/akun/login.php";
    //indeks dari webservice{
    public static final String TAG_STATUS = "status";
    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_ID = "id_user";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_EMAIL = "email";
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//menset view

        etLoginEmail = findViewById(R.id.etLoginEmail);//menghubungkan dengan EditText Email di view
        etLoginPassword = findViewById(R.id.etLoginPassword);//menghubungkan dengan EditText Password di view
        btnLogin = findViewById(R.id.btnLoginSubmit);//menghubungkan dengan Button Login di view

        btnLogin.setOnClickListener(new View.OnClickListener() {//event saat button login diklik
            @Override
            public void onClick(View v) {
                email = etLoginEmail.getText().toString();//mengambil nilai dari edittext email dan menyimpan di variable email
                password = etLoginPassword.getText().toString();//mengambil nilai dari edittext password dan menyimpan di variable password

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//membuat requestQueue
                StringRequest stringReq = new StringRequest(Request.Method.POST, url_select, new Response.Listener<String>() {//menentukan method dan url
                    @Override
                    public void onResponse(String response) {//respon dari webservice dimasukkan ke variable response
                        Log.d(TAG, "respon : " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);//mengubah response menjadi JSONObject
                            statusLogin = jObj.getInt(TAG_STATUS) == 1 ? true : false;//jika status dari webservice = 1 , maka statusLogin true
                            if (statusLogin) {//jika status = true maka
                                JSONObject arrayData = jObj.getJSONObject(TAG_DATA);
                                id_user = arrayData.getInt(TAG_ID);//memasukkan id_user dari ws
                                nama = arrayData.getString(TAG_NAMA);//memasukkan nama dari ws
                                email = arrayData.getString(TAG_EMAIL);//memasukkan email dari ws

                                HashMap<String, String> qvalues = new HashMap<>();// membuat hashmap untuk insert ke SQLite
                                qvalues.put("id_user",String.valueOf(id_user));
                                qvalues.put("nama",nama);
                                qvalues.put("email",email);

                                controller.insertData(qvalues);//memanggil insertData di DBController dengan parameter qvalues

                                if(!nama.isEmpty()){//jika nama kosong login gagal
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);//membuat intent pindah ke halaman Home
                                    startActivity(intent);//memulai intent
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Gagal Login . Pastikan username dan password anda benar", Toast.LENGTH_SHORT).show();//menampilkan notifikasi gagal login
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Gagal : "+jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();//menampilkan notifikasi gagal dengan message dari ws
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Gagal Login . Pastikan username dan password anda benar", Toast.LENGTH_SHORT).show();//menampilkan notifikasi gagal login
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error : "+error.getMessage());
                        Toast.makeText(LoginActivity.this, "Gagal login , Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){//membuat parameter untuk dikirim ke ws
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);

                        return params;
                    }
                };
                requestQueue.add(stringReq);

            }
        });
    }
}