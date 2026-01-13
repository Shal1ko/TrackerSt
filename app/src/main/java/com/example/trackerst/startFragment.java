package com.example.trackerst;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class startFragment extends Fragment implements SensorEventListener {
    TextView displayer;
    SensorManager sm;
    Sensor Accelometer;

    int stepCount = 0;
    long lastStepTime = 0;
    static final int STEP_DELAY_MS = 500;
    static final float STEP_THRESHOLD = 9.8f * 1.5f;
    float[] lastAccel = new float[3];
    float accelCurrent,accelLast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.startlayout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        displayer = view.findViewById(R.id.displayer);

        sm = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        Accelometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        displayer.setText("Steps: 0");

        if (Accelometer == null) {
            displayer.setText("This device has no Accelometer counter");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Accelometer != null) {
            sm.registerListener(this, Accelometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // each is axis of movement
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            accelLast = accelCurrent;
            accelCurrent = (float) Math.sqrt(x*x + y*y + z*z); //პითაგორას თეორემა 3d-ში

            float curentJolt = accelCurrent - accelLast;

            long currentTime = System.currentTimeMillis();

            if (curentJolt > STEP_THRESHOLD) {
                if (currentTime - lastStepTime > STEP_DELAY_MS) {
                    stepCount++;
                    lastStepTime = currentTime;
                    displayer.setText("Steps: " + stepCount);
                }
            }
        }
    }
}
