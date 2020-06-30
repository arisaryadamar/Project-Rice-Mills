package com.project.ricemills.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.project.ricemills.R;
import com.project.ricemills.akun.Sign_In;
import com.project.ricemills.beras.Beras;
import com.project.ricemills.beras.Stok;
import com.project.ricemills.config.AuthData;
import com.project.ricemills.grafik.Grafik;
import com.project.ricemills.pemasokan.Pemasokan;
import com.project.ricemills.penggilingan.Penggilingan;
import com.project.ricemills.penjualan.Penjualan;

public class Dashboard extends AppCompatActivity {
    LinearLayout pemasokan, penggilingan, penjualan, stok, grafik, hasil, keluar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        pemasokan = findViewById(R.id.pemasokan);
        penggilingan = findViewById(R.id.penggilingan);
        penjualan = findViewById(R.id.penjualan);
        stok = findViewById(R.id.stok);
        hasil = findViewById(R.id.hasil);
        keluar = findViewById(R.id.keluar);
        grafik = findViewById(R.id.grafik);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthData.getInstance(getBaseContext()).logout();
                finish();
                startActivity(new Intent(getBaseContext(), Sign_In.class));
            }
        });
        hasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Beras.class));
            }
        });
        pemasokan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Pemasokan.class));
            }
        });
        penggilingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Penggilingan.class));
            }
        });
        penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Penjualan.class));
            }
        });
        stok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Stok.class));
            }
        });
        grafik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Grafik.class));
            }
        });

        if (AuthData.getInstance(getBaseContext()).getlevel().equals("2")){
            penjualan.setVisibility(View.GONE);
            grafik.setVisibility(View.GONE);
        }else if (AuthData.getInstance(getBaseContext()).getlevel().equals("3")){
            pemasokan.setVisibility(View.GONE);
            penggilingan.setVisibility(View.GONE);
            stok.setVisibility(View.GONE);
            grafik.setVisibility(View.GONE);
            hasil.setVisibility(View.GONE);
        }

    }
}
