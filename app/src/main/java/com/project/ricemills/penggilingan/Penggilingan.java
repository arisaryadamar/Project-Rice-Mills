package com.project.ricemills.penggilingan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.ricemills.R;
import com.project.ricemills.config.AppController;
import com.project.ricemills.config.ServerAccess;
import com.project.ricemills.penggilingan.adapter.Adapter_Penggilingan;
import com.project.ricemills.penggilingan.model.Penggilingan_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Penggilingan extends AppCompatActivity {
    private Adapter_Penggilingan adapter;
    private List<Penggilingan_Model> list;
    private RecyclerView listdata;
    RecyclerView.LayoutManager mManager;
    FloatingActionButton tambah;
    SwipeRefreshLayout swLayout;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    Button tanggal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penggilingan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listdata = (RecyclerView) findViewById(R.id.listdata);
        listdata.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new Adapter_Penggilingan(this,(ArrayList<Penggilingan_Model>) list, this);
        mManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        listdata.setLayoutManager(mManager);
        listdata.setAdapter(adapter);
        tambah = findViewById(R.id.tambah);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Form_Penggilingan.class));
            }
        });
        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);
        swLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark);
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload("");
            }
        });
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tanggal = findViewById(R.id.tanggal);
        tanggal = findViewById(R.id.tanggal);
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanTanggal();
            }
        });
        loadJson("");
    }
    private void tampilkanTanggal() {

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tanggal.setText("Tanggal :"+dateFormatter.format(newDate.getTime()));
                reload(dateFormatter.format(newDate.getTime()));
                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
    public void reload(String tanggal){
//        not_found.setVisibility(View.GONE);
        list.clear();
        loadJson(tanggal); // your code
        listdata.getAdapter().notifyDataSetChanged();
        swLayout.setRefreshing(false);
    }
    private void loadJson(String tanggal)
    {
        Intent data = getIntent();
        String link = ServerAccess.PENGGILINGAN+"api";
        if(tanggal.isEmpty()){
            link = ServerAccess.PENGGILINGAN+"api";
        }else{
            link = ServerAccess.PENGGILINGAN+"filter/"+tanggal;
        }
        StringRequest senddata = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray res = null;
                try {
                    res = new JSONArray(response);
//                    JSONArray arr = res.getJSONArray("data");
                    if(res.length() > 0) {
                        for (int i = 0; i < res.length(); i++) {
                            try {
                                JSONObject data = res.getJSONObject(i);
                                Penggilingan_Model md = new Penggilingan_Model();
                                md.setKode(data.getString("id_penggilingan"));
                                md.setTanggal(data.getString("Tanggal"));
                                md.setBerat(data.getString("Berat"));
                                md.setBiaya(ServerAccess.numberConvert(data.getString("Biaya_Penggilingan")));
                                list.add(md);
                            } catch (Exception ea) {
                                ea.printStackTrace();
                                Log.d("pesan", ea.getMessage());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(Penggilingan.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Penggilingan.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    Log.d("pesan", "error "+e.getMessage());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Penggilingan.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(senddata);
    }
}
