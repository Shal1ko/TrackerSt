package com.example.trackerst;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class stepsFragment extends Fragment {

    Button returnToStart, toWeight;
    ListView stepsListView;

    StepsAdapter adapter;
    ArrayList<Steps> stepsArrayList;
    stepsTable StepsTable;

    int stepCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stepsfragmentlayout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        returnToStart = view.findViewById(R.id.returnToStartFromSteps);
        stepsListView = view.findViewById(R.id.stepsListView);
        toWeight = view.findViewById(R.id.toWeightFromSteps);

        StepsTable = new stepsTable(getContext(), "stepsTable", null, 1);


        Bundle arguments = getArguments();


        if (arguments != null && arguments.containsKey("STEP_COUNT")) {
            stepCount = arguments.getInt("STEP_COUNT");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String date = dateFormat.format(System.currentTimeMillis());


            ContentValues values = new ContentValues();
            values.put(stepsTable.COLUMN_2_STEPS, stepCount);
            values.put(stepsTable.COLUMN_3_DATE, date);
            StepsTable.insertData(values);
        }

        stepsArrayList = StepsTable.getData();
        adapter = new StepsAdapter(getContext(), stepsArrayList);
        stepsListView.setAdapter(adapter);



        stepsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("Confirm Deletion").setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StepsTable.deleteData(stepsArrayList.get(position).getId());
                                stepsArrayList.clear();
                                stepsArrayList.addAll(StepsTable.getData());
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null).show();

                return false;
            }
        });

        returnToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).previousFragment();
            }
        });

        toWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("STEP_COUNT", stepCount);

                ((MainActivity) getActivity()).nextFragment("Weight", "weightFragment", bundle);
            }
        });

    }
}
