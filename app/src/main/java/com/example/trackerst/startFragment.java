package com.example.trackerst;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class startFragment extends Fragment implements SensorEventListener {
    TextView displayer;
    Button toSteps, startCounting;
    SensorManager sm;
    Sensor Accelometer;
    heightDB hdb;
    weightDB wdb;

    static int stepCount = 0;
    long lastStepTime = 0;
    static final int STEP_DELAY_MS = 500;
    static final float STEP_THRESHOLD = 9.8f * 1.5f;
    float[] lastAccel = new float[3];
    float accelCurrent,accelLast;

    float[] gravity = new float[3];
    float[] linear_acceleration = new float[3];
    float smoothedAcceleration = 0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.startlayout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        displayer = view.findViewById(R.id.displayer);
        toSteps = view.findViewById(R.id.toSteps);
        startCounting = view.findViewById(R.id.startCounting);

        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;

        sm = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        Accelometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);




        hdb = new heightDB(getContext(), "heightDB", null, 1);
        double heightCheck = hdb.getHeight();
        if (heightCheck == -1) {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.input_height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            EditText heightInputEditText = dialog.findViewById(R.id.heightInputEditText);
            Button heightInputButton = dialog.findViewById(R.id.heightInputButton);


            heightInputButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!heightInputEditText.getText().toString().isEmpty()) {
                        try {
                            double height = Double.parseDouble(heightInputEditText.getText().toString());
                            hdb.replaceHeight(height);
                            dialog.dismiss();

                            wdb = new weightDB(getContext(), "weightDB", null, 1);
                            double lastWeight = wdb.getLastWeight();
                            if (lastWeight == -1) {
                                Dialog dialog1 = new Dialog(getContext());
                                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog1.setContentView(R.layout.layout_add_weight_popup);

                                Button addWeightButton = dialog1.findViewById(R.id.weightAddButton);
                                Button cancelAddWeight = dialog1.findViewById(R.id.cancelAddWeight);
                                EditText weightAddEditText = dialog1.findViewById(R.id.weightAddEditText);
                                cancelAddWeight.setEnabled(false);

                                addWeightButton.setOnClickListener(a -> {
                                    if (!weightAddEditText.getText().toString().isEmpty()) {
                                        try {
                                            String date = DateFormat.dateFormat.format(System.currentTimeMillis());
                                            ContentValues values = new ContentValues();
                                            values.put(weightDB.COLUMN_3_WEIGHT, Double.parseDouble(weightAddEditText.getText().toString()));
                                            values.put(weightDB.COLUMN_4_DATE, date);
                                            values.put(weightDB.COLUMN_5_ESTIMATED_OR_INPUT, "Input");
                                            wdb.insertData(values);
                                            dialog1.cancel();
                                        }
                                        catch (NumberFormatException e) {
                                            Toast.makeText(getContext(), "Please enter a valid weight", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Please enter a valid weight", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                dialog1.show();
                            }
                        }

                        catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Please enter a valid height", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please enter a valid height", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            dialog.show();
        }





        if (!Counting.isCounting) startCounting.setText("Start Counting");
        else if (Counting.isCounting) startCounting.setText("Stop Counting");

        if (Accelometer == null) {
            displayer.setText("This device has no Accelometer counter");
            startCounting.setEnabled(false);
            toSteps.setEnabled(false);
        }
        else {
            displayer.setText("0");
        }

        toSteps.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (Counting.isCounting) {
                sm.unregisterListener(this);
                Counting.isCounting = false;
                startCounting.setText("Start Counting");
            }

            bundle.putInt("STEP_COUNT", stepCount);
            ((MainActivity) getActivity()).nextFragment("Steps", Fragments.STEPS, bundle);
            stepCount = 0;
            updateUiState();
        });

        startCounting.setOnClickListener(v ->{
            if (!Counting.isCounting) {
                if (Accelometer != null) {
                    stepCount = 0;
                    displayer.setText("0");
                    sm.registerListener(this, Accelometer, SensorManager.SENSOR_DELAY_GAME);
                    Counting.isCounting = true;
                    startCounting.setText("Stop Counting");
                }
            }
            else if (Counting.isCounting) {
                sm.unregisterListener(this);
                Counting.isCounting = false;
                startCounting.setText("Start Counting");
            }
            updateUiState();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUiState();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void updateUiState() {
        if (Counting.isCounting) {
            startCounting.setText("Stop Counting");
        } else {
            startCounting.setText("Start Counting");
        }
        // Also update the step display
        displayer.setText(String.valueOf(stepCount));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(!Counting.isCounting) return;

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // --- Step 1: Isolate Gravity from the Raw Data ---
            // This is a low-pass filter. It works like a moving average,
            // separating the constant gravitational force from the dynamic, changing
            // acceleration of the user's movement.

            final float alpha = 0.8f; // Smoothing factor, between 0 and 1.

            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // Remove the gravity contribution from the raw sensor data.
            // What's left is the actual acceleration of the device.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];


            // --- Step 2: Calculate Magnitude on the Clean Data ---
            // We do the same Pythagorean theorem, but now on our "clean"
            // linear_acceleration data, which has no gravity.
            float magnitude = (float) Math.sqrt(
                    linear_acceleration[0] * linear_acceleration[0] +
                            linear_acceleration[1] * linear_acceleration[1] +
                            linear_acceleration[2] * linear_acceleration[2]
            );


            // --- Step 3: Smooth the Magnitude Itself ---
            // The magnitude can still be a bit spiky. We apply another
            // lightweight smoothing filter to it to make finding the "peak" of the step
            // more reliable.
            smoothedAcceleration = smoothedAcceleration * 0.7f + magnitude * 0.3f;


            // --- Step 4: Detect the Step Peak ---
            // Now, instead of looking for a "jolt", we look for the moment
            // the smoothedAcceleration value crosses a threshold. This is much
            // more reliable.

            // A better threshold, now that gravity is removed. This is tunable.
            final float STEP_DETECTION_THRESHOLD = 0.8f;

            long currentTime = System.currentTimeMillis();

            // Check for a step peak.
            if (smoothedAcceleration > STEP_DETECTION_THRESHOLD) {
                // And check the delay to prevent over-counting.
                if (currentTime - lastStepTime > STEP_DELAY_MS) {
                    lastStepTime = currentTime;
                    stepCount++;
                    displayer.setText(String.valueOf(stepCount));
                }
            }
        }
    }
}
