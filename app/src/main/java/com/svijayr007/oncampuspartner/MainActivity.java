package com.svijayr007.oncampuspartner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.model.PartnerUserModel;
import com.svijayr007.oncampuspartner.ui.login.LoginActivity;
import com.svijayr007.oncampuspartner.ui.no_internet.NoInternetActivity;
import com.svijayr007.oncampuspartner.ui.signup.SignupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private DatabaseReference partnerRef;


    @BindView(R.id.normal_loading_layout)
    RelativeLayout normal_loading_layout;
    @BindView(R.id.root_layout)
    RelativeLayout root_layout;

    //Verification pending
    @BindView(R.id.verification_pending_layout)
    LinearLayout verification_pending_layout;
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


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if(listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        partnerRef = FirebaseDatabase.getInstance(Common.partnerDb).getReference(Common.PARTNER_REF);
        firebaseAuth  = FirebaseAuth.getInstance();
        listener = firebaseAuth -> {
            Dexter.withActivity(MainActivity.this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user!=null){
                                //check registration
                                checkPartnerFromFirebase(user);

                            }else {
                                phoneLogin();
                            }

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Toast.makeText(MainActivity.this, "You must enable this permission to use app", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        };

    }

    private void checkPartnerFromFirebase(FirebaseUser user) {

        partnerRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            PartnerUserModel partnerUserModel = dataSnapshot.getValue(PartnerUserModel.class);
                            Common.currentPartnerUser = partnerUserModel; // for updating time
                            if(partnerUserModel.isActive()){
                                goToHomeActivity(partnerUserModel);
                            }
                            else {
                                //Verification Not done
                                Common.updateLastVisitedTime(MainActivity.this); // Only if verification not done
                                root_layout.setBackgroundColor(getResources().getColor(R.color.white));
                                normal_loading_layout.setVisibility(View.GONE);
                                verification_pending_layout.setVisibility(View.VISIBLE);
                                setListener();
                                Toast.makeText(MainActivity.this, "verification pending", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            //user not exists in database

                            showRegisterDialog();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void setListener() {
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
                Dexter.withContext(MainActivity.this)
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
                                Toast.makeText(MainActivity.this, "You must accept this permission to Call user", Toast.LENGTH_SHORT).show();
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
                    Bungee.fade(MainActivity.this);
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
                    Bungee.fade(MainActivity.this);
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
                    Bungee.fade(MainActivity.this);
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
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showRegisterDialog() {
        Intent signUpIntent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(signUpIntent);
        finish();
    }

    private void goToHomeActivity(PartnerUserModel partnerUserModel) {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Main Activity Token Fetch: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Common.currentPartnerUser  = partnerUserModel;
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    }
                }).addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                Common.currentPartnerUser  = partnerUserModel;
                Common.updateToken(MainActivity.this,task.getResult().getToken());
                Common.updateLastVisitedTime(MainActivity.this);
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        });


    }

    private void phoneLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }


}