package com.byam.chris.outdoorsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

public class AltitudeActivity extends AppCompatActivity implements SensorEventListener {

    Sensor s;
    SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_altitude);

        sm = ((SensorManager)getSystemService(SENSOR_SERVICE));
        s = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);

        //get stored value of origin if there is one
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //if an origin has been stored, then display it, otherwise display that none have been stored
        String originAltitude = preferences.getString("originAltitude", null);
        TextView originTextView = (TextView)findViewById(R.id.originVal);
        if(originAltitude != null){
            originTextView.setText(originAltitude);
        }
        else{
            originTextView.setText("No Altitude Stored");
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //register listener
        if (sm != null){
            sm.registerListener(this, s, sm.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        //unregister listener
        if (sm != null){
            sm.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        //check that sensor type is correct
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE){
            //check that value was registered...what to do if it's not?
            if ((int)event.values[0] > 0){

                //with the changed pressure calculate the altitude, assuming that the pressure at sea level is
                float pressure = event.values[0];

                double height = ((Math.pow(1012.25/pressure,1/5.257)-1)*(15+273.15)/0.0065);
                int hInt = (int)height;
                TextView heightDisplay = (TextView)findViewById(R.id.currentVal);
                heightDisplay.setText("" + hInt + " m");

                //pause sensor after reading
                onPause();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    public void setOriginAltitude(View view){
        //get current val and store it in the preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView heightDisplay = (TextView)findViewById(R.id.currentVal);

        SharedPreferences.Editor edit =  preferences.edit();
        edit.putString("originAltitude", heightDisplay.getText().toString());
        edit.commit();

        TextView originView = (TextView) findViewById(R.id.originVal);
        originView.setText(heightDisplay.getText().toString());
    }

}
