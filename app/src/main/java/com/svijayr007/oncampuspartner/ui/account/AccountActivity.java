package com.svijayr007.oncampuspartner.ui.account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.svijayr007.oncampuspartner.MainActivity;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.ui.no_internet.NoInternetActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import spencerstudios.com.bungeelib.Bungee;

public class AccountActivity extends AppCompatActivity {
    @BindView(R.id.partner_name)
    TextView partner_name;
    @BindView(R.id.partner_phone)
    TextView partner_phone;
    @BindView(R.id.partner_email)
    TextView partner_email;
    @BindView(R.id.partner_campus)
    TextView partner_campus;

    @BindView(R.id.mailUsTV)
    TextView mailUsTV;

    @BindView(R.id.callUsTV)
    TextView callUsTV;

    @BindView(R.id.websiteTV)
    TextView websiteTV;

    @BindView(R.id.privacyTV)
    TextView privacyTV;

    @BindView(R.id.faqTV)
    TextView faqTV;

    @BindView(R.id.logoutTV)
    TextView logoutTV;

    @BindView(R.id.backIV)
    ImageView backIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        setUi();
        setListener();
    }

    private void setListener() {
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mailUsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:contact@oncampus.in"));
                try{
                    startActivity(emailIntent);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //call us
        callUsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(AccountActivity.this)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(new StringBuilder("tel:").append("+919865251212").toString()));
                                startActivity(intent);
                            }
                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(AccountActivity.this, "You must accept this permission to Call user", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });
        //Website
        websiteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Common.isInternetAvailable(getApplicationContext())){
                    Common.openCustomBrowser(getApplicationContext(), Common.onCampusWebsite);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
                    startActivity(intent);
                    Bungee.fade(AccountActivity.this);
                }
            }
        });
        //Privacy Documentation
        privacyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Common.isInternetAvailable(getApplicationContext())){
                    Common.openCustomBrowser(getApplicationContext(), Common.onCampusPrivacy);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
                    startActivity(intent);
                    Bungee.fade(AccountActivity.this);
                }

            }
        });

        //Faq
        faqTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Common.isInternetAvailable(getApplicationContext())){
                    Common.openCustomBrowser(getApplicationContext(), Common.onCampusWebsite);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
                    startActivity(intent);
                    Bungee.fade(AccountActivity.this);
                }
            }
        });

        //logout
        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.currentPartnerUser = null;
                FirebaseAuth.getInstance().signOut();

                //Intent to main activity
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setUi() {
        partner_name.setText(new StringBuilder()
        .append(Common.currentPartnerUser.getName()));

        partner_phone.setText(new StringBuilder()
        .append(Common.currentPartnerUser.getPhone()));

        partner_email.setText(new StringBuilder()
        .append(Common.currentPartnerUser.getEmail()));

        partner_campus.setText(new StringBuilder()
        .append(Common.currentPartnerUser.getCampusId()));


    }
}