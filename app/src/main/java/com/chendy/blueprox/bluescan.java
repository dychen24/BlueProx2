package com.chendy.blueprox;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.LocationManager;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class bluescan extends AppCompatActivity implements SensorEventListener {

    /*
     * Initialize sensor parameters:
     */
    private SensorManager mSensorManager = null;
    private LocationManager locationManager;
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    static int detection_length = 50;
    float[] gyroValues = new float[detection_length];

    // Initiate Bluetooth parameters:
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluescan);
        // initialize viewer
        accelerate = (EditText) findViewById(R.id.main_et_accelerate);
        gyro = (EditText) findViewById(R.id.main_et_gyro);
        syslog = (EditText) findViewById(R.id.main_et_syslog);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Initiate scanning process:
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        adapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            System.out.println("I'm HERE!!!");

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                // Show progress dialog
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // Dismiss progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Bluetooth devices found:
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                syslog.getText().append("Found device: " + device.getName());

            } else {
                System.out.println("Blue not found");

            }
        }
    };

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
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        float[] values = event.values;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                accelerometerValues = values;
                DecimalFormat df=new DecimalFormat("0.00");
                accelerate.setText("Acc(X): " + df.format(accelerometerValues[0]) + " a/m");
                //syslog.getText().append("\nAcc" + accelerometerValues[0]+" ,"+accelerometerValues[1]+" ,"+accelerometerValues[2]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticFieldValues = values;
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroValues = values;
                DecimalFormat df2=new DecimalFormat("0.00");
                gyro.setText("Gyro(X): " + df2.format(gyroValues[0]));
                break;
        }

    }
}
