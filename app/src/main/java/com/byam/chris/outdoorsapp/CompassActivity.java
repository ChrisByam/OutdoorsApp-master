package com.byam.chris.outdoorsapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Chris on 4/30/2016.
 */

//use geomagnetic field sensor and accelerometer to report compass bearing
public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    ImageView mPointer;
    Sensor a;
    Sensor m;
    SensorManager sm;
    float[] mLastAcc = new float[3];
    float[] mLastMag = new float[3];
    boolean mLastAccSet = false;
    boolean mLastMagSet = false;
    float[] mR = new float[9];
    float[] mOrientation = new float[3];
    float mCurrentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_compass);

        sm = ((SensorManager)getSystemService(SENSOR_SERVICE));
        a = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mPointer = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register listeners
        sm.registerListener(this, a, SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener(this, m, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //unregister listener
        sm.unregisterListener(this, a);
        sm.unregisterListener(this, m);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        //copy values from sensors into arrays
        if (event.sensor == a) {
            System.arraycopy(event.values, 0, mLastAcc, 0, event.values.length);
            mLastAccSet = true;
        } else if (event.sensor == m) {
            System.arraycopy(event.values, 0, mLastMag, 0, event.values.length);
            mLastMagSet = true;
        }
        //if sensors contain values, perform orientation analysis
        if (mLastAccSet && mLastMagSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAcc, mLastMag);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegrees = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegrees,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);
            ra.setFillAfter(true);
            mPointer.startAnimation(ra);
            mCurrentDegree = -azimuthInDegrees;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

}
