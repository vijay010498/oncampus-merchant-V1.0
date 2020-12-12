package com.svijayr007.oncampuspartner.ui.foods;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.suke.widget.SwitchButton;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.adapter.MyCategoriesAdapter;
import com.svijayr007.oncampuspartner.adapter.MyFabMenuAdapter;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.common.SpacesItemDecoration;
import com.svijayr007.oncampuspartner.eventBus.ResMenuScrollEvent;
import com.svijayr007.oncampuspartner.model.CategoryModel;
import com.svijayr007.oncampuspartner.model.RestaurantModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodActivity extends AppCompatActivity {

    private KProgressHUD hud;
    private MenuViewModel menuViewModel;
    private  AlertDialog dialogMenuFab;

    //Binding
    @BindView(R.id.image_refresh)
    ImageView image_refresh;
    @BindView(R.id.image_close)
    ImageView image_close;
    @BindView(R.id.restaurant_rating)
    TextView restaurant_rating;
    @BindView(R.id.restaurant_prep_time)
    TextView restaurant_prep_time;
    @BindView(R.id.restaurant_price_for_2)
    TextView restaurant_price_for_2;
    @BindView(R.id.restaurant_rating_count)
    TextView restaurant_rating_count;
    @BindView(R.id.recycler_menu_res)
    RecyclerView recycler_menu_res;
    @BindView(R.id.restaurant_image)
    ImageView restaurant_image;
    @BindView(R.id.res_title_name)
    TextView res_title_name;
    @BindView(R.id.text_restaurant_name)
    TextView text_restaurant_name;
    @BindView(R.id.text_res_food_category)
    TextView text_res_food_category;
    @BindView(R.id.text_res_address)
    TextView text_res_address;
    @BindView(R.id.restaurant_opened_switch)
    SwitchButton restaurant_opened_switch;

    //Menu fab
    @BindView(R.id.menu_fab)
    ExtendedFloatingActionButton menu_fab;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    //Adapter
    MyCategoriesAdapter categoriesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        //Hud before open
        hud = KProgressHUD.create(FoodActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                .setCancellable(false)
                .setLabel("Loading")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
        //init important
        ButterKnife.bind(this);
        menuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        setUIData();
        setListener();
    }

    private void setListener() {
        image_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
                startActivity(getIntent());
                overridePendingTransition(0,0);
            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        restaurant_opened_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                hud.show();
                String value = "";
                if(isChecked){
                    value = "true";
                }
                else if(!isChecked) {
                    value = "false";

                }
                String finalValue = value;
                FirebaseDatabase.getInstance(Common.restaurantDB)
                        .getReference(Common.RESTAURANT_REF)
                        .child(Common.currentPartnerUser.getRestaurant())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Map<String, Object> updateData = new HashMap<>();
                                    updateData.put("isOpened", finalValue);
                                    FirebaseDatabase.getInstance(Common.restaurantDB)
                                            .getReference(Common.RESTAURANT_REF)
                                            .child(Common.currentPartnerUser.getRestaurant())
                                            .updateChildren(updateData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        hud.dismiss();
                                                        if(isChecked){
                                                            Toast.makeText(FoodActivity.this, "You are open now", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else if(!isChecked){
                                                            Toast.makeText(FoodActivity.this, "You are closed now!", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }else {
                                                        hud.dismiss();
                                                        Toast.makeText(FoodActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                        restaurant_opened_switch.setChecked(!isChecked);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            hud.dismiss();
                                            Toast.makeText(FoodActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            restaurant_opened_switch.setChecked(!isChecked);
                                        }
                                    });


                                }else {
                                    hud.dismiss();
                                    Toast.makeText(FoodActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    restaurant_opened_switch.setChecked(!isChecked);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                hud.dismiss();
                                Toast.makeText(FoodActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                restaurant_opened_switch.setChecked(!isChecked);
                            }
                        });
            }
        });
        
        //Menu fab
        menu_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodActivity.this,android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
                builder.setTitle(new StringBuilder()
                        .append("Your Restaurant's")
                        .append(" Menu"));
                View v = LayoutInflater.from(FoodActivity.this).inflate(R.layout.layout_fab_res_menu,null);
                RecyclerView recycler_menu_fab = v.findViewById(R.id.recycler_menu_fab);
                menuViewModel.getCategoryListMutable().observe(FoodActivity.this, new Observer<List<CategoryModel>>() {
                    @Override
                    public void onChanged(List<CategoryModel> categoryModels) {
                        MyFabMenuAdapter myFabMenuAdapter = new MyFabMenuAdapter(FoodActivity.this,categoryModels);
                        recycler_menu_fab.setAdapter(myFabMenuAdapter);
                        recycler_menu_fab.setLayoutManager(new LinearLayoutManager(FoodActivity.this));
                        recycler_menu_fab.setVerticalScrollBarEnabled(true);
                        recycler_menu_fab.addItemDecoration(new DividerItemDecoration(recycler_menu_fab.getContext(), DividerItemDecoration.VERTICAL));

                    }
                });
                builder.setView(v);
                dialogMenuFab = builder.create();
                dialogMenuFab.show();

            }
        });

    }

    private void setUIData() {
        hud.show();
        FirebaseDatabase.getInstance(Common.restaurantDB)
                .getReference(Common.RESTAURANT_REF)
                .child(Common.currentPartnerUser.getRestaurant())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        RestaurantModel resModel = snapshot.getValue(RestaurantModel.class);
                        //Set data
                        Glide.with(getApplicationContext())
                                .load(resModel.getImageUrl())
                                .placeholder(R.drawable.ic_restaurant)
                                .into(restaurant_image);
                        res_title_name.setText(new StringBuilder()
                        .append(resModel.getName())
                        .append(" "));

                        text_restaurant_name.setText(new StringBuilder()
                        .append(resModel.getName()));

                        text_res_food_category.setText(new StringBuilder()
                        .append(resModel.getFoodCategories()));

                        text_res_address.setText(new StringBuilder()
                        .append(resModel.getCampus()));

                        restaurant_rating.setText(new StringBuilder()
                        .append(resModel.getRating()));

                        restaurant_prep_time.setText(new StringBuilder()
                        .append(resModel.getPrepTime())
                        .append(" ")
                        .append("mins"));

                        restaurant_price_for_2.setText(new StringBuilder("₹")
                        .append(resModel.getPriceForTwoPeople()));

                        restaurant_rating_count.setText(new StringBuilder()
                        .append(resModel.getRatingCount())
                        .append("+ ")
                        .append("ratings"));
                        recycler_menu_res.setHasFixedSize(true);
                        recycler_menu_res.setNestedScrollingEnabled(true);
                        recycler_menu_res.addItemDecoration(new SpacesItemDecoration(8));

                        //Opened
                        if(resModel.getIsOpened().equals("false")){
                            restaurant_opened_switch.setChecked(false);
                        }
                        //Rating animation
                        Animation animation = AnimationUtils.loadAnimation(FoodActivity.this,R.anim.fade_in_repeat);
                        animation.setDuration(2000);
                        restaurant_rating_count.startAnimation(animation);
                        hud.dismiss();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i("ERROR",error.getMessage());
                        hud.dismiss();
                    }
                });
        //set Restaurant menu
        menuViewModel.getCategoryListMutable().observe(this, categoryModels -> {
            //Adapter
            categoriesAdapter = new MyCategoriesAdapter(FoodActivity.this,categoryModels);
            recycler_menu_res.setAdapter(categoriesAdapter);
            recycler_menu_res.setLayoutManager(new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.VERTICAL,false));
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMenuFabClicked(ResMenuScrollEvent event){
        dialogMenuFab.dismiss();
        recycler_menu_res.post(new Runnable() {
            @Override
            public void run() {
                float y = recycler_menu_res.getY() + recycler_menu_res.getChildAt(event.getPos()).getY();
                nestedScrollView.smoothScrollTo(0, (int) y,500);
                recycler_menu_res.getChildAt(event.getPos()).setBackgroundColor(getResources().getColor(R.color.quantum_orange));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        recycler_menu_res.getChildAt(event.getPos()).setBackgroundColor(getResources().getColor(R.color.white));

                    }
                },1000);
            }
        });
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