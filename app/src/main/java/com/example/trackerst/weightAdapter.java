package com.example.trackerst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class weightAdapter extends BaseAdapter {
    ArrayList<Weight> list;
    LayoutInflater inflater;

    public weightAdapter(Context context, ArrayList<Weight> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater.inflate(R.layout.layout_weight, parent, false);
        }

        TextView caloriesBurnt = convertView.findViewById(R.id.caloriesBurnt);
        TextView weight = convertView.findViewById(R.id.weight);
        TextView date = convertView.findViewById(R.id.date);
        TextView inputType = convertView.findViewById(R.id.inputType);

        caloriesBurnt.setText("Calories Burnt - " +  list.get(position).getCaloriesBurnt());
        weight.setText("Weight - " + list.get(position).getWeight());
        date.setText("Date - " + list.get(position).getDate());
        inputType.setText("Input Type - " + list.get(position).getInputType());

        return convertView;
    }
}
