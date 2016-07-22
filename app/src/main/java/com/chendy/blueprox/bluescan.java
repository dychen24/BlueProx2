package com.chendy.blueprox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.LocationManager;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Looper;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class bluescan extends AppCompatActivity implements SensorEventListener {

    /*
     * Initialize parameters:
     */
    private SensorManager mSensorManager = null;
    private LocationManager locationManager;
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    float[] orientationValues = new float[3];
    float[] gravityValues = new float[3];
    static int detection_length = 50;
    float[] gyroValues = new float[detection_length];

    static float[] acc_i = new float[detection_length];
    static float[] acc_x = new float[detection_length];
    static float[] acc_y = new float[detection_length];

    static int sample_range = 30; //Used to be 50
    float[] gyro_lp = new float[sample_range];
    List<Float> al=new ArrayList<Float>();

    private EditText accelerate;
    private EditText gyro;
    private EditText magnetic;
    private EditText orientation;
    private EditText speed;
    private EditText bump;
    private EditText gravity;
    private EditText Speed_text;
    private EditText integration;
    private EditText syslog;

    private float Acc_Z = 0;
    public float Acc_integrated = 0;

    double q_velo = 0.08;
    double dt = 0.1;
    private Location location;
    double GPS_SPEED;

    double Latitude;
    double Longitude;

    double velo = 0;
    double velo_filtered=0;



    @Override
    protected void onResume() {
        super.onResume();
        // Registrate the Accelerometer:
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
 /*       mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_GAME);
                */
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Some change here
        setContentView(R.layout.activity_bluescan);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event){

    }
}
