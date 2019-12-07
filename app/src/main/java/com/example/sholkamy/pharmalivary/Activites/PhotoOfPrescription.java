package com.example.sholkamy.pharmalivary.Activites;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sholkamy.pharmalivary.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PhotoOfPrescription extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 786;
    private Uri mUserImageUri = null;
    String mUserImageUriString ;


    /**
     * Firebase Components ...
     */
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mDrugsPhotoStorageReference;


    private ImageView mPrescriptionImage;
    private Button mNextButton;
    private Button mChangePhotoButton;
    private ProgressBar mNextProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_of_prescription);

        /**
         * Initialize Firebase Components ...
         */
        mFirebaseStorage = FirebaseStorage.getInstance();


        /**
         * The Entry point for Database ...
         */
        mDrugsPhotoStorageReference = mFirebaseStorage.getReference().child("Drugs_Photos");


        if(isServicesOK()){
            init();
        }

        ChoseImage();

        mPrescriptionImage = (ImageView) findViewById(R.id.PrescriptionImage);
        mChangePhotoButton = (Button) findViewById(R.id.ReButton);
        mNextProgressBar = (ProgressBar) findViewById(R.id.NextProgress);

        ButtonVISIBLE();

        mChangePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoseImage();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mUserImageUri = CropImage.getPickImageResultUri(this, data);

            //GET CROPPED IMAGE URI AND PASS TO IMAGEVIEW
            mPrescriptionImage.setImageURI(mUserImageUri);
            UploadMedicineImage();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //THIS IS HAPPEN WHEN USER CLICK ALLOW ON PERMISSION
            //START PICK IMAGE ACTIVITY
            CropImage.startPickImageActivity(PhotoOfPrescription.this);
        }
    }


    private void init(){
        mNextButton = (Button) findViewById(R.id.NextButton);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(mUserImageUri == null)
                    ViewMessage("Plz ,Enter a medicine image");

                else {
                    Intent intent = new Intent(PhotoOfPrescription.this, MapActivity.class);
                    intent.putExtra("URI_String", mUserImageUriString);
                    startActivity(intent);
                }
            }
        });
    }


    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(PhotoOfPrescription.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(PhotoOfPrescription.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void ChoseImage() {
        //IF THE ANDROID SDK UP TO MARSMALLOW BUILD NUMBER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //START REQUEST PERMISSION
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            //ELSE BELOW START OPEN PICKER
            CropImage.startPickImageActivity(PhotoOfPrescription.this);
        }
    }


    private void UploadMedicineImage() {


        if (mUserImageUri != null) {
            final StorageReference imgReference = mDrugsPhotoStorageReference.child(mUserImageUri.getLastPathSegment());
            UploadTask uploadTask = imgReference.putFile(mUserImageUri);

            ButtonINVISIBLE();

            // [START download_via_url]
            mDrugsPhotoStorageReference.child(mUserImageUri.getLastPathSegment()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    mUserImageUriString = uri.toString();
                }
            });

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return imgReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri taskResult = task.getResult();
                        mUserImageUriString = taskResult.toString();
                        ButtonVISIBLE();
                        Toast.makeText(PhotoOfPrescription.this, "Photo uploaded", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }


    private void ButtonVISIBLE() {
        mNextButton.setVisibility(View.VISIBLE);
        mNextProgressBar.setVisibility(View.INVISIBLE);
    }


    private void ButtonINVISIBLE() {
        mNextButton.setVisibility(View.INVISIBLE);
        mNextProgressBar.setVisibility(View.VISIBLE);
    }


    private void ViewMessage(String message) {
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT).show();
    }

}
