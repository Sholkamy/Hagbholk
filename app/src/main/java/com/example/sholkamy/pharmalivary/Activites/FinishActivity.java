package com.example.sholkamy.pharmalivary.Activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sholkamy.pharmalivary.R;

public class FinishActivity extends AppCompatActivity {

    private Button mSubBtn;
    private ImageView mBallon;
    private TextView mExplainText;
    private Animation mFromButtom, mFromTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        mSubBtn = (Button) findViewById(R.id.Home_btn);
        mBallon = (ImageView) findViewById(R.id.ballon);
        mExplainText = (TextView) findViewById(R.id.ExplainText);
        mFromButtom = AnimationUtils.loadAnimation(this,R.anim.from_bottom);
        mFromTop = AnimationUtils.loadAnimation(this,R.anim.from_top);

        mSubBtn.setAnimation(mFromButtom);
        mExplainText.setAnimation(mFromButtom);
        mBallon.setAnimation(mFromTop);


        mSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinishActivity.this, MainActivity.class));
                finish();
            }
        });
    }

}
