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
    EditText etLoginEmail, etLoginPassword;
    Button btnLogin;
    String email, password, nama;
    int id_user;
    boolean statusLogin = false;
    DBController controller = new DBController(this);

    static String hostname = "http://192.168.100.8";
//    static String hostname = "http://10.20.14.90";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String url_select = hostname+"/ws-notewarehouse/akun/login.php";
//    private static String url_select = "http://10.20.14.90/ws-notewarehouse/akun/login.php";
    public static final String TAG_STATUS = "status";
    public static final String TAG_DATA = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_ID = "id_user";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLoginSubmit);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etLoginEmail.getText().toString();
                password = etLoginPassword.getText().toString();

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringReq = new StringRequest(Request.Method.POST, url_select, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "respon : " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            statusLogin = jObj.getInt(TAG_STATUS) == 1 ? true : false;
                            if (statusLogin) {
                                JSONObject arrayData = jObj.getJSONObject(TAG_DATA);
                                id_user = arrayData.getInt(TAG_ID);
                                nama = arrayData.getString(TAG_NAMA);
                                email = arrayData.getString(TAG_EMAIL);
//                                Toast.makeText(LoginActivity.this, "Sukses mengedit data", Toast.LENGTH_SHORT).show();

                                HashMap<String, String> qvalues = new HashMap<>();
                                qvalues.put("id_user",String.valueOf(id_user));
                                qvalues.put("nama",nama);
                                qvalues.put("email",email);

                                controller.insertData(qvalues);

                                if(!nama.isEmpty()){
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Gagal Login . Pastikan username dan password anda benar", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Gagal : "+jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Gagal Login . Pastikan username dan password anda benar", Toast.LENGTH_SHORT).show();
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
                    protected Map<String, String> getParams(){
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