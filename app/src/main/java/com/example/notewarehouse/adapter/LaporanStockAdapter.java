package com.example.notewarehouse.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.example.notewarehouse.R;
import com.example.notewarehouse.database.LaporanStock;

import java.util.ArrayList;

public class LaporanStockAdapter extends RecyclerView.Adapter<LaporanStockAdapter.LaporanStockViewHolder>{
    private ArrayList<LaporanStock> listData;
    Context context;
    Bundle bundle = new Bundle();

    public LaporanStockAdapter(ArrayList<LaporanStock> listData) {
        this.listData = listData;
    }

    @Override
    public LaporanStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInf = LayoutInflater.from(parent.getContext());
        View view = layoutInf.inflate(R.layout.row_data_laporan_stok,parent,false);
        return new LaporanStockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanStockViewHolder holder, int position) {
        String kd, nm, st;
        int jml;

        kd = listData.get(position).getId();
        nm = listData.get(position).getNamaBarang();
        st = listData.get(position).getSatuan();
        jml = listData.get(position).getJumlah();

        holder.tvkodeBarang.setText(kd);
        holder.tvnamaBarang.setText(nm);
        holder.tvsatuan.setText(st);
        holder.tvjumlah.setText(String.valueOf(jml));
    }

    @Override
    public int getItemCount() {
        return listData != null ? listData.size() : 0;
    }

    public class LaporanStockViewHolder extends RecyclerView.ViewHolder {
        private CardView cardku;
        private TextView tvkodeBarang, tvnamaBarang, tvsatuan, tvjumlah;

        public LaporanStockViewHolder(@NonNull View itemView) {
            super(itemView);
            cardku = itemView.findViewById(R.id.cardLaporanStock);
            tvkodeBarang = itemView.findViewById(R.id.tRowLaporanStokKodeBarang);
            tvnamaBarang = itemView.findViewById(R.id.tRowLaporanStokNamaBarang);
            tvsatuan = itemView.findViewById(R.id.tRowLaporanStokSatuan);
            tvjumlah = itemView.findViewById(R.id.tRowLaporanStokJumlah);
        }
    }
}
