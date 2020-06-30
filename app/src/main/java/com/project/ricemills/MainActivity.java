package com.project.ricemills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.project.ricemills.akun.Sign_In;
import com.project.ricemills.grafik.Grafik_Pemasokan;
import com.project.ricemills.grafik.Grafik_Penjualan;
import com.project.ricemills.pemasokan.Form_Pemasokan;
import com.project.ricemills.pemasokan.Pemasokan;
import com.project.ricemills.penggilingan.Penggilingan;
import com.project.ricemills.penjualan.Penjualan;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent i = new Intent(this, Sign_In.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
//        memulai timer splash screen
        timer.start();
    }
}
