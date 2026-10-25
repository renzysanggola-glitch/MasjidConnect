package com.example.masjidconnect;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class KeuanganAdapter extends RecyclerView.Adapter<KeuanganAdapter.ViewHolder> {

    private List<HashMap<String, String>> listKeuangan;

    public KeuanganAdapter(List<HashMap<String, String>> listKeuangan) {
        this.listKeuangan = listKeuangan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_keuangan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> item = listKeuangan.get(position);

        holder.tvJudul.setText(item.get("judul"));
        holder.tvTanggal.setText(item.get("tanggal"));

        double jumlah = Double.parseDouble(item.get("jumlah"));
        String tipe = item.get("tipe");

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        if ("pemasukan".equalsIgnoreCase(tipe)) {
            holder.tvJumlah.setText("+" + formatRupiah.format(jumlah));
            holder.tvJumlah.setTextColor(Color.parseColor("#2E7D32"));
            holder.tvTipe.setText("Pemasukan");
            holder.tvTipe.setTextColor(Color.parseColor("#2E7D32"));
            holder.tvTipe.setBackgroundColor(Color.parseColor("#E8F5E9"));
            holder.ivIcon.setImageResource(android.R.drawable.ic_input_add);
        } else {
            holder.tvJumlah.setText("-" + formatRupiah.format(jumlah));
            holder.tvJumlah.setTextColor(Color.parseColor("#C62828"));
            holder.tvTipe.setText("Pengeluaran");
            holder.tvTipe.setTextColor(Color.parseColor("#C62828"));
            holder.tvTipe.setBackgroundColor(Color.parseColor("#FFEBEE"));
            holder.ivIcon.setImageResource(android.R.drawable.ic_delete);
        }
    }

    @Override
    public int getItemCount() {
        return listKeuangan != null ? listKeuangan.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvTanggal, tvJumlah, tvTipe;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_item_keu_judul);
            tvTanggal = itemView.findViewById(R.id.tv_item_keu_tanggal);
            tvJumlah = itemView.findViewById(R.id.tv_item_keu_jumlah);
            tvTipe = itemView.findViewById(R.id.tv_item_keu_tipe);
            ivIcon = itemView.findViewById(R.id.iv_icon_tipe);
        }
    }
}