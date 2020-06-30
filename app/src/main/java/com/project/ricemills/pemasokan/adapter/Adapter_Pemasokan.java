package com.project.ricemills.pemasokan.adapter;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.ricemills.R;
import com.project.ricemills.config.AppController;
import com.project.ricemills.config.ServerAccess;
import com.project.ricemills.pemasokan.Form_Pemasokan;
import com.project.ricemills.pemasokan.Pemasokan;
import com.project.ricemills.pemasokan.model.Pemasokan_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter_Pemasokan extends RecyclerView.Adapter<Adapter_Pemasokan.ViewHolder> {
    private ArrayList<Pemasokan_Model> listdata;
    private Activity activity;
    private Context context;

    public Adapter_Pemasokan(Activity activity, ArrayList<Pemasokan_Model> listdata, Context context) {
        this.listdata = listdata;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public Adapter_Pemasokan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_pemasokan, parent, false);
        Adapter_Pemasokan.ViewHolder vh = new Adapter_Pemasokan.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(Adapter_Pemasokan.ViewHolder holder, int position) {
        final Adapter_Pemasokan.ViewHolder x = holder;
        holder.kode.setText(listdata.get(position).getKode());
        holder.tgl.setText(listdata.get(position).getTanggal());
        holder.nama.setText(listdata.get(position).getNama());
        holder.nmr.setText(listdata.get(position).getTelp());
        holder.alamat.setText(listdata.get(position).getAlamat());
        holder.berat.setText(listdata.get(position).getBerat());
        holder.harga.setText(listdata.get(position).getHarga());
        holder.mContext = context;
        holder.kode.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView kode, tgl, nama, nmr, alamat, berat, harga, edit, delete;
        ImageView cover;
        Context mContext;

        public ViewHolder(View v) {
            super(v);
            kode = (TextView) v.findViewById(R.id.kode);
            tgl = (TextView) v.findViewById(R.id.tgl);
            nama = (TextView) v.findViewById(R.id.nama);
            nmr = (TextView) v.findViewById(R.id.nmr);
            alamat = (TextView) v.findViewById(R.id.alamat);
            berat = (TextView) v.findViewById(R.id.berat);
            harga = (TextView) v.findViewById(R.id.harga);
            edit = (TextView) v.findViewById(R.id.edit);
            delete = (TextView) v.findViewById(R.id.delete);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                        intent = new Intent(v.getContext(), Form_Pemasokan.class);
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
                StringRequest senddata = new StringRequest(Request.Method.GET, ServerAccess.PEMASOKAN+"ApiHapus/"+id, new Response.Listener<String>() {
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
                                intent = new Intent(context, Pemasokan.class);
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

