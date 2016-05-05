package com.byam.chris.outdoorsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //set up GridView for home page
        GridView gv = (GridView) findViewById(R.id.gridView);
        gv.setAdapter(new ImageAdapter(this));

        //switch to proper activity on click of icon (identified by position variable)
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = null;
                if (position == 0) { //compass
                    intent = new Intent(MainActivity.this, CompassActivity.class);
                }
                else if (position == 1) { //HRM
                    intent = new Intent(MainActivity.this, HRMActivity.class);
                }
                else if (position == 2){ //altitude
                    intent = new Intent(MainActivity.this, AltitudeActivity.class);
                }
                else{ //gps
                    intent = new Intent(MainActivity.this, GPSActivity.class);
                }
                startActivity(intent);
            }
        });

    }
}

