package com.example.remindmethereapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StandortActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standort2);


        Button Ebutton = (Button) findViewById(R.id.wähleStandort);
        Button Sbutton = (Button) findViewById(R.id.makiereStandort);
        Button Tbutton = (Button) findViewById(R.id.teileStandort);

        Ebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(StandortActivity2.this, MapActivity.class);
                startActivity(int1);
            }
        });

         Sbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent int2 = new Intent(StandortActivity2.this, MainActivity.class);
        startActivity(int2);

            }
        });

        Tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int3 = new Intent(StandortActivity2.this, teileStandort.class);
                startActivity(int3);
            }
        });
    }
    }





