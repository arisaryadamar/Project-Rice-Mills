package com.project.ricemills.beras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.ricemills.R;
import com.project.ricemills.config.AppController;
import com.project.ricemills.config.ServerAccess;
import com.project.ricemills.pemasokan.Form_Pemasokan;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Form_Beras extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    ProgressDialog pd;
    Button simpan;
    String tgl = "";
    EditText kode_barang, ukuran_sak, stok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_beras);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        pd = new ProgressDialog(Form_Beras.this);
        kode_barang = findViewById(R.id.kode_barang);
        ukuran_sak = findViewById(R.id.ukuran_sak);
        stok = findViewById(R.id.stok);
        simpan = findViewById(R.id.simpan);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        tgl = formattedDate;
        final Intent data = getIntent();
        if (data.hasExtra("edit")){
            simpan.setText("Ubah");
            loadJson(data.getStringExtra("kode"));
        }
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.hasExtra("edit")){
                    update(data.getStringExtra("kode"));
                }else{
                    simpan();
                }
            }
        });
    }
    private void loadJson(String kode)
    {
        Intent data = getIntent();
        StringRequest senddata = new StringRequest(Request.Method.GET, ServerAccess.BERAS+"detail/"+kode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject data = res.getJSONObject("data");
                    kode_barang.setText(data.getString("kode_barang"));
                    ukuran_sak.setText(data.getString("ukuran_sak"));
                    stok.setText(data.getString("stok"));
                } catch (JSONException e) {
                    Toast.makeText(Form_Beras.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    Log.d("pesan", "error "+e.getMessage());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Form_Beras.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(senddata);
    }
    private void update(final String kode){
        pd.setMessage("Proses...");
        pd.setCancelable(false);
        pd.show();
        final String fkodebarang =kode_barang.getText().toString().trim();
        final String fukuran_sak =ukuran_sak.getText().toString().trim();
        final String fstok =stok.getText().toString().trim();
        //mengecek username apakah kosong apa tidak. jika kosong maka akan menampilkan alert
        if (fkodebarang.isEmpty()) {
            Toast.makeText(getBaseContext(), "Kode Barang Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            kode_barang.setFocusable(true);
            pd.dismiss();

        }else if (fukuran_sak.isEmpty()) {
            Toast.makeText(getBaseContext(), "Ukuran Sak Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            ukuran_sak.setFocusable(true);
            pd.dismiss();

        }else if (fstok.isEmpty()) {
            Toast.makeText(getBaseContext(), "Stok Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            stok.setFocusable(true);
            pd.dismiss();

        }else{
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.BERAS+"ApiUpdate", new Response.Listener<String>() {
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
                            Toast.makeText(getBaseContext(), "Berhasil Update Pemasokan", Toast.LENGTH_SHORT).show();
//                            berganti halaman setelah login berhasil
                            finish();
                            pd.dismiss();
                        }else{
                            pd.dismiss();
                            Toast.makeText(Form_Beras.this, res.getString("message"), Toast.LENGTH_SHORT).show();
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
                    params.put("kode_barang", kode);
                    params.put("ukuran_sak", fukuran_sak);
                    params.put("stok", fstok);

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(senddata);
        }
    }
    private void simpan(){
        pd.setMessage("Proses...");
        pd.setCancelable(false);
        pd.show();
        final String fkodebarang =kode_barang.getText().toString().trim();
        final String fukuran_sak =ukuran_sak.getText().toString().trim();
        final String fstok =stok.getText().toString().trim();
        //mengecek username apakah kosong apa tidak. jika kosong maka akan menampilkan alert
        if (fkodebarang.isEmpty()) {
            Toast.makeText(getBaseContext(), "Kode Barang Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            kode_barang.setFocusable(true);
            pd.dismiss();

        }else if (fukuran_sak.isEmpty()) {
            Toast.makeText(getBaseContext(), "Ukuran Sak Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            ukuran_sak.setFocusable(true);
            pd.dismiss();

        }else if (fstok.isEmpty()) {
            Toast.makeText(getBaseContext(), "Stok Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            stok.setFocusable(true);
            pd.dismiss();

        }else{
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.BERAS+"ApiInput", new Response.Listener<String>() {
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
                            Toast.makeText(Form_Beras.this, "Gagal Tambah Hasil Giling", Toast.LENGTH_SHORT).show();
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
                    params.put("ukuran_sak", fukuran_sak);
                    params.put("stok", fstok);

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(senddata);
        }
    }
}
