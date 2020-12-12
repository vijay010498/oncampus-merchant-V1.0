package com.svijayr007.oncampuspartner.ui.signup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.svijayr007.oncampuspartner.MainActivity;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.adapter.MyCampusAdapter;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.common.SpacesItemDecoration;
import com.svijayr007.oncampuspartner.eventBus.CampusSelectedEvent;
import com.svijayr007.oncampuspartner.model.CampusModel;
import com.svijayr007.oncampuspartner.model.PartnerUserModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private EditText edtPartnerName;
    private TextView gmail;
    private TextView textCampusName;
    private ImageView closeImage;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private MaterialButton buttonRegister;
    private DatabaseReference partnerRef;
    private static  CampusModel selectedCampus;

    //Campus
    private CampusViewModel campusViewModel;
    private AlertDialog dialogCampus;
    private KProgressHUD hud;


    //Google SIgn in
    private static  final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mgoogleSignInClient;
    private static String googleIdToken ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        campusViewModel = ViewModelProviders.of(this).get(CampusViewModel.class);
        init();
        setListener();
    }

    private void init() {
        hud = KProgressHUD.create(SignupActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                .setCancellable(true)
                .setLabel("Creating Account")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        edtPartnerName = findViewById(R.id.edit_partnerName);
        textCampusName = findViewById(R.id.text_campus_name);
        gmail = findViewById(R.id.gmail);
        closeImage = findViewById(R.id.image_close);
        mAuth  = FirebaseAuth.getInstance();
        user  = mAuth.getCurrentUser();
        buttonRegister = findViewById(R.id.button_register);
        partnerRef = FirebaseDatabase.getInstance(Common.partnerDb).getReference(Common.PARTNER_REF);

        //Google signin
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult();
                googleIdToken = account.getIdToken();
                gmail.setText(new StringBuilder()
                .append(account.getEmail()));

            }catch (Exception e){
                if(e.getMessage().contains("12501")){
                    Toast.makeText(this, "No Email selected", Toast.LENGTH_SHORT).show();
                    gmail.setHint("Link Email!");
                }
                else
                    Toast.makeText(this, "Gmail Error"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void setListener() {
        //for close image
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        textCampusName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                builder.setTitle("Select Your Campus!");
                View v = LayoutInflater.from(SignupActivity.this).inflate(R.layout.layout_campus,null);
                RecyclerView recycler_campus = v.findViewById(R.id.recycler_campus);
                campusViewModel.getMessageError().observe(SignupActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String message) {
                        Toast.makeText(SignupActivity.this, ""+message, Toast.LENGTH_SHORT).show();

                    }
                });
                campusViewModel.getCampusListMutable().observe(SignupActivity.this, new Observer<List<CampusModel>>() {
                    @Override
                    public void onChanged(List<CampusModel> campusModelList) {
                        Log.i("CAMPUS CHECk-2",campusModelList.get(0).getName());
                        MyCampusAdapter campusAdapter = new MyCampusAdapter(SignupActivity.this,campusModelList);
                        Log.i("CAMPUS CHECK-3",String.valueOf(campusAdapter.getItemCount()));
                        recycler_campus.setAdapter(campusAdapter);
                        recycler_campus.setLayoutManager(new LinearLayoutManager(SignupActivity.this));
                        recycler_campus.setVerticalScrollBarEnabled(true);
                        recycler_campus.addItemDecoration(new SpacesItemDecoration(5));
                    }
                });
                builder.setView(v);
                dialogCampus = builder.create();
                dialogCampus.show();
            }
        });


        //Google signin
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mgoogleSignInClient != null){
                    mgoogleSignInClient.signOut();
                    gmail.setText("");
                }
                Intent signInIntent = mgoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //For Register Button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edtPartnerName.getText().toString())){
                    edtPartnerName.setError("Name Required");
                    return;
                }
                else if(TextUtils.isEmpty(gmail.getText().toString()) ||
                        !isValidMail(gmail.getText().toString())){
                    gmail.setError("Email Empty or not valid");
                    return;
                }
                else if(TextUtils.isEmpty(textCampusName.getText().toString())){
                    textCampusName.setError("Please select a campus");
                    return;
                }
                hud.show();
                AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken,null);
                mAuth.getCurrentUser().linkWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    PartnerUserModel partnerUserModel = new PartnerUserModel();
                                    partnerUserModel.setActive(false); // Default False
                                    partnerUserModel.setEmail(gmail.getText().toString());
                                    partnerUserModel.setName(edtPartnerName.getText().toString());
                                    partnerUserModel.setPhone(user.getPhoneNumber());
                                    partnerUserModel.setUid(user.getUid());
                                    partnerUserModel.setLastVisited(System.currentTimeMillis());
                                    partnerUserModel.setCampusId(selectedCampus.getCampusId());

                                    partnerRef.child(user.getUid()).setValue(partnerUserModel)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        hud.dismiss();
                                                        Toast.makeText(SignupActivity.this,"Registration Success",Toast.LENGTH_SHORT).show();
                                                        goToMainActivity(partnerUserModel);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            hud.dismiss();
                                            Toast.makeText(SignupActivity.this, "Creating Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hud.dismiss();
                        if(e.getMessage().contains("already associated with a different")){
                            Toast.makeText(SignupActivity.this, "Email Account Already Added with different account", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(SignupActivity.this, "Linking Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignupActivity.this)
                .setTitle("Cancel Process?")
                .setMessage("Are you sure want to cancel the registration process?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SignupActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void goToMainActivity(PartnerUserModel userModel) {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        Common.currentPartnerUser = userModel;
                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                Common.currentPartnerUser = userModel;
                Common.createToken(SignupActivity.this,task.getResult().getToken());
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();

            }
        });
    }

    @Subscribe
    public void onCampusClicked(CampusSelectedEvent event){
        selectedCampus = event.getSelectedCampus();
        textCampusName.setText(new StringBuilder()
                .append(event.getSelectedCampus().getName()));
        dialogCampus.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


}