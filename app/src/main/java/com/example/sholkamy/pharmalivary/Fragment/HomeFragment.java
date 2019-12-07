package com.example.sholkamy.pharmalivary.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.sholkamy.pharmalivary.Activites.Login;
import com.example.sholkamy.pharmalivary.Activites.MainActivity;
import com.example.sholkamy.pharmalivary.Activites.PhotoOfPrescription;
import com.example.sholkamy.pharmalivary.Activites.Signup;
import com.example.sholkamy.pharmalivary.Activites.WriteMedicineName;
import com.example.sholkamy.pharmalivary.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class HomeFragment extends Fragment {

    static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 786;


    private NewsFragment mNewsFragment;
    private PrescriptionFragment mPrescriptionFragment;
    private SettingFragment mSettingFragment;

    private LinearLayout mSearchAMedicine;
    private LinearLayout mShowAPrescription;
    private LinearLayout mWriteAMedicineName;
    private LinearLayout mViewAProfile;
    private LinearLayout mPrescriptionPhoto;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View RootView = inflater.inflate(R.layout.fragment_home, container, false);

        mNewsFragment = new NewsFragment();
        mPrescriptionFragment = new PrescriptionFragment();
        mSettingFragment = new SettingFragment();


        mSearchAMedicine = (LinearLayout) RootView.findViewById(R.id.SearchAMedicine);
        mShowAPrescription = (LinearLayout) RootView.findViewById(R.id.ShowAPrescription);
        mWriteAMedicineName = (LinearLayout) RootView.findViewById(R.id.WriteAMedicineName);
        mViewAProfile = (LinearLayout) RootView.findViewById(R.id.ViewAProfile);
        mPrescriptionPhoto = (LinearLayout) RootView.findViewById(R.id.PrescriptionPhoto);




        mSearchAMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BottomNavigationView)getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.action_news);
                setFragment(mNewsFragment);
            }
        });


        mShowAPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(mPrescriptionFragment);
            }
        });


        mViewAProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BottomNavigationView)getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.action_settings);
                setFragment(mSettingFragment);
            }
        });


        mPrescriptionPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhotoOfPrescription.class);
                startActivity(intent);
            }
        });


        mWriteAMedicineName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WriteMedicineName.class);
                startActivity(intent);
            }
        });


        return RootView;
    }


    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame , fragment);
        fragmentTransaction.commit();
    }
}
