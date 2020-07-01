package com.project.ricemills.penggilingan.adapter;

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
import com.project.ricemills.config.AppController;
import com.project.ricemills.config.ServerAccess;
import com.project.ricemills.pemasokan.Form_Pemasokan;
import com.project.ricemills.pemasokan.Pemasokan;
import com.project.ricemills.penggilingan.Form_Penggilingan;
import com.project.ricemills.penggilingan.Penggilingan;
import com.project.ricemills.penggilingan.model.Penggilingan_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Adapter_Penggilingan extends RecyclerView.Adapter<Adapter_Penggilingan.ViewHolder> {
    private ArrayList<Penggilingan_Model> listdata;
    private Activity activity;
    private Context context;

    public Adapter_Penggilingan(Activity activity, ArrayList<Penggilingan_Model> listdata, Context context) {
        this.listdata = listdata;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public Adapter_Penggilingan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_penggilingan, parent, false);
        Adapter_Penggilingan.ViewHolder vh = new Adapter_Penggilingan.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(Adapter_Penggilingan.ViewHolder holder, int position) {
        final Adapter_Penggilingan.ViewHolder x = holder;
        holder.kode.setText(listdata.get(position).getKode());
        holder.Tanggal.setText(listdata.get(position).getTanggal());
        holder.Berat.setText(listdata.get(position).getBerat());
        holder.Biaya_Penggilingan.setText(listdata.get(position).getBiaya());
        holder.mContext = context;
        holder.kode.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView kode, Tanggal, Berat, Biaya_Penggilingan, edit, delete;
        ImageView cover;
        Context mContext;

        public ViewHolder(View v) {
            super(v);
            kode = (TextView) v.findViewById(R.id.kode);
            Tanggal = (TextView) v.findViewById(R.id.Tanggal);
            Berat = (TextView) v.findViewById(R.id.Berat);
            Biaya_Penggilingan = (TextView) v.findViewById(R.id.Biaya_Penggilingan);
            edit = (TextView) v.findViewById(R.id.edit);
            delete = (TextView) v.findViewById(R.id.delete);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(v.getContext(), Form_Penggilingan.class);
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
            StringRequest senddata = new StringRequest(Request.Method.GET, ServerAccess.PENGGILINGAN+"ApiHapus/"+id, new Response.Listener<String>() {
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
                            Toast.makeText(context, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show();
//                            berganti halaman setelah login berhasil
                            Intent intent;
                            intent = new Intent(context, Penggilingan.class);
                            context.startActivity(intent);
                        }else{
                            Toast.makeText(context, " Hapus Data gagal", Toast.LENGTH_SHORT).show();
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

