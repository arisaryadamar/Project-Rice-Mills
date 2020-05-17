package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.loginpage.ui.InputPemasokan;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
    }
    public void Pemasokan(View view) {
        Intent intent = new Intent(HomeActivity.this, InputPemasokan.class);
        startActivity(intent);
    }
}
