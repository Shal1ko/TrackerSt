package com.example.trackerst;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class weightFragment extends Fragment {
    Button addWeight, backToPreviousFragmentFromWeight, changeHeight;
    ListView weightListView;
    weightDB db;
    heightDB hdb;
    ArrayList<Weight> list;
    weightAdapter adapter;

    int stepCount = 0;
    double height;
    double lastWeight;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getContext() == null) return;

        addWeight = view.findViewById(R.id.addWeight);
        backToPreviousFragmentFromWeight = view.findViewById(R.id.backToPreviousFragmentFromWeight);
        weightListView = view.findViewById(R.id.weightListView);
        changeHeight = view.findViewById(R.id.replaceHeight);

        db = new weightDB(getContext(), "weightDB", null, 1);
        hdb = new heightDB(getContext(), "heightDB", null, 1);

        list = db.getData();
        height = hdb.getHeight();
        adapter = new weightAdapter(getContext(), list);
        weightListView.setAdapter(adapter);



        Bundle argumments = getArguments();

        if (argumments != null) {
            stepCount = argumments.getInt("STEP_COUNT");

            double distance = (stepCount * (height * 0.414))/100000;
            lastWeight = db.getLastWeight();


            double caloriesBurnt = distance * lastWeight * 0.65;
            double newWeight = lastWeight - (caloriesBurnt/7700);
            String date = DateFormat.dateFormat.format(System.currentTimeMillis());

            ContentValues values = new ContentValues();
            values.put(weightDB.COLUMN_2_CALORIES_BURNT, caloriesBurnt);
            values.put(weightDB.COLUMN_3_WEIGHT, newWeight);
            values.put(weightDB.COLUMN_4_DATE, date);
            values.put(weightDB.COLUMN_5_ESTIMATED_OR_INPUT, "Estimated");

            db.insertData(values);
            list.clear();
            list.addAll(db.getData());
            adapter.notifyDataSetChanged();
            argumments.remove("STEP_COUNT");
        }



        backToPreviousFragmentFromWeight.setOnClickListener(v -> {
            ((MainActivity) getActivity()).previousFragment();
        });




        addWeight.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.layout_add_weight_popup);

            Button addWeightButton = dialog.findViewById(R.id.weightAddButton);
            Button cancelAddWeight = dialog.findViewById(R.id.cancelAddWeight);
            EditText weightAddEditText = dialog.findViewById(R.id.weightAddEditText);

            addWeightButton.setOnClickListener(a -> {
                ContentValues values = new ContentValues();
                String date = DateFormat.dateFormat.format(System.currentTimeMillis());

                values.put(weightDB.COLUMN_3_WEIGHT, Double.parseDouble(weightAddEditText.getText().toString()));
                values.put(weightDB.COLUMN_4_DATE, date);
                values.put(weightDB.COLUMN_5_ESTIMATED_OR_INPUT, "Input");
                db.insertData(values);
                list.clear();
                list.addAll(db.getData());
                adapter.notifyDataSetChanged();
                dialog.cancel();
            });


            cancelAddWeight.setOnClickListener(a -> {
                dialog.cancel();
            });

            dialog.show();

        });




        changeHeight.setOnClickListener( v1 -> {
            Dialog dialog1 = new Dialog(getContext());
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.input_height);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            EditText heightInputEditText = dialog1.findViewById(R.id.heightInputEditText);
            Button heightInputButton = dialog1.findViewById(R.id.heightInputButton);
            Button cancelHeightInput = dialog1.findViewById(R.id.cancelHeightInput);
            cancelHeightInput.setEnabled(true);


            heightInputButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!heightInputEditText.getText().toString().isEmpty()) {
                        double height = Double.parseDouble(heightInputEditText.getText().toString());
                        hdb.replaceHeight(height);
                        dialog1.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Please enter a valid height", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            cancelHeightInput.setOnClickListener(v2 -> {
                dialog1.dismiss();
            });


            dialog1.show();
        });



    }
}
