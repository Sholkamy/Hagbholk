package com.example.sholkamy.pharmalivary.Activites;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sholkamy.pharmalivary.Adapter.PharmaciesDisplayAdapter;
import com.example.sholkamy.pharmalivary.Classes.PharmaciesData;
import com.example.sholkamy.pharmalivary.Classes.RejectPharmaciesID;
import com.example.sholkamy.pharmalivary.Classes.RequestMessage;
import com.example.sholkamy.pharmalivary.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Pharmacies extends AppCompatActivity {

    /**
     * Firebase Components ...
     */
    private FirebaseDatabase mOrdersFirebaseDatabase ,mPharmaciesFirebaseDatabase;
    private DatabaseReference mOrdersMessageDatabaseReference ,mPharmaciesMessageDatabaseReference;
    private FirebaseUser user;
    private FirebaseAuth mFirebaseAuth;

    //Declaration of view
    private PharmaciesDisplayAdapter mPharmaciesDisplayAdapter;
    private ListView primaryMessageListView;
    private List<PharmaciesData> requestMessages = new ArrayList<>();


    //To Show massages in Ur Mob
    ChildEventListener mChildEventListener;

    private String mUsername = "Null";
    private String requestKey = "Null";
    private String mUserID = "Null";
    String mUserImageUriString = "Null";
    ArrayList<String> mMedicineName = null;
    ArrayList<String> mMedicineQuantity = null;
    private boolean CheckNew = true;
    private String CurrentOrderID = "Null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacies);

        /**
         * Initialize Firebase Components ...
         */
        mOrdersFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        mUserID = user.getUid();
        mUsername = user.getDisplayName();



        //The Entry point for Database
        mPharmaciesFirebaseDatabase = FirebaseDatabase.getInstance();
        mPharmaciesMessageDatabaseReference = mPharmaciesFirebaseDatabase.getReference().child("Pharmacies");

        primaryMessageListView = (ListView) findViewById(R.id.primaryMessageListView);


        final double Lat = getIntent().getDoubleExtra("LatOfCurrentLocation" , 0.0);
        final double Lng = getIntent().getDoubleExtra("LngOfCurrentLocation" , 0.0);

        ViewMessage(Lat + " , " + Lng);

        // Initialize message ListView and its adapter
        mPharmaciesDisplayAdapter = new PharmaciesDisplayAdapter(this, R.layout.item_display_pharmacies, requestMessages);

        primaryMessageListView.setAdapter(mPharmaciesDisplayAdapter);


        if (getIntent().getStringExtra("URI_String") != null) {
            mUserImageUriString = getIntent().getStringExtra("URI_To_Finish_String");
        }

        if ((ArrayList<String>) getIntent().getSerializableExtra("Medicine_To_Finish_ArrayList") != null
                && (ArrayList<String>) getIntent().getSerializableExtra("Quantity_To_Finish_ArrayList") != null) {
            mMedicineName = (ArrayList<String>) getIntent().getSerializableExtra("Medicine_To_Finish_ArrayList");
            mMedicineQuantity = (ArrayList<String>) getIntent().getSerializableExtra("Quantity_To_Finish_ArrayList");
        }


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                PharmaciesData pharmaciesData = dataSnapshot.getValue(PharmaciesData.class);

                double pharmaciesLng = Double.valueOf(pharmaciesData.getLng());
                double pharmaciesLat = Double.valueOf(pharmaciesData.getLat());

                if(CheckReject(MainActivity.PharmaciesID, user.getUid(), pharmaciesData.getID()) == false)
                    CheckNew = false;

                if(BetweenDistance(Lng, Lat, pharmaciesLng, pharmaciesLat) &&
                        CheckReject(MainActivity.PharmaciesID, user.getUid(), pharmaciesData.getID()))
                    mPharmaciesDisplayAdapter.add(pharmaciesData);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        primaryMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String PharmaciesID = requestMessages.get(position).getID();
                String mUserPhone = "01018779173";


                if(CheckNew) {

                    /**
                     * The Entry point for Database ...
                     */
                    mOrdersMessageDatabaseReference = mOrdersFirebaseDatabase.getReference().child("Orders").push();
                    //To get key in variable
                    requestKey = mOrdersMessageDatabaseReference.getKey();

                    RequestMessage requestMessage = new RequestMessage(2, 0, mUsername, requestKey, mUserID,
                        PharmaciesID, mUserImageUriString, mUserPhone, Lng, Lat, mMedicineName, mMedicineQuantity);
                mOrdersMessageDatabaseReference.setValue(requestMessage);
                }

                if(CheckNew == false) {
                    mOrdersMessageDatabaseReference = mOrdersFirebaseDatabase.getReference();

                    try {
                        mOrdersMessageDatabaseReference.child("Orders").child(CurrentOrderID).child("pharmaciesID").setValue(PharmaciesID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                Intent intent = new Intent(Pharmacies.this, FinishActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mPharmaciesMessageDatabaseReference.addChildEventListener(mChildEventListener);


        GoogleMap googleMap;


    }

    private boolean CheckReject(List<RejectPharmaciesID> pharmaciesID, String uid, String id) {
        for(int i = 0; i < pharmaciesID.size(); i++)
        {
            if(pharmaciesID.get(i).getCurrentUser() == uid &&
                    pharmaciesID.get(i).getPharmaciesID() == id){

                CurrentOrderID = pharmaciesID.get(i).getOrderID();
                return false;
            }
        }
        return true;
    }

    private boolean BetweenDistance(double lng, double lat, double pharmaciesLng, double pharmaciesLat) {

        double MinCurrentLat = lat - 0.15;
        double MaxCurrentLat = lat + 0.15;
        double MinCurrentLng = lng - 0.15;
        double MaxCurrentLng = lng + 0.15;

        if(pharmaciesLng > MaxCurrentLng || pharmaciesLng < MinCurrentLng ||
                pharmaciesLat > MaxCurrentLat || pharmaciesLat < MinCurrentLat)
            return false;

        else
            return true;
    }

    private void ViewMessage(String message) {
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
}
