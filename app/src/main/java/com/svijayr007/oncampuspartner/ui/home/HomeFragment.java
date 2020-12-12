package com.svijayr007.oncampuspartner.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.adapter.SettlementAdapter;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.common.SpacesItemDecoration;
import com.svijayr007.oncampuspartner.model.OrderModel;
import com.svijayr007.oncampuspartner.model.RestaurantModel;
import com.svijayr007.oncampuspartner.model.SettlementModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    private KProgressHUD hud;


    @BindView(R.id.restaurant_image)
    ImageView restaurant_image;
    @BindView(R.id.text_restaurant_name)
    TextView text_restaurant_name;
    @BindView(R.id.text_res_food_category)
    TextView text_res_food_category;
    @BindView(R.id.text_res_address)
    TextView text_res_address;
    @BindView(R.id.restaurant_rating)
    TextView restaurant_rating;
    @BindView(R.id.restaurant_rating_count)
    TextView restaurant_rating_count;
    @BindView(R.id.total_orders_count_allTime)
    TextView total_orders_count_allTime;
    @BindView(R.id.total_orders_amount_allTime)
    TextView total_orders_amount_allTime;
    @BindView(R.id.total_orders_count_today)
    TextView total_orders_count_today;
    @BindView(R.id.total_orders_amount_today)
    TextView total_orders_amount_today;
    @BindView(R.id.total_orders_cancelled_by_partner)
    TextView total_orders_cancelled_by_partner;
    @BindView(R.id.total_orders_cancelled_by_user)
    TextView total_orders_cancelled_by_user;
    @BindView(R.id.total_orders_cancelled_by_partner_today)
    TextView total_orders_cancelled_by_partner_today;
    @BindView(R.id.total_orders_cancelled_by_user_today)
    TextView total_orders_cancelled_by_user_today;

    //Current order's
    @BindView(R.id.current_orders_new_orders)
    TextView current_orders_new_orders;
    @BindView(R.id.current_orders_preparing)
    TextView current_orders_preparing;
    @BindView(R.id.current_orders_out_for_delivery)
    TextView current_orders_out_for_delivery;
    @BindView(R.id.current_orders_ready_for_pickup)
    TextView current_orders_ready_for_pickup;
    @BindView(R.id.current_orders_delivered_pickedUp)
    TextView current_orders_delivered_pickedUp;
    @BindView(R.id.current_orders_cancelled_by_partner)
    TextView current_orders_cancelled_by_partner;
    @BindView(R.id.current_orders_cancelled_by_user)
    TextView current_orders_cancelled_by_user;


    //Yesterday sales
    @BindView(R.id.total_orders_count_yesterday)
    TextView total_orders_count_yesterday;
    @BindView(R.id.total_orders_cancelled_by_partner_yesterday)
    TextView total_orders_cancelled_by_partner_yesterday;
    @BindView(R.id.total_orders_cancelled_by_user_yesterday)
    TextView total_orders_cancelled_by_user_yesterday;
    @BindView(R.id.total_orders_amount_yesterday)
    TextView total_orders_amount_yesterday;

    //weekly so far
    @BindView(R.id.total_orders_count_weekly)
    TextView total_orders_count_weekly;
    @BindView(R.id.total_orders_cancelled_by_partner_weekly)
    TextView total_orders_cancelled_by_partner_weekly;
    @BindView(R.id.total_orders_cancelled_by_user_weekly)
    TextView total_orders_cancelled_by_user_weekly;
    @BindView(R.id.total_orders_amount_weekly)
    TextView total_orders_amount_weekly;

    //last 5 Settlements
    @BindView(R.id.recycler_settlements)
    RecyclerView recycler_settlements;
    private SettlementViewModel settlementViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Init important
        ButterKnife.bind(this, root);
        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                .setCancellable(false)
                .setLabel("Loading")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
        setUiData();
        setDashboardData();
        settlementViewModel =
                new ViewModelProvider(this).get(SettlementViewModel.class);
        //
        recycler_settlements.setHasFixedSize(true);
        recycler_settlements.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_settlements.addItemDecoration(new SpacesItemDecoration(5));
        //
        setSettlementsData();

        return root;

    }


    private void setUiData() {
        hud.show();
        FirebaseDatabase.getInstance(Common.restaurantDB)
                .getReference(Common.RESTAURANT_REF)
                .child(Common.currentPartnerUser.getRestaurant())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            RestaurantModel restaurantModel = snapshot.getValue(RestaurantModel.class);
                            restaurantModel.setKey(snapshot.getKey());
                            if (getActivity() == null) {
                                return;
                            }
                            //Set details
                            Glide.with(getActivity())
                                    .load(restaurantModel.getImageUrl())
                                    .placeholder(R.drawable.ic_restaurant)
                                    .into(restaurant_image);
                            text_restaurant_name.setText(new StringBuilder()
                            .append(restaurantModel.getName()));
                            text_res_food_category.setText(new StringBuilder()
                            .append(restaurantModel.getFoodCategories()));
                            text_res_address.setText(new StringBuilder()
                            .append(restaurantModel.getCampus()));
                            restaurant_rating.setText(new StringBuilder()
                            .append(restaurantModel.getRating()));
                            restaurant_rating_count.setText(new StringBuilder()
                            .append(restaurantModel.getRatingCount())
                            .append("+ ratings"));

                            //Rating animation
                            Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in_repeat);
                            animation.setDuration(2000);
                            restaurant_rating_count.startAnimation(animation);

                            hud.dismiss();

                        }else {
                            hud.dismiss();
                            Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        hud.dismiss();
                        Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();

                    }
                });

    }
    private void setDashboardData() {
        hud.show();
        FirebaseDatabase.getInstance(Common.ordersDB)
                .getReference(Common.ORDER_REF)
                .orderByChild("restaurantId")
                .equalTo(Common.currentPartnerUser.getRestaurant())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            //order's count All time
                            int order_count =0;
                            int total_amount = 0;
                            int order_cancelled_by_partner = 0;
                            int order_cancelled_by_user = 0;
                            for(DataSnapshot orderSnapShot : snapshot.getChildren()){
                                OrderModel tempModel = orderSnapShot.getValue(OrderModel.class);
                                if(tempModel.getOrderStatus() == 3){
                                    order_count++;
                                    total_amount+=tempModel.getPayToRestaurantAfterCommissionAmount();
                                }
                                else if(tempModel.getOrderStatus() == -1){
                                    //Cancelled by partner
                                    order_cancelled_by_partner++;
                                }
                                else if(tempModel.getOrderStatus() == -3){
                                    //Cancelled by user
                                    order_cancelled_by_user++;
                                }
                            }
                            total_orders_count_allTime.setText(new StringBuilder()
                            .append(order_count));
                            total_orders_amount_allTime.setText(new StringBuilder()
                            .append("₹")
                            .append(total_amount));
                            total_orders_cancelled_by_partner.setText(new StringBuilder()
                            .append(order_cancelled_by_partner));
                            total_orders_cancelled_by_user.setText(new StringBuilder()
                            .append(order_cancelled_by_user));

                            //Get current date in timestamp
                            Date date = new Date();
                            SimpleDateFormat DateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String today = DateFormat.format(date);
                            try {
                                Date dateToday = DateFormat.parse(today);
                                //using calender
                                Calendar calendarToday = Calendar.getInstance();
                                calendarToday.setTime(dateToday);
                                Long todayStamp = calendarToday.getTimeInMillis();

                                //Tomorrow date
                                Calendar calendarTomorrow = Calendar.getInstance();
                                calendarTomorrow.setTime(dateToday);
                                calendarTomorrow.add(Calendar.DATE,1);
                                Long tomorrowStamp = calendarTomorrow.getTimeInMillis();

                                int order_count_today = 0;
                                int total_amount_today = 0;
                                int total_cancelled_by_partner_today = 0;
                                int total_cancelled_user_today = 0;


                                for(DataSnapshot orderSnapShot : snapshot.getChildren()){
                                    OrderModel tempModel = orderSnapShot.getValue(OrderModel.class);

                                        if (tempModel.getCreateDate() >= todayStamp && tempModel.getCreateDate() <= tomorrowStamp) {
                                            if(tempModel.getOrderStatus() == 3) {
                                                order_count_today++;
                                                total_amount_today += tempModel.getPayToRestaurantAfterCommissionAmount();
                                            }
                                            else if(tempModel.getOrderStatus() == -1){
                                                total_cancelled_by_partner_today++;
                                            }
                                            else if(tempModel.getOrderStatus() == -3){
                                                total_cancelled_user_today++;
                                            }

                                    }
                                }

                                total_orders_count_today.setText(new StringBuilder()
                                .append(order_count_today));
                                total_orders_amount_today.setText(new StringBuilder()
                                .append("₹")
                                .append(total_amount_today));
                                total_orders_cancelled_by_partner_today.setText(new StringBuilder()
                                .append(total_cancelled_by_partner_today));
                                total_orders_cancelled_by_user_today.setText(new StringBuilder()
                                .append(total_cancelled_user_today));
                            }catch (Exception e){
                                e.printStackTrace();
                            };

                            //Weekly sales
                            try{
                                Date dateToday = DateFormat.parse(today);

                                //using calendar
                                Calendar calendarWeek = Calendar.getInstance();
                                calendarWeek.setTime(dateToday);
                                calendarWeek.add(Calendar.WEEK_OF_YEAR,-1);
                                Long weekStamp = calendarWeek.getTimeInMillis();
                                Log.i("WEEk TIME STAMP:", String.valueOf(weekStamp));

                                int order_count_weekly = 0;
                                int total_amount_weekly = 0;
                                int total_cancelled_by_partner_weekly = 0;
                                int total_cancelled_user_weekly = 0;

                                for(DataSnapshot orderSnapShot : snapshot.getChildren()){
                                    OrderModel tempModel = orderSnapShot.getValue(OrderModel.class);

                                    if(tempModel.getCreateDate() >= weekStamp){
                                        if(tempModel.getOrderStatus() == 3){
                                            order_count_weekly++;
                                            total_amount_weekly+= tempModel.getPayToRestaurantAfterCommissionAmount();
                                        }
                                        else if(tempModel.getOrderStatus() == -1){
                                            total_cancelled_by_partner_weekly++;
                                        }
                                        else if(tempModel.getOrderStatus() == -3){
                                            total_cancelled_user_weekly++;
                                        }
                                    }

                                }

                                total_orders_count_weekly.setText(new StringBuilder()
                                .append(order_count_weekly));
                                total_orders_cancelled_by_partner_weekly.setText(new StringBuilder()
                                .append(total_cancelled_by_partner_weekly));
                                total_orders_cancelled_by_user_weekly.setText(new StringBuilder()
                                .append(total_cancelled_user_weekly));
                                total_orders_amount_weekly.setText(new StringBuilder()
                                .append("₹")
                                .append(total_amount_weekly));

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            //Yesterday sale's
                            try{
                                Date dateToday = DateFormat.parse(today);

                                //using calender
                                Calendar calenderToday = Calendar.getInstance();
                                calenderToday.setTime(dateToday);
                                Long todayStamp = calenderToday.getTimeInMillis();



                                //yesterday date
                                Calendar calendarYesterday = Calendar.getInstance();
                                calendarYesterday.setTime(dateToday);
                                calendarYesterday.add(Calendar.DATE,-1);
                                Long yesterdayStamp = calendarYesterday.getTimeInMillis();


                                int order_count_yesterday = 0;
                                int total_amount_yesterday = 0;
                                int total_cancelled_by_partner_yesterday = 0;
                                int total_cancelled_user_yesterday = 0;

                                for(DataSnapshot orderSnapShot : snapshot.getChildren()){
                                    OrderModel tempModel = orderSnapShot.getValue(OrderModel.class);

                                    if(tempModel.getCreateDate() >= yesterdayStamp && tempModel.getCreateDate() <todayStamp){
                                        if(tempModel.getOrderStatus() == 3){
                                            order_count_yesterday++;
                                            total_amount_yesterday += tempModel.getPayToRestaurantAfterCommissionAmount();
                                        }
                                        else if (tempModel.getOrderStatus() == -1){
                                            total_cancelled_by_partner_yesterday++;
                                        }
                                        else if(tempModel.getOrderStatus() == -3){
                                            total_cancelled_user_yesterday++;
                                        }
                                    }

                                }
                                total_orders_count_yesterday.setText(new StringBuilder()
                                .append(order_count_yesterday));
                                total_orders_cancelled_by_partner_yesterday.setText(new StringBuilder()
                                .append(total_cancelled_by_partner_yesterday));
                                total_orders_cancelled_by_user_yesterday.setText(new StringBuilder()
                                .append(total_cancelled_user_yesterday));
                                total_orders_amount_yesterday.setText(new StringBuilder()
                                .append("₹")
                                .append(total_amount_yesterday));
                            }catch(Exception e){
                                e.printStackTrace();
                            }


                            //Current order's data
                            try {
                                Date dateToday = DateFormat.parse(today);
                                //using calender
                                Calendar calendarToday = Calendar.getInstance();
                                calendarToday.setTime(dateToday);
                                Long todayStamp = calendarToday.getTimeInMillis();

                                //Tomorrow date
                                Calendar calendarTomorrow = Calendar.getInstance();
                                calendarTomorrow.setTime(dateToday);
                                calendarTomorrow.add(Calendar.DATE,1);
                                Long tomorrowStamp = calendarTomorrow.getTimeInMillis();

                                int current_new_orders = 0;
                                int current_preparing = 0;
                                int current_out_for_delivery = 0;
                                int current_ready_for_pickup = 0;
                                int current_delivered_pickedup = 0;
                                int current_cancelled_by_partner = 0;
                                int current_cancelled_by_user = 0;

                                for(DataSnapshot orderSnapShot : snapshot.getChildren()){
                                    OrderModel tempModel = orderSnapShot.getValue(OrderModel.class);
                                    if (tempModel.getCreateDate() >= todayStamp && tempModel.getCreateDate() <= tomorrowStamp) {
                                        int status = tempModel.getOrderStatus();
                                        if (status == 0) {
                                            current_new_orders++;
                                        } else if (status == 1) {
                                            current_preparing++;
                                        } else if (status == 2) {
                                            current_out_for_delivery++;
                                        } else if (status == -2) {
                                            current_ready_for_pickup++;
                                        } else if (status == 3) {
                                            current_delivered_pickedup++;
                                        } else if (status == -1) {
                                            current_cancelled_by_partner++;
                                        } else if (status == -3) {
                                            current_cancelled_by_user++;
                                        }
                                    }
                                }
                                current_orders_new_orders.setText(new StringBuilder()
                                        .append(current_new_orders));
                                current_orders_preparing.setText(new StringBuilder()
                                        .append(current_preparing));
                                current_orders_out_for_delivery.setText(new StringBuilder()
                                        .append(current_out_for_delivery));
                                current_orders_ready_for_pickup.setText(new StringBuilder()
                                        .append(current_ready_for_pickup));
                                current_orders_delivered_pickedUp.setText(new StringBuilder()
                                        .append(current_delivered_pickedup));
                                current_orders_cancelled_by_partner.setText(new StringBuilder()
                                        .append(current_cancelled_by_partner));
                                current_orders_cancelled_by_user.setText(new StringBuilder()
                                        .append(current_cancelled_by_user));
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            hud.dismiss();
                        }
                        else {
                            hud.dismiss();
                            Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //No orders
                        hud.dismiss();
                    }
                });
    }


    private void setSettlementsData() {
        settlementViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        settlementViewModel.getSettlementMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<SettlementModel>>() {
            @Override
            public void onChanged(List<SettlementModel> settlementModelList) {
                Collections.reverse(settlementModelList);
               /* for(int i=0;i<settlementModelList.size();i++){
                    for(String key : settlementModelList.get(i).getOrders().keySet()){
                        Toast.makeText(getContext(), ""+key, Toast.LENGTH_SHORT).show();
                    }
                }*/
                SettlementAdapter settlementAdapter = new SettlementAdapter(getContext(),settlementModelList);
                recycler_settlements.setAdapter(settlementAdapter);
            }
        });



    }

}