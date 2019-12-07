package com.example.sholkamy.pharmalivary.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.sholkamy.pharmalivary.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView bgapp, clover;
    private LinearLayout textsplash;
    Animation frombottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);


        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);

        bgapp.animate().translationY(-1900).setStartDelay(300);
        clover.animate().alpha(0).setStartDelay(600);
        textsplash.animate().translationY(140).alpha(0).setStartDelay(300);





    }
}