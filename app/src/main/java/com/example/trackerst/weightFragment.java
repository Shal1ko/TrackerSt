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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class weightFragment extends Fragment {
    Button addWeight, backToPreviousFragmentFromWeight;
    ListView weightListView;
    weightDB db;
    ArrayList<Weight> list;
    weightAdapter adapter;

    int stepCount = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addWeight = view.findViewById(R.id.addWeight);
        backToPreviousFragmentFromWeight = view.findViewById(R.id.backToPreviousFragmentFromWeight);
        weightListView = view.findViewById(R.id.weightListView);
        db = new weightDB(getContext(), "weightDB", null, 1);

        list = db.getData();
        adapter = new weightAdapter(getContext(), list);
        weightListView.setAdapter(adapter);


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
                values.put(weightDB.COLUMN_3_WEIGHT, Integer.parseInt(weightAddEditText.getText().toString()));
                values.put(weightDB.COLUMN_4_DATE, System.currentTimeMillis());
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


    }
}
