package com.project.ricemills.beras.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ricemills.R;
import com.project.ricemills.beras.model.Stok_Model;
import com.project.ricemills.penggilingan.adapter.Adapter_Penggilingan;
import com.project.ricemills.penggilingan.model.Penggilingan_Model;

import java.util.ArrayList;

public class Adapter_Stok extends RecyclerView.Adapter<Adapter_Stok.ViewHolder> {
    private ArrayList<Stok_Model> listdata;
    private Activity activity;
    private Context context;

    public Adapter_Stok(Activity activity, ArrayList<Stok_Model> listdata, Context context) {
        this.listdata = listdata;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public Adapter_Stok.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_stok, parent, false);
        Adapter_Stok.ViewHolder vh = new Adapter_Stok.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(Adapter_Stok.ViewHolder holder, int position) {
        final Adapter_Stok.ViewHolder x = holder;
        holder.kode.setText(listdata.get(position).getKode());
        holder.kode_barang.setText(listdata.get(position).getKode_barang());
        holder.mContext = context;
        holder.kode.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView kode, kode_barang;
        ImageView cover;
        Context mContext;

        public ViewHolder(View v) {
            super(v);
            kode = (TextView) v.findViewById(R.id.kode);
            kode_barang = (TextView) v.findViewById(R.id.kode_barang);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    try {
//                        Intent intent;
//                        intent = new Intent(v.getContext(), Detail_Hewan.class);
//                        intent.putExtra("kode", kode.getText().toString());
//                        v.getContext().startActivity(intent);
//                    } catch (Exception e) {
//                        Log.d("pesan", "error");
//                    }
                }
            });
        }
    }
}

