package com.example.remindmethereapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button Ebutton=(Button)findViewById(R.id.Erinnerung_btn);
        Button Sbutton =(Button)findViewById(R.id.Standort_btn);

        Ebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(StartActivity.this,dateActivity.class);
                startActivity(int1);
            }
        });

        Sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int2 = new Intent(StartActivity.this, StandortActivity2.class);
                startActivity(int2);

            }
        });
    }
}