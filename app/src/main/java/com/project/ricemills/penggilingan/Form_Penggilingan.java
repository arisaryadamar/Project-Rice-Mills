package com.project.ricemills.penggilingan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.util.Map;

public class Form_Penggilingan extends AppCompatActivity {
    ProgressDialog pd;
    Button simpan;
    String tgl = "";
    EditText Tanggal, Berat, Biaya_Penggilingan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_penggilingan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pd = new ProgressDialog(Form_Penggilingan.this);
        Tanggal = findViewById(R.id.Tanggal);
        Berat = findViewById(R.id.Berat);
        Biaya_Penggilingan = findViewById(R.id.Biaya_Penggilingan);
        simpan = findViewById(R.id.simpan);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        Tanggal.setText(formattedDate);
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
        StringRequest senddata = new StringRequest(Request.Method.GET, ServerAccess.PENGGILINGAN+"detail/"+kode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject data = res.getJSONObject("data");
                    Tanggal.setText(data.getString("Tanggal"));
                    tgl = data.getString("Tanggal");
                    Berat.setText(data.getString("Berat"));
                    Biaya_Penggilingan.setText(data.getString("Biaya_Penggilingan"));
                } catch (JSONException e) {
                    Toast.makeText(Form_Penggilingan.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    Log.d("pesan", "error "+e.getMessage());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Form_Penggilingan.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(senddata);
    }
    private void update(final String kode){
        pd.setMessage("Proses...");
        pd.setCancelable(false);
        pd.show();
        final String ftanggal =Tanggal.getText().toString().trim();
        final String fberat =Berat.getText().toString().trim();
        final String fbiaya =Biaya_Penggilingan.getText().toString().trim();
        //mengecek username apakah kosong apa tidak. jika kosong maka akan menampilkan alert
        if (ftanggal.isEmpty()) {
            Toast.makeText(getBaseContext(), "Tanggal Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            Tanggal.setFocusable(true);
            pd.dismiss();

        }else if (fberat.isEmpty()) {
            Toast.makeText(getBaseContext(), "Berat Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            Berat.setFocusable(true);
            pd.dismiss();

        }else if (fbiaya.isEmpty()) {
            Toast.makeText(getBaseContext(), "Biaya Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            Biaya_Penggilingan.setFocusable(true);
            pd.dismiss();

        }else{
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.PENGGILINGAN+"ApiUpdate", new Response.Listener<String>() {
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
                            Toast.makeText(getBaseContext(), "Berhasil Update", Toast.LENGTH_SHORT).show();
//                            berganti halaman setelah login berhasil
                            finish();
                            pd.dismiss();
                        }else{
                            pd.dismiss();
                            Toast.makeText(Form_Penggilingan.this, "Gagal Update", Toast.LENGTH_SHORT).show();
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
                    params.put("Tanggal", ftanggal);
                    params.put("Berat", fberat);
                    params.put("Biaya_Penggilingan", fbiaya);
                    params.put("id_penggilingan", kode);

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
        final String ftanggal =Tanggal.getText().toString().trim();
        final String fberat =Berat.getText().toString().trim();
        final String fbiaya =Biaya_Penggilingan.getText().toString().trim();
        //mengecek username apakah kosong apa tidak. jika kosong maka akan menampilkan alert
        if (ftanggal.isEmpty()) {
            Toast.makeText(getBaseContext(), "Tanggal Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            Tanggal.setFocusable(true);
            pd.dismiss();

        }else if (fberat.isEmpty()) {
            Toast.makeText(getBaseContext(), "Berat Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            Berat.setFocusable(true);
            pd.dismiss();

        }else if (fbiaya.isEmpty()) {
            Toast.makeText(getBaseContext(), "Biaya Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            Biaya_Penggilingan.setFocusable(true);
            pd.dismiss();

        }else{
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.PENGGILINGAN+"ApiInput", new Response.Listener<String>() {
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
                            Toast.makeText(getBaseContext(), "Berhasil Tambah Penggilingan", Toast.LENGTH_SHORT).show();
//                            berganti halaman setelah login berhasil
                            finish();
                            pd.dismiss();
                        }else{
                            pd.dismiss();
                            Toast.makeText(Form_Penggilingan.this, "Gagal Tambah Penggilingan", Toast.LENGTH_SHORT).show();
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
                    params.put("Tanggal", tgl);
                    params.put("Berat", fberat);
                    params.put("Biaya_Penggilingan", fbiaya);

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(senddata);
        }
    }
}
