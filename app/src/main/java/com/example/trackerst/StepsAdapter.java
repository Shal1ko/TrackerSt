package com.example.trackerst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StepsAdapter extends BaseAdapter {

    ArrayList<Steps> list;
    LayoutInflater inflater;

    public StepsAdapter(Context context, ArrayList<Steps> list) {
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
            convertView = inflater.inflate(R.layout.stepslayout, parent, false);
        }

        TextView steps = convertView.findViewById(R.id.steps);
        TextView date = convertView.findViewById(R.id.date);

        steps.setText("Steps: " + list.get(position).getSteps());
        date.setText("Date: " + list.get(position).getDate());

        return convertView;
    }
}
