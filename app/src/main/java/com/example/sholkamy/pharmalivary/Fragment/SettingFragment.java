package com.example.sholkamy.pharmalivary.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sholkamy.pharmalivary.Activites.Login;
import com.example.sholkamy.pharmalivary.Activites.MainActivity;
import com.example.sholkamy.pharmalivary.Activites.Signup;
import com.example.sholkamy.pharmalivary.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {

    /**
     * Firebase Components ...
     */
    private FirebaseAuth mFirebaseAuth ;

    private TextView tvName, tvEmail, tvPhone;
    private CircleImageView mProfileImage;
    private LinearLayout mLogout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_setting, container, false);

        mLogout = (LinearLayout)RootView.findViewById(R.id.logout);
        tvName = (TextView)RootView.findViewById(R.id.NameTextView);
        tvEmail = (TextView)RootView.findViewById(R.id.tvEmail);
        tvPhone = (TextView)RootView.findViewById(R.id.tvPhone);
        mProfileImage = (CircleImageView) RootView.findViewById(R.id.profile_image);




        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        updateUI(user);


        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);

                LoginManager.getInstance().logOut();
                FacebookSdk.getApplicationContext();
            }
        });

        return RootView;
    }

    private void updateUI(FirebaseUser user) {

        if(user.getDisplayName() != null)
            tvName.setText(user.getDisplayName());

        if(user.getPhoneNumber() != null)
            tvPhone.setText(user.getDisplayName());

        if(user.getEmail() != null)
            tvEmail.setText(user.getEmail());

        if(user.getPhotoUrl() != null)
            Picasso.get().load(user.getPhotoUrl()).into(mProfileImage);
    }

}
