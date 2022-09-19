package com.example.remindmethereapp;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 12;
    static AlarmDatabase alarmDatabase;
    final Context context = MainActivity.this;
    final private int REQUEST_CODE = 1;
    double lati, lang;
    RecyclerView mRecyclerView;
    private GeoAlarmAdapter mAdapter;
    static public ArrayList<GeoAlarm> mAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setVolumeControlStream(AudioManager.STREAM_ALARM);



        FloatingActionButton setAlarm = (FloatingActionButton) findViewById(R.id.set_alarm);

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        mAlarms = new ArrayList<GeoAlarm>();

        mRecyclerView = (RecyclerView) findViewById(R.id.alarm_list);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new GeoAlarmAdapter(mAlarms);
        mRecyclerView.setAdapter(mAdapter);
        //Adds horizontal bar after each item
        /*
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        */
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this));



        alarmDatabase = new AlarmDatabase(getApplicationContext());

        showAlarms();
        stopService(new Intent(this, GeoService.class));
        startService(new Intent(this, GeoService.class));

    }


    public void setAlarm(String name, LocationCoordiante location, boolean vibrate,
                         String ringtone, String ringtoneName, int range, String message) {
        GeoAlarm geoAlarm = new GeoAlarm(name, location, vibrate, ringtone, ringtoneName, range, message);
        geoAlarm.setStatus(true);
        alarmDatabase.insertData(geoAlarm);
        geoAlarm.setmId(alarmDatabase.getId());
        mAlarms.add(geoAlarm);
        mAdapter.addItem(mAdapter.getItemCount());
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((GeoAlarmAdapter) mAdapter).setOnItemClickListener(
                new GeoAlarmAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(final int position, View v) {
                        //On click event for row items


                        View view = findViewById(R.id.activity_main);
                        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.location_alarm_dialog, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                        alertDialogBuilder.setView(promptsView);

                        final EditText userInput = (EditText) promptsView.findViewById(R.id.alarm_name_input);
                        TextView locationShow = (TextView) promptsView.findViewById(R.id.location_coordinates);
                        final Spinner ringtoneSelect = (Spinner) promptsView.findViewById(R.id.ringtone);
                        final CheckBox vibration = (CheckBox) promptsView.findViewById(R.id.vibration);
                        vibration.setChecked(mAlarms.get(position).getVibration());
                        locationShow.setText(mAlarms.get(position).getLocationCoordinate());
                        userInput.setText(mAlarms.get(position).getName());
                        final EditText range = (EditText) promptsView.findViewById(R.id.range);
                        range.setText("" + mAlarms.get(position).getRadius());
                        final EditText message = (EditText) promptsView.findViewById(R.id.message);
                        message.setText("" + mAlarms.get(position).getMessage());


                        final Map<String, String> ringtones = new HashMap<>();
                        RingtoneManager manager = new RingtoneManager(MainActivity.this);
                        manager.setType(RingtoneManager.TYPE_ALARM);
                        Cursor cursor = manager.getCursor();
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            ringtones.put(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX),
                                    manager.getRingtoneUri(cursor.getPosition()).toString());
                            cursor.moveToNext();
                        }

                        final ArrayList<String> ringtoneNames = new ArrayList<String>();
                        for (Map.Entry<String, String> entry : ringtones.entrySet()) {
                            ringtoneNames.add(entry.getKey());
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, ringtoneNames);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ringtoneSelect.setAdapter(dataAdapter);


                        alertDialogBuilder
                                .setCancelable(true)
                                .setPositiveButton("SAVE",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //Sets the alarm. Code needs to be entered
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        final AlertDialog alertDialog = alertDialogBuilder.create();


                        alertDialog.show();
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (range.getText().toString().equals("")) {
                                    Toast.makeText(MainActivity.this, "Gib eine Entfernung ein ", Toast.LENGTH_SHORT).show();
                                } else if (Integer.parseInt(range.getText().toString()) < 50) {
                                    Toast.makeText(MainActivity.this, "Die Entfernung soll gleich oder über 50m liegen", Toast.LENGTH_SHORT).show();
                                } else {
                                    mAlarms.get(position).setName(userInput.getText().toString());
                                    mAlarms.get(position).setRingtone(ringtoneSelect.getSelectedItem().toString(),
                                            ringtones.get(ringtoneSelect.getSelectedItem()));
                                    mAlarms.get(position).setVibration(vibration.isChecked());
                                    mAlarms.get(position).setRadius(Integer.parseInt(range.getText().toString()));
                                    mAlarms.get(position).setMessage("" + message.getText());
                                    mAdapter.refreshItem(position);
                                    alertDialog.dismiss();
                                }
                            }
                        });

                    }
                }


        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Intent", "" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {


                lati = data.getDoubleExtra("latitude", 0);
                lang = data.getDoubleExtra("longitude", 0);


                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.location_alarm_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.alarm_name_input);
                TextView locationShow = (TextView) promptsView.findViewById(R.id.location_coordinates);
                final EditText range = (EditText) promptsView.findViewById(R.id.range);
                final Spinner ringtoneSelect = (Spinner) promptsView.findViewById(R.id.ringtone);
                final CheckBox vibration = (CheckBox) promptsView.findViewById(R.id.vibration);
                final EditText message = (EditText) promptsView.findViewById(R.id.message);
                userInput.setText(data.getStringExtra("address"));


                final Map<String, String> ringtones = new HashMap<>();
                RingtoneManager manager = new RingtoneManager(MainActivity.this);
                manager.setType(RingtoneManager.TYPE_ALARM);
                Cursor cursor = manager.getCursor();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    ringtones.put(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX),
                            manager.getRingtoneUri(cursor.getPosition()).toString());
                    cursor.moveToNext();
                }


                final ArrayList<String> ringtoneNames = new ArrayList<String>();
                for (Map.Entry<String, String> entry : ringtones.entrySet()) {
                    ringtoneNames.add(entry.getKey());
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, ringtoneNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ringtoneSelect.setAdapter(dataAdapter);


                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Sets the alarm. Code needs to be entered

                                    }

                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog

                final AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (range.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this, "Gib eine Entfernung ein ", Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(range.getText().toString()) < 50) {
                            Toast.makeText(MainActivity.this, "Die Entfernung soll gleich oder über 50m liegen", Toast.LENGTH_SHORT).show();
                        } else {
                            setAlarm(userInput.getText().toString(),
                                    new LocationCoordiante(lati, lang), vibration.isChecked(),
                                    ringtones.get(ringtoneSelect.getSelectedItem()), ringtoneSelect.getSelectedItem().toString(),
                                    Integer.parseInt(range.getText().toString()), "" + message.getText());
                            //stopService(new Intent(context,GeoService.class));
                            Intent intent = new Intent(context, GeoService.class);
                            startService(intent);
                            alertDialog.dismiss();
                        }
                    }
                });
                locationShow.setText("" + lati + ", " + lang);
                userInput.setText(data.getStringExtra("address"));
            }
        }
    }



    private void showAlarms() {

        ArrayList<GeoAlarm> geoAlarms = alarmDatabase.getAllData();

        if (geoAlarms == null) {

        } else {
            for (GeoAlarm geoAlarm1 : geoAlarms) {
                mAlarms.add(geoAlarm1);
                mAdapter.addItem(mAdapter.getItemCount());
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            return;
        }
        startActivityForResult(new Intent(MainActivity.this, CustomPlacePicker.class), REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(MainActivity.this, CustomPlacePicker.class), REQUEST_CODE);
                }

            }
        }
    }
}
