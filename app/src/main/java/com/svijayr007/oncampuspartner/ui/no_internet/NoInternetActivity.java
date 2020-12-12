package com.svijayr007.oncampuspartner.ui.no_internet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.svijayr007.oncampuspartner.MainActivity;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.common.Common;

import spencerstudios.com.bungeelib.Bungee;

public class NoInternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        final LottieAnimationView lottieAnimationView = findViewById(R.id.lottie);
        CardView retryCv = findViewById(R.id.retryCV);

        retryCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Common.isInternetAvailable(getApplicationContext())){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    Bungee.fade(NoInternetActivity.this);
                }else {
                    lottieAnimationView.playAnimation();
                }

            }
        });
    }

    public void onBackPressed() {
        if(!Common.isInternetAvailable(getApplicationContext())) {
            super.onBackPressed();
            Bungee.fade(NoInternetActivity.this);
        }else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(startMain);
            finish();
        }
    }
}