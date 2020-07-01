package com.project.ricemills.pemasokan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.ricemills.R;
import com.project.ricemills.akun.Sign_In;
import com.project.ricemills.config.AppController;
import com.project.ricemills.config.AuthData;
import com.project.ricemills.config.ServerAccess;
import com.project.ricemills.dashboard.Dashboard;
import com.project.ricemills.pemasokan.model.Pemasokan_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Form_Pemasokan extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    ProgressDialog pd;
    Button simpan;
    String tgl = "";
    EditText tanggal, nama, no_hp, berat, alamat, harga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pemasokan);
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
        pd = new ProgressDialog(Form_Pemasokan.this);
        tanggal = findViewById(R.id.tanggal);
        nama = findViewById(R.id.nama);
        no_hp = findViewById(R.id.no_hp);
        berat = findViewById(R.id.berat);
        alamat = findViewById(R.id.alamat);
        harga = findViewById(R.id.harga);
        simpan = findViewById(R.id.simpan);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        tanggal.setText(formattedDate);
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
        StringRequest senddata = new StringRequest(Request.Method.GET, ServerAccess.PEMASOKAN+"detail/"+kode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject data = res.getJSONObject("data");
                    tanggal.setText(data.getString("tgl"));
                    tgl = data.getString("tgl");
                    nama.setText(data.getString("nama"));
                    no_hp.setText(data.getString("nmr"));
                    alamat.setText(data.getString("alamat"));
                    berat.setText(data.getString("berat"));
                    harga.setText(data.getString("harga"));
                } catch (JSONException e) {
                    Toast.makeText(Form_Pemasokan.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    Log.d("pesan", "error "+e.getMessage());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Form_Pemasokan.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(senddata);
    }
    private void update(final String kode){
        pd.setMessage("Proses...");
        pd.setCancelable(false);
        pd.show();
        final String ftanggal =tanggal.getText().toString().trim();
        final String fnama =nama.getText().toString().trim();
        final String fno_hp =no_hp.getText().toString().trim();
        final String fberat =berat.getText().toString().trim();
        final String fharga =harga.getText().toString().trim();
        final String falamat =alamat.getText().toString().trim();
        //mengecek username apakah kosong apa tidak. jika kosong maka akan menampilkan alert
        if (ftanggal.isEmpty()) {
            Toast.makeText(getBaseContext(), "Tanggal Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            tanggal.setFocusable(true);
            pd.dismiss();

        }else if (fnama.isEmpty()) {
            Toast.makeText(getBaseContext(), "Nama Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            nama.setFocusable(true);
            pd.dismiss();

        }else if (fno_hp.isEmpty()) {
            Toast.makeText(getBaseContext(), "No Hp Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            no_hp.setFocusable(true);
            pd.dismiss();

        }else if (fberat.isEmpty()) {
            Toast.makeText(getBaseContext(), "Berat Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            berat.setFocusable(true);
            pd.dismiss();

        }else if (fharga.isEmpty()) {
            Toast.makeText(getBaseContext(), "Harga Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            harga.setFocusable(true);
            pd.dismiss();

        }else if (falamat.isEmpty()) {
            Toast.makeText(getBaseContext(), "Alamat Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            alamat.setFocusable(true);
            pd.dismiss();

        }else{
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.PEMASOKAN+"ApiUpdate", new Response.Listener<String>() {
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
                            Toast.makeText(Form_Pemasokan.this, res.getString("message"), Toast.LENGTH_SHORT).show();
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
                    params.put("tgl", tgl);
                    params.put("nama", fnama);
                    params.put("nmr", fno_hp);
                    params.put("alamat", falamat);
                    params.put("berat", fberat);
                    params.put("harga", fharga);
                    params.put("id_pemasokan", kode);

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
        final String ftanggal =tanggal.getText().toString().trim();
        final String fnama =nama.getText().toString().trim();
        final String fno_hp =no_hp.getText().toString().trim();
        final String fberat =berat.getText().toString().trim();
        final String fharga =harga.getText().toString().trim();
        final String falamat =alamat.getText().toString().trim();
        //mengecek username apakah kosong apa tidak. jika kosong maka akan menampilkan alert
        if (ftanggal.isEmpty()) {
            Toast.makeText(getBaseContext(), "Tanggal Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            tanggal.setFocusable(true);
            pd.dismiss();

        }else if (fnama.isEmpty()) {
            Toast.makeText(getBaseContext(), "Nama Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            nama.setFocusable(true);
            pd.dismiss();

        }else if (fno_hp.isEmpty()) {
            Toast.makeText(getBaseContext(), "No Hp Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            no_hp.setFocusable(true);
            pd.dismiss();

        }else if (fberat.isEmpty()) {
            Toast.makeText(getBaseContext(), "Berat Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            berat.setFocusable(true);
            pd.dismiss();

        }else if (fharga.isEmpty()) {
            Toast.makeText(getBaseContext(), "Harga Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            harga.setFocusable(true);
            pd.dismiss();

        }else if (falamat.isEmpty()) {
            Toast.makeText(getBaseContext(), "Alamat Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            alamat.setFocusable(true);
            pd.dismiss();

        }else{
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.PEMASOKAN+"ApiInput", new Response.Listener<String>() {
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
                            Toast.makeText(getBaseContext(), "Berhasil Tambah Pemasokan", Toast.LENGTH_SHORT).show();
//                            berganti halaman setelah login berhasil
                            finish();
                            pd.dismiss();
                        }else{
                            pd.dismiss();
                            Toast.makeText(Form_Pemasokan.this, res.getString("message"), Toast.LENGTH_SHORT).show();
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
                    params.put("tgl", tgl);
                    params.put("nama", fnama);
                    params.put("nmr", fno_hp);
                    params.put("alamat", falamat);
                    params.put("berat", fberat);
                    params.put("harga", fharga);

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(senddata);
        }
    }

}
