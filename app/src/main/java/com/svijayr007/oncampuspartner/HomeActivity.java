package com.svijayr007.oncampuspartner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.ui.account.AccountActivity;
import com.svijayr007.oncampuspartner.ui.deliveryAgents.DeliveryAgentFragment;
import com.svijayr007.oncampuspartner.ui.foods.FoodActivity;
import com.svijayr007.oncampuspartner.ui.home.HomeFragment;
import com.svijayr007.oncampuspartner.ui.orders.OrdersActivity;

import io.reactivex.disposables.CompositeDisposable;

public class HomeActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    //Navigation
    private boolean isNavigateOrders = false;


    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getArgs();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //FCM Subscription
        /************************************************************************************/
        subscribeToGlobalTopic(Common.globalTopic);
        subscribeToGlobalPartnerTopic(Common.globalPartnerTopic);
        subscribeToPartnerCampusTopic(new StringBuilder()
        .append("partner")
        .append("_")
        .append(Common.currentPartnerUser.getCampusId()).toString());
        /************************************************************************************/

        if(isNavigateOrders){
            startActivity(new Intent(HomeActivity.this, OrdersActivity.class));
        }

        //Bottom navigation - default home fragment
        loadFragment(new HomeFragment());

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        //Navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        if(bottomNavigationView.getSelectedItemId()!=R.id.nav_home)
                            fragment = new HomeFragment();
                        break;
                    case R.id.nav_MyRestaurant:
                        startActivity(new Intent(HomeActivity.this, FoodActivity.class));
                        break;
                    case R.id.nav_orders:
                        startActivity(new Intent(HomeActivity.this, OrdersActivity.class));
                        break;
                    case R.id.nav_deliveryAgents:
                        if(bottomNavigationView.getSelectedItemId() != R.id.nav_deliveryAgents)
                            fragment = new DeliveryAgentFragment();
                        break;
                    case R.id.nav_account:
                        startActivity(new Intent(HomeActivity.this, AccountActivity.class));
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }

    private void getArgs() {
        isNavigateOrders = getIntent().getBooleanExtra("isNavigateViewOrder",false);
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return  true;
        }
        return false;
    }



    //FCM Subscription
    /************************************************************************************/
    private void subscribeToGlobalPartnerTopic(String globalPartnerTopic) {
        FirebaseMessaging.getInstance()
                .subscribeToTopic(globalPartnerTopic)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "Global Partner Topic"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("FCM ","Subscriber to Global Partner");

            }
        });

    }

    private void subscribeToGlobalTopic(String globalTopic) {
        FirebaseMessaging.getInstance()
                .subscribeToTopic(globalTopic)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       Toast.makeText(HomeActivity.this, "Global Topic"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("FCM ","Subscriber to Global");

            }
        });
    }
    private void subscribeToPartnerCampusTopic(String partner_campus_id_topic) {
        FirebaseMessaging.getInstance()
                .subscribeToTopic(partner_campus_id_topic)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "Partner_Campus_TOpic"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("FCM ","Subscriber to partner_campus");

            }
        });
    }


    /************************************************************************************/

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }




}