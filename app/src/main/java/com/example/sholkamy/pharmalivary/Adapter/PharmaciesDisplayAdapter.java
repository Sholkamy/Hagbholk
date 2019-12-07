package com.example.sholkamy.pharmalivary.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sholkamy.pharmalivary.Classes.PharmaciesData;
import com.example.sholkamy.pharmalivary.R;

import java.util.List;

public class PharmaciesDisplayAdapter extends ArrayAdapter<PharmaciesData>{
    public PharmaciesDisplayAdapter(Context context, int resource, List<PharmaciesData> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_display_pharmacies, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.NameTextView);
        TextView LatTextView = (TextView) convertView.findViewById(R.id.LatTextView);
        TextView LngTextView = (TextView) convertView.findViewById(R.id.LngTextView);

        PharmaciesData pharmaciesData = getItem(position);

        nameTextView.setText(pharmaciesData.getName());
        LatTextView.setText(pharmaciesData.getLat());
        LngTextView.setText(pharmaciesData.getLng());

        return convertView;
    }
}

