package com.project.ricemills.beras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.ricemills.R;
import com.project.ricemills.config.AppController;
import com.project.ricemills.config.ServerAccess;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Form_Stok extends AppCompatActivity {
    ProgressDialog pd;
    Button simpan;
    String tgl = "";
    EditText stokk;
    TextView stok, kode_barang, ukuran_sak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_stok);
        stok = findViewById(R.id.stok);
        stokk = findViewById(R.id.stokk);
        kode_barang = findViewById(R.id.kode_barang);
        ukuran_sak = findViewById(R.id.ukuran_sak);
        Intent data = getIntent();
        stok.setText(data.getStringExtra("stok"));
        kode_barang.setText(data.getStringExtra("kode"));
        ukuran_sak.setText(data.getStringExtra("ukuran_sak"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        pd = new ProgressDialog(Form_Stok.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        simpan = findViewById(R.id.simpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
        simpan.setText("Update");
    }
    private void simpan(){
        pd.setMessage("Proses...");
        pd.setCancelable(false);
        pd.show();
        Intent data = getIntent();
        final String fkodebarang =data.getStringExtra("kode");

        final String fukuran_sak =data.getStringExtra("ukuran_sak");
        final String fstokk =stokk.getText().toString().trim();
        final String fstok =data.getStringExtra("stok");
        Log.d("pesan", "fstokk "+fstokk);
        Log.d("pesan", "fstok "+fstok);
        //mengecek username apakah kosong apa tidak. jika kosong maka akan menampilkan alert
        if (fstokk.isEmpty()) {
            Toast.makeText(getBaseContext(), "Stok Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            stokk.setFocusable(true);
            pd.dismiss();

        }else{
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.BERAS+"ApiProsesTambah", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.cancel();
                    try {
//                        fungsi ini berfungsi untuk mengubah string menjadi jsonObject
                        JSONObject res = new JSONObject(response);
                        Log.d("pesan", res.toString());
//                        mengecek data apakah null atau tidak jika tidak null maka akan di eksekusi di blok if dibawah ini
                        if (res.getString("stat").equals("true")) {
//                            berfungsi untuk mengambil object dengan nama data
//                            JSONArray d = r.getJSONArray("");
//                            menampilkan pesan jika login berhasil
                            Toast.makeText(getBaseContext(), "Berhasil Tambah Hasil Giling", Toast.LENGTH_SHORT).show();
//                            berganti halaman setelah login berhasil
                            finish();
                            pd.dismiss();
                        }else{
                            pd.dismiss();
                            Toast.makeText(Form_Stok.this, "Gagal Tambah Hasil Giling", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();

                    Log.e("errornyaa ", "" + error);
                    Toast.makeText(getBaseContext(), "Gagal Login, "+error, Toast.LENGTH_SHORT).show();


                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
//                    mengirim request username dan password ke api
                    params.put("kode_barang", fkodebarang);
                    params.put("stokk", fstokk);
                    params.put("stok", fstok);

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(senddata);
        }
    }
}
