package com.example.sholkamy.pharmalivary.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sholkamy.pharmalivary.Activites.MainActivity;
import com.example.sholkamy.pharmalivary.R;

public class PrescriptionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View RootView = inflater.inflate(R.layout.fragment_prescription, container, false);


        return RootView;
    }
}
