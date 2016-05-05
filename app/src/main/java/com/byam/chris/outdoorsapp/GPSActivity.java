package com.byam.chris.outdoorsapp;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class GPSActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_gps);


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String originLatitudeStr = preferences.getString("originLatitude", null);
        String originLongitudeStr = preferences.getString("originLongitude", null);
        if(originLatitudeStr != null && originLongitudeStr != null) {
            TextView origLatitudeTV = (TextView) findViewById(R.id.originLatitude);
            TextView origLongitudeTV = (TextView) findViewById(R.id.originLongitude);
            origLatitudeTV.setText(originLatitudeStr);
            origLongitudeTV.setText(originLongitudeStr);
        }


        getLocation(getCurrentFocus());

    }

    public void getLocation(View view) {

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double latitude = (location.getLatitude()) * Math.PI / 180;
        double longitude = (location.getLongitude()) * Math.PI / 180;

        TextView latitudeTV = (TextView) findViewById(R.id.currentLatitude);
        TextView longitudeTV = (TextView) findViewById(R.id.currentLongitude);
        TextView directionTV = (TextView) findViewById(R.id.direction);
        TextView distanceTV = (TextView) findViewById(R.id.distance);

        latitudeTV.setText("" + (latitude *  180 / Math.PI));
        longitudeTV.setText("" + (longitude *  180 / Math.PI));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String originLatitudeStr = preferences.getString("originLatitude", null);
        String originLongitudeStr = preferences.getString("originLongitude", null);
        double distance;
        double direction;


        if(originLatitudeStr != null && originLongitudeStr != null) {
            double originLatitude = Double.parseDouble(originLatitudeStr) * Math.PI / 180;
            double originLongitude = Double.parseDouble(originLongitudeStr) * Math.PI / 180;

            distance = 6372.795477598 * Math.acos(Math.sin(latitude) * Math.sin(originLatitude) + Math.cos(latitude) * Math.cos(originLatitude) * Math.cos(longitude - originLongitude));
            double dlat = Math.log(originLatitude/2 + Math.PI/4)/Math.tan(latitude / 2 + Math.PI/4);
            double dlon = Math.abs(longitude - originLongitude);
            direction = Math.atan2(dlon, dlat);
            directionTV.setText("" + (int)direction);
            distanceTV.setText("" + (int)distance + "km");
            if(direction > (15 * Math.PI / 8) && direction < (Math.PI / 8))
                directionTV.setText("E");
            else if(direction < (1 * Math.PI / 16) && direction > (3 *Math.PI / 8))
                directionTV.setText("NE");
            else if(direction > (3 * Math.PI / 8) && direction < (5 * Math.PI / 8))
                directionTV.setText("N");
            else if(direction < (7 * Math.PI / 8) && direction > (5 * Math.PI / 8))
                directionTV.setText("NW");
            else if(direction < (9 * Math.PI / 8) && direction > (7 * Math.PI / 8))
                directionTV.setText("W");
            else if(direction < (11 * Math.PI / 8) && direction > (9 * Math.PI / 8))
                directionTV.setText("SW");
            else if(direction > (11 * Math.PI / 8) && direction < (13 * Math.PI / 8))
                directionTV.setText("S");
            else //if(direction < (15 * Math.PI / 8) && direction > (13 * Math.PI / 8))
                directionTV.setText("SE");
        }
        else{
            Toast.makeText(getApplicationContext(), "origin location must be stored to find distance and direction", Toast.LENGTH_SHORT);
        }
    }

    public void setOriginCoord(View view){
        TextView latitudeTV = (TextView) findViewById(R.id.currentLatitude);
        TextView longitudeTV = (TextView) findViewById(R.id.currentLongitude);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("originLatitude", latitudeTV.getText().toString());
        editor.putString("originLongitude", longitudeTV.getText().toString());
        editor.commit();

        TextView origLatitudeTV = (TextView) findViewById(R.id.originLatitude);
        TextView origLongitudeTV = (TextView) findViewById(R.id.originLongitude);
        origLatitudeTV.setText(latitudeTV.getText());
        origLongitudeTV.setText(longitudeTV.getText());
    }

}
