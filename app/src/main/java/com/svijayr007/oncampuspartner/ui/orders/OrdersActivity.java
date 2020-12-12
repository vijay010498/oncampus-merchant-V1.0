package com.svijayr007.oncampuspartner.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.svijayr007.oncampuspartner.HomeActivity;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.eventBus.DeliveryAgentOutForDeliveryEvent;
import com.svijayr007.oncampuspartner.model.DeliveryOrderModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersActivity extends AppCompatActivity {
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    String[] tabsTitles;
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        //init
        ButterKnife.bind(this);
        initViewPager();

        hud =  KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(this.getResources().getColor(R.color.colorPrimaryDark))
                .setCancellable(false)
                .setLabel("Processing")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
    }

    private void initViewPager() {
        List<Fragment> orderFilterList = new ArrayList<>();
        orderFilterList.add(new NewOrdersFragment().OrderFilter(0));
        orderFilterList.add(new NewOrdersFragment().OrderFilter(1));
        orderFilterList.add(new NewOrdersFragment().OrderFilter(2));
        orderFilterList.add(new NewOrdersFragment().OrderFilter(-2));
        orderFilterList.add(new NewOrdersFragment().OrderFilter(3));
        orderFilterList.add(new NewOrdersFragment().OrderFilter(-1));
        orderFilterList.add(new NewOrdersFragment().OrderFilter(-3));
        tabsTitles = new String[]{"New Orders", "Preparing","Out for delivery","READY FOR PICKUP","Delivered","Cancelled By you","Cancelled By Customer"};
        viewPager.setAdapter(new TabsSetup(orderFilterList, getSupportFragmentManager(),tabsTitles));
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onDeliveryAgentSelect(DeliveryAgentOutForDeliveryEvent event){
        Toast.makeText(this, ""+event.getSelectedDeliveryAgent().getPhone(), Toast.LENGTH_SHORT).show();
        hud.show();
        //Create delivery order
        DeliveryOrderModel deliveryOrderModel = new DeliveryOrderModel();
        deliveryOrderModel.setPartnerUserModel(Common.currentPartnerUser);
        deliveryOrderModel.setAgentId(event.getSelectedDeliveryAgent().getAgentId());
        deliveryOrderModel.setOrderModel(event.getDeliveryOrder());
        deliveryOrderModel.setDeliveryAgentModel(event.getSelectedDeliveryAgent());
        deliveryOrderModel.setDeliveryStatus(0); // 0 = Not Delivered //1 = delivered

        //First create delivery order
        FirebaseDatabase.getInstance(Common.deliveryOrdersDB)
                .getReference(Common.DELIVERY_ORDER_REF)
                .child(event.getDeliveryOrder().getKey())
                .setValue(deliveryOrderModel)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hud.dismiss();
                        Toast.makeText(OrdersActivity.this, "Server Busy!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Change Order Status
                Map<String, Object> updateDate = new HashMap<>();
                updateDate.put("orderStatus",2);
                FirebaseDatabase.getInstance(Common.ordersDB)
                        .getReference(Common.ORDER_REF)
                        .child(event.getDeliveryOrder().getKey())
                        .updateChildren(updateDate)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hud.dismiss();
                                Toast.makeText(OrdersActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();


                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hud.dismiss();
                        Toast.makeText(OrdersActivity.this, "Order Sent To Delivery Agent, Update Success", Toast.LENGTH_SHORT).show();

                        //Navigate to Orders
                        Intent homeIntent = new Intent(OrdersActivity.this, HomeActivity.class);
                        homeIntent.putExtra("isNavigateViewOrder",true);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        overridePendingTransition(0,0);
                        startActivity(homeIntent);
                        overridePendingTransition(0,0);
                        finish();
                    }
                });

            }
        });


    }
}