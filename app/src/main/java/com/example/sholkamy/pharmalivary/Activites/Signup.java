package com.example.sholkamy.pharmalivary.Activites;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sholkamy.pharmalivary.Classes.RequestMessage;
import com.example.sholkamy.pharmalivary.Classes.UserData;
import com.example.sholkamy.pharmalivary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {


    private String UserKey = "Null";
    static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 786;
    private Uri mUserImageUri = null;


    private EditText inputName, inputEmail, inputPassword, inputConfirmPassword;
    private Button btnSignUp;
    private ProgressBar mProgressBar;
    private CircleImageView  mProfileImageView;


    /**
     * Firebase Components ...
     */
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mMessageDatabaseReference = mFirebaseDatabase.getInstance().getReference().child("Users").push();
        //To get key in variable
        UserKey = mMessageDatabaseReference.getKey();

        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputName = (EditText) findViewById(R.id.userName);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mProfileImageView = (CircleImageView) findViewById(R.id.profile_image);
        mProgressBar = (ProgressBar) findViewById(R.id.JoinProgress);
        mProgressBar.setVisibility(View.INVISIBLE);


        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IF THE ANDROID SDK UP TO MARSMALLOW BUILD NUMBER
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //START REQUEST PERMISSION
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    //ELSE BELOW START OPEN PICKER
                    CropImage.startPickImageActivity(Signup.this);
                }
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSignUp.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);

                final String name = inputName.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String confirmPassword = inputConfirmPassword.getText().toString().trim();


                if (TextUtils.isEmpty(name)) {
                    ButtonVisible();
                    ViewMessage("Enter full name!");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    ButtonVisible();
                    ViewMessage("Enter email address!");
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    ButtonVisible();
                    ViewMessage("Enter password!");
                    return;
                }

                if (password.length() < 6) {
                    ButtonVisible();
                    ViewMessage("Password too short, enter minimum 6 characters!");
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    ButtonVisible();
                    ViewMessage("Enter password!");
                    return;
                }

                if (checkPassword() == false) {
                    ButtonVisible();
                    ViewMessage("Error in Re-enter the password!");
                    return;
                }

                else {
                    CreatUserAccount(name,email,password);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            //NOW CROP IMAGE URI
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .setRequestedSize(800, 800)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //GET CROPPED IMAGE URI AND PASS TO IMAGEVIEW
                mUserImageUri = result.getUri();
                mProfileImageView.setImageURI(result.getUri());
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //THIS IS HAPPEN WHEN USER CLICK ALLOW ON PERMISSION
            //START PICK IMAGE ACTIVITY
            CropImage.startPickImageActivity(Signup.this);
        }
    }


    private void ButtonVisible() {
        btnSignUp.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }


    private void ViewMessage(String message) {
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT).show();
    }


    private void CreatUserAccount(final String name, final String email, final String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        ViewMessage("Authentication failed." + task.getException());
                        ButtonVisible();
                    } else {
                        updateUserInformation(name,mUserImageUri,mFirebaseAuth.getCurrentUser());
                    }
                }
            });
    }


    private void updateUserInformation(final String name, Uri mUserImageUri, final FirebaseUser currentUser) {
        StorageReference mUserImageReference = FirebaseStorage.getInstance().getReference().child("user_photo");


        if (mUserImageUri == null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                ViewMessage("congratulations" + user.getDisplayName()
                                        + "! successful create user ..." + task.isSuccessful());
                                mUploadUI();                                            }
                        }
                    });
        }


        else {
            final StorageReference mUserImage = mUserImageReference.child(mUserImageUri.getLastPathSegment());
            mUserImage.putFile(mUserImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mUserImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            UserProfileChangeRequest mProfileUpload = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(uri)
                                    .build();

                            currentUser.updateProfile(mProfileUpload)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                                ViewMessage("congratulations" + user.getDisplayName()
                                                        + "! successful create user ..." + task.isSuccessful());
                                                mUploadUI();
                                            }
                                        }
                                    });
                        }
                    });
                }
            });
        }
    }


    private void mUploadUI() {
        startActivity(new Intent(Signup.this, MainActivity.class));
        finish();
    }


    public boolean checkPassword()
    {
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmPassword);

        String S1 = inputConfirmPassword.getText().toString();
        String S2 = inputPassword.getText().toString();

        if (S1.equals(S2))
            return true;

        return false;
    }
}