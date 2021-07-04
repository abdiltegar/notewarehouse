package com.example.notewarehouse.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.notewarehouse.R;
import com.example.notewarehouse.app.AppController;
import com.example.notewarehouse.barangkeluar.BarangKeluarActivity;
import com.example.notewarehouse.barangkeluar.BarangKeluarEditActivity;
import com.example.notewarehouse.database.BarangKeluar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BarangKeluarAdapter extends RecyclerView.Adapter<BarangKeluarAdapter.BarangKeluarViewHolder>{
    private ArrayList<BarangKeluar> listData;
    Context context;
    Bundle bundle = new Bundle();

    static String hostname = "http://192.168.100.8";
    String url_delete = hostname+"/ws-notewarehouse/barang_keluar/delete.php";

    public BarangKeluarAdapter(ArrayList<BarangKeluar> listData) {
        this.listData = listData;
    }

    @Override
    public BarangKeluarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInf = LayoutInflater.from(parent.getContext());
        View view = layoutInf.inflate(R.layout.row_data_barang_keluar,parent,false);
        return new BarangKeluarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarangKeluarViewHolder holder, int position) {
        int id_arus, id_user, jumlah;
        String kode_barang, nama_barang, satuan, kode_ruangan, nama_ruangan, tanggal;

        id_arus = listData.get(position).getId();
        tanggal = listData.get(position).getTanggal();
        kode_barang = listData.get(position).getKodeBarang();
        nama_barang = listData.get(position).getNamaBarang();
        satuan = listData.get(position).getSatuan();
        jumlah = listData.get(position).getJumlah();
        kode_ruangan = listData.get(position).getKodeRuangan();
        nama_ruangan = listData.get(position).getNamaRuangan();

        holder.tvtanggal.setText(tanggal);
        holder.tvnamaBarang.setText(nama_barang+" - "+satuan);
        holder.tvjumlah.setText(String.valueOf(jumlah));
        holder.tvruangan.setText(nama_ruangan);

        holder.cardku.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                PopupMenu pm = new PopupMenu(v.getContext(), v);
                pm.inflate(R.menu.popupmenu);

                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menuedit:
                                Bundle bendel = new Bundle();
                                bendel.putInt("kunci_id_arus", id_arus);
                                bendel.putString("kunci_tanggal", tanggal);
                                bendel.putString("kunci_kode_barang", kode_barang);
                                bendel.putString("kunci_kode_ruangan", kode_ruangan);
                                bendel.putInt("kunci_jumlah", jumlah);

                                Intent inten = new Intent(v.getContext(), BarangKeluarEditActivity.class);
                                inten.putExtras(bendel);
                                v.getContext().startActivity(inten);
                                break;
                            case R.id.menuhapus:
                                AlertDialog.Builder alertdb = new AlertDialog.Builder(v.getContext());
                                alertdb.setTitle("Yakin barang keluar pada tanggal "+tanggal+" "+nama_barang+" sejumlah "+jumlah+" "+satuan+" akan dihapus ?");
                                alertdb.setMessage("Tekan Ya untuk menghapus");
                                alertdb.setCancelable(false);
                                alertdb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HapusData(String.valueOf(id_arus));
                                        Toast.makeText(v.getContext(), "Data barang keluar pada tanggal "+tanggal+" "+nama_barang+" sejumlah "+jumlah+" "+satuan+" telah dihapus", Toast.LENGTH_SHORT).show();
                                        Intent inten = new Intent(v.getContext(), BarangKeluarActivity.class);
                                        v.getContext().startActivity(inten);
                                    }
                                });
                                alertdb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog adlg = alertdb.create();
                                adlg.show();
                                break;
                        }
                        return true;
                    }
                });
                pm.show();
                return true;
            }
        });
    }

    private void HapusData(final String idx){
        final String TAG = BarangKeluarActivity.class.getSimpleName();
        final String TAG_STATUS = "status";
        final int[] sukses = new int[1];

        StringRequest stringReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon : " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    sukses[0] = jObj.getInt(TAG_STATUS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error : "+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id_arus", idx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringReq);
    }

    @Override
    public int getItemCount() {
        return listData != null ? listData.size() : 0;
    }

    public class BarangKeluarViewHolder extends RecyclerView.ViewHolder {
        private CardView cardku;
        private TextView tvtanggal, tvnamaBarang, tvjumlah, tvruangan;

        public BarangKeluarViewHolder(@NonNull View itemView) {
            super(itemView);
            cardku = itemView.findViewById(R.id.cardBarangKeluar);
            tvtanggal = itemView.findViewById(R.id.tRowBarangKeluarTanggal);
            tvnamaBarang = itemView.findViewById(R.id.tRowBarangKeluarNamaBarang);
            tvjumlah = itemView.findViewById(R.id.tRowBarangKeluarJumlah);
            tvruangan = itemView.findViewById(R.id.tRowBarangKeluarRuangan);
        }
    }
}
