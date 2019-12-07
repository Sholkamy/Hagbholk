package com.example.sholkamy.pharmalivary.Activites;

import android.app.NotificationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.sholkamy.pharmalivary.Classes.NotificationHelper;
import com.example.sholkamy.pharmalivary.Classes.RejectPharmaciesID;
import com.example.sholkamy.pharmalivary.Classes.RequestMessage;
import com.example.sholkamy.pharmalivary.Fragment.HomeFragment;
import com.example.sholkamy.pharmalivary.Fragment.NewsFragment;
import com.example.sholkamy.pharmalivary.Fragment.PrescriptionFragment;
import com.example.sholkamy.pharmalivary.Fragment.SettingFragment;
import com.example.sholkamy.pharmalivary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private FrameLayout mFrameLayout;

    private NewsFragment mNewsFragment;
    private HomeFragment mHomeFragment;
    private PrescriptionFragment mPrescriptionFragment;
    private SettingFragment mSettingFragment;

    private long backPressedTime;
    private Toast backToast;

    //To connection app with Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private FirebaseAuth mFirebaseAuth ;
    ChildEventListener mChildEventListener;



    private List<String> UserID = new ArrayList<>();
    private List<Integer> Response = new ArrayList<>();
    public static List<RejectPharmaciesID> PharmaciesID = new ArrayList<RejectPharmaciesID>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //The Entry point for Database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("Orders");
        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mFirebaseAuth.getCurrentUser();

        //For Display Notification
        final NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final NotificationHelper helper = new NotificationHelper(this);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mFrameLayout = (FrameLayout) findViewById(R.id.mainFrame);

        mNewsFragment = new NewsFragment();
        mHomeFragment = new HomeFragment();
        mPrescriptionFragment = new PrescriptionFragment();
        mSettingFragment = new SettingFragment();

        setFragment(mPrescriptionFragment);
        mBottomNavigationView.setItemIconSize(65);
        mBottomNavigationView.setSelectedItemId(R.id.action_home);
        setFragment(mHomeFragment);



        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_news:
                        setFragment(mNewsFragment);
                        item.setChecked(true);
                        break;

                    /*case R.id.action_prescription:
                        setFragment(mPrescriptionFragment);
                        item.setChecked(true);
                        break;*/

                    case R.id.action_home:
                        setFragment(mHomeFragment);
                        item.setChecked(true);
                        break;


                    case R.id.action_settings:
                        setFragment(mSettingFragment);
                        item.setChecked(true);
                        break;
                }
                return false;
            }
        });

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        if (currentFragment instanceof NewsFragment) {
            mBottomNavigationView.setSelectedItemId(R.id.action_news);
        }

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RequestMessage friendlyMessage = dataSnapshot.getValue(RequestMessage.class);

                int getResponse = friendlyMessage.getResponse();
                String getUser_Id = friendlyMessage.getUser_id();
                String CurrentUserID = user.getUid();

                if(CheckStringEqual(getUser_Id ,CurrentUserID) &&getResponse == 1)
                {
                    NotificationCompat.Builder bb = helper.createNotification("Pharmalivary",
                            "Ur request has been Accepted");
                    notificationManager.notify(0,bb.build());

                    //Vibration
                    bb.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                }

                if(CheckStringEqual(getUser_Id ,CurrentUserID) &&getResponse == 0)
                {
                    String PharId = friendlyMessage.getPharmaciesID();
                    String Oid = friendlyMessage.getOrderID();
                    RejectPharmaciesID rejectPharmaciesID = new RejectPharmaciesID(CurrentUserID, PharId, Oid);

                    PharmaciesID.add(rejectPharmaciesID);


                    NotificationCompat.Builder bb = helper.createNotification("Pharmalivary",
                            "Ur request has been declined");
                    notificationManager.notify(0,bb.build());

                    //Vibration
                    bb.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        mMessageDatabaseReference.addChildEventListener(mChildEventListener);


    }


    // This method compares two strings
    private boolean CheckStringEqual(String getUser_id, String currentUserID) {
            int l1 = getUser_id.length();
            int l2 = currentUserID.length();
            int lmin = Math.min(l1, l2);

            for (int i = 0; i < lmin; i++) {
                int str1_ch = (int)getUser_id.charAt(i);
                int str2_ch = (int)currentUserID.charAt(i);

                if (str1_ch != str2_ch) {
                    return false;
                }
            }

            // Edge case for strings like
            // String 1="Geeks" and String 2="Geeksforgeeks"
            if (l1 != l2) {
                return false;
            }

            // If none of the above conditions is true,
            // it implies both the strings are equal
                return true;
        }


    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else {
            backToast = Toast.makeText(getBaseContext() , "Press back again to exit" ,Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();

    }


    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame , fragment);
        fragmentTransaction.commit();
    }
}
