package com.example.watch_tracker;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.Button;

import com.example.watch_tracker.databinding.ActivityMainBinding;

public class MainActivity extends Activity implements SensorEventListener{

    private TextView mTextView;
    private ActivityMainBinding binding;
    private SensorManager sensorManager;
    private Sensor sensor;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp = 0;
    private float[] angle = new float[3];
    private TextView angle_X;
    private TextView angle_Y;
    private TextView angle_Z;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button button_start = findViewById(R.id.start);
        final Button button_stop = findViewById(R.id.stop);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener((SensorEventListener) this, sensor, SensorManager.SENSOR_DELAY_UI);
        angle_X = (TextView) findViewById(R.id.angle_x);
        angle_Y = (TextView) findViewById(R.id.angle_y);
        angle_Z = (TextView) findViewById(R.id.angle_z);
//
        button_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                angle[0] = 0;
                angle[1] = 0;
                angle[2] = 0;
                angle_X.setText(String.format("%.1f", (float) Math.toDegrees(angle[0])));
                angle_Y.setText(String.format("%.1f", (float) Math.toDegrees(angle[1])));
                angle_Z.setText(String.format("%.1f", (float) Math.toDegrees(angle[2])));
                onResume();
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                onPause();
                angle_X.setText(String.format("%.1f", (float) Math.toDegrees(angle[0])));
                angle_Y.setText(String.format("%.1f", (float) Math.toDegrees(angle[1])));
                angle_Z.setText(String.format("%.1f", (float) Math.toDegrees(angle[2])));
            }
        });
    }

    public void onSensorChanged(SensorEvent event)
    {
        if (timestamp != 0)
        {
            // event.timesamp表示当前的时间，单位是纳秒（1百万分之一毫秒）
            final float dT = (event.timestamp - timestamp) * NS2S;
            angle[0] += event.values[0] * dT;
            angle[1] += event.values[1] * dT;
            angle[2] += event.values[2] * dT;
        }
        timestamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    protected void onResume() {
        super.onResume();
        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        Sensor gyrpscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyrpscope != null) {
            sensorManager.registerListener((SensorEventListener) this, gyrpscope,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    protected void onPause() {
        super.onPause();
        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener((SensorEventListener) this);
    }

}