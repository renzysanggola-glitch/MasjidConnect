package com.example.masjidconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {

    private List<HashMap<String, String>> listAgenda;

    public AgendaAdapter(List<HashMap<String, String>> listAgenda) {
        this.listAgenda = listAgenda;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agenda_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> agenda = listAgenda.get(position);

        holder.tvJudul.setText(agenda.get("judul"));
        holder.tvPembicara.setText(agenda.get("pembicara"));
        holder.tvWaktu.setText("🕒 " + agenda.get("waktu"));
        holder.tvKategori.setText("📍 " + agenda.get("kategori"));

        // Memisah Tanggal dan Bulan untuk Badge Kiri
        String tglFull = agenda.get("tanggal"); // Format ekspektasi: "19 Sept 2026" atau "19"
        if (tglFull != null && !tglFull.isEmpty()) {
            String[] parts = tglFull.split(" ");
            if (parts.length >= 2) {
                holder.tvTgl.setText(parts[0]);
                holder.tvBulan.setText(parts[1].toUpperCase());
            } else {
                holder.tvTgl.setText(tglFull);
                holder.tvBulan.setText("AGENDA");
            }
        }
    }

    @Override
    public int getItemCount() {
        return listAgenda != null ? listAgenda.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvPembicara, tvWaktu, tvKategori, tvTgl, tvBulan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_item_judul);
            tvPembicara = itemView.findViewById(R.id.tv_item_pembicara);
            tvWaktu = itemView.findViewById(R.id.tv_item_waktu);
            tvKategori = itemView.findViewById(R.id.tv_item_kategori);
            tvTgl = itemView.findViewById(R.id.tv_item_tgl);
            tvBulan = itemView.findViewById(R.id.tv_item_bulan);
        }
    }
}