package com.example.remindmethereapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.rtp.AudioStream;
import android.os.Bundle;
import android.os.Vibrator;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.remindmethereapp.R;

import java.io.IOException;

public class AlarmScreenActivity extends AppCompatActivity {
    private GeoAlarm geoAlarm;
    private MediaPlayer player;
    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_alarm_screen);
        geoAlarm = (GeoAlarm) getIntent().getSerializableExtra("geoAlarm");


        AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, mgr.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
        this.setVolumeControlStream(AudioManager.STREAM_ALARM);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        TextView name, message;
        name = (TextView) findViewById(R.id.show_name);
        message = (TextView) findViewById(R.id.show_message);

        if (geoAlarm.getName() != "") {
            name.setText(" at " + geoAlarm.getName());
        } else {
            name.setText("" + geoAlarm.getName());
        }
        message.setText("" + geoAlarm.getMessage());

        if (geoAlarm.getVibration()) {
            long[] pattern = {0, 1000, 100};
            v.vibrate(pattern, 0);
        }

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_ALARM);
        try {
            player.setDataSource(this,Uri.parse(geoAlarm.getRingtoneUri()));
            player.setLooping(true);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void offAlarm(View view) {
        geoAlarm.setStatus(false);
        AlarmDatabase database = new AlarmDatabase(getApplicationContext());
        database.updateData(geoAlarm);
        while (player.isPlaying()) {
            player.stop();
        }
        v.cancel();

        startService(new Intent(getApplicationContext(), GeoService.class));
        finish();
    }

    @Override
    public void onBackPressed() {

    }

}