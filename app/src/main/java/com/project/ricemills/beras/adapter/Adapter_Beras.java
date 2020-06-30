package com.project.ricemills.beras.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.ricemills.R;
import com.project.ricemills.beras.Beras;
import com.project.ricemills.beras.Form_Beras;
import com.project.ricemills.beras.Form_Stok;
import com.project.ricemills.beras.model.Beras_Model;
import com.project.ricemills.beras.model.Stok_Model;
import com.project.ricemills.config.AppController;
import com.project.ricemills.config.ServerAccess;
import com.project.ricemills.pemasokan.Form_Pemasokan;
import com.project.ricemills.pemasokan.Pemasokan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Adapter_Beras extends RecyclerView.Adapter<Adapter_Beras.ViewHolder> {
    private ArrayList<Beras_Model> listdata;
    private Activity activity;
    private Context context;

    public Adapter_Beras(Activity activity, ArrayList<Beras_Model> listdata, Context context) {
        this.listdata = listdata;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public Adapter_Beras.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_beras, parent, false);
        Adapter_Beras.ViewHolder vh = new Adapter_Beras.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(Adapter_Beras.ViewHolder holder, int position) {
        final Adapter_Beras.ViewHolder x = holder;
        holder.kode.setText(listdata.get(position).getKode());
        holder.kode_barang.setText(listdata.get(position).getKode_barang());
        holder.ukuran_sak.setText(listdata.get(position).getUkuran_sak());
        holder.stok.setText(listdata.get(position).getStok());
        holder.mContext = context;
        holder.kode.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView kode, kode_barang, ukuran_sak, stok, edit, delete, tambah_stok;
        ImageView cover;
        Context mContext;

        public ViewHolder(View v) {
            super(v);
            kode = (TextView) v.findViewById(R.id.kode);
            kode_barang = (TextView) v.findViewById(R.id.kode_barang);
            ukuran_sak = (TextView) v.findViewById(R.id.ukuran_sak);
            stok = (TextView) v.findViewById(R.id.stok);
            edit = (TextView) v.findViewById(R.id.edit);
            delete = (TextView) v.findViewById(R.id.delete);
            tambah_stok = (TextView) v.findViewById(R.id.tambah_stok);
            tambah_stok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(v.getContext(), Form_Stok.class);
                    intent.putExtra("edit", true);
                    intent.putExtra("kode", kode.getText().toString());
                    intent.putExtra("ukuran_sak", ukuran_sak.getText().toString());
                    intent.putExtra("stok", stok.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(v.getContext(), Form_Beras.class);
                    intent.putExtra("edit", true);
                    intent.putExtra("kode", kode.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context c = v.getContext();
                    new AlertDialog.Builder(v.getContext())
                            .setIcon(R.drawable.logo)
                            .setTitle("Hapus Data")
                            .setMessage("Apakah Anda Hapus Data ?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    hapus(c,  kode.getText().toString());
                                }
                            })
                            .setNegativeButton("Tidak", null)
                            .show();

                }
            });
        }
        private void hapus(final Context context, String id){
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.GET, ServerAccess.BERAS+"ApiHapus/"+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
//                        fungsi ini berfungsi untuk mengubah string menjadi jsonObject
                        JSONObject res = new JSONObject(response);
                        Log.d("pesan", res.toString());
//                        mengecek data apakah null atau tidak jika tidak null maka akan di eksekusi di blok if dibawah ini
                        if (res.getString("stat").equals("true")) {
//                            berfungsi untuk mengambil object dengan nama data
//                            JSONArray d = r.getJSONArray("");
//                            menampilkan pesan jika login berhasil
                            Toast.makeText(context, "Berhasil Hapus Pemasokan", Toast.LENGTH_SHORT).show();
//                            berganti halaman setelah login berhasil
                            Intent intent;
                            intent = new Intent(context, Beras.class);
                            intent.putExtra("kode", kode.getText().toString());
                            context.startActivity(intent);
                        }else{
                            Toast.makeText(context, " Hapus Pemasokan gagal", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("errornyaa ", "" + error);
                    Toast.makeText(context, "Gagal Login, "+error, Toast.LENGTH_SHORT).show();


                }
            });

            AppController.getInstance().addToRequestQueue(senddata);
        }
        }
}

