package com.svijayr007.oncampuspartner.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.suke.widget.SwitchButton;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.callback.IRecyclerClickListener;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.model.FoodModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyFoodListAdapter extends RecyclerView.Adapter<MyFoodListAdapter.MyViewHolder>{
    private Context context;
    private List<FoodModel> foodModelList;
    private KProgressHUD hud ;

    public MyFoodListAdapter(Context context, List<FoodModel> foodModelList) {
        this.context = context;
        this.foodModelList = foodModelList;
        hud =  KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .setCancellable(false)
                .setLabel("Loading")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_food_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Check availability
        if(foodModelList.get(position).isAvailable()){
            holder.food_available_switch.setChecked(true);
            holder.available_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_available_24));
        }
        else {
            holder.food_available_switch.setChecked(false);
            holder.available_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_not_available_24));
        }


        //UI
        if(foodModelList.get(position)!=null)
            Glide.with(context)
            .load(foodModelList.get(position).getImage())
            .placeholder(R.drawable.ic_food)
            .into(holder.img_food_image);
        if(foodModelList.get(position)!=null)
            holder.txt_food_price.setText(new StringBuilder("₹")
                    .append(foodModelList.get(position).getPrice()));
        if(foodModelList.get(position).getDescription() == null || TextUtils.isEmpty(foodModelList.get(position).getDescription()))
            holder.food_desc.setVisibility(View.GONE);
        else
            holder.food_desc.setText(new StringBuilder()
            .append(foodModelList.get(position).getDescription()));
        if(foodModelList.get(position)!=null)
            holder.txt_food_name.setText(new StringBuilder("")
            .append(foodModelList.get(position).getName()));

        /****************************************************************************************************/

        //veg non-veg image
        //default veg
        if(foodModelList.get(position).getIsVeg() == 0){
            holder.image_veg_non_veg.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_non_veg));
        }
        else if(foodModelList.get(position).getIsVeg() == 1){
            holder.image_veg_non_veg.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_veg));
        }
        else {
            holder.image_veg_non_veg.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_non_veg));
        }



        /****************************************************************************************************/

        holder.txt_food_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+foodModelList.get(position).getDescription(), Toast.LENGTH_SHORT).show();

            }
        });

        //switch listener
        holder.food_available_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                hud.show();
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("available",isChecked);

                //Update to firebase
                FirebaseDatabase.getInstance(Common.restaurantDB)
                        .getReference(Common.RESTAURANT_REF)
                        .child(Common.currentPartnerUser.getRestaurant())
                        .child(Common.CATEGORY_REF)
                        .child(foodModelList.get(position).getMenuId())
                        .child(Common.FOOD_REF)
                        .child(String.valueOf(position))
                        .updateChildren(updateData)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                holder.food_available_switch.setChecked(!isChecked);
                                hud.dismiss();

                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(isChecked){
                                holder.available_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_available_24));
                            }else {
                                holder.available_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_not_available_24));
                            }
                            String tempStr = "";
                            if(isChecked)
                                tempStr = "is now Available";
                            else
                                tempStr = "is not Available now";
                            Toast.makeText(context, ""+foodModelList.get(position).getName() + new StringBuilder(" ").append(tempStr) , Toast.LENGTH_SHORT).show();
                            foodModelList.get(position).setAvailable(isChecked);
                            hud.dismiss();
                        }

                    }
                });





                /*foodModelList.get(position).setAvailable(isChecked);
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("available",isChecked);

                //Update to firebase
                FirebaseDatabase.getInstance(Common.restaurantDB)
                        .getReference(Common.RESTAURANT_REF)
                        .child(Common.currentPartnerUser.getRestaurant())
                        .child(Common.CATEGORY_REF)
                        .child(foodModelList.get(position).getMenuId())
                        .child(Common.FOOD_REF)
                        .child(String.valueOf(position))
                        .updateChildren(updateData)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                foodModelList.get(position).setAvailable(!isChecked);
                                holder.food_available_switch.setChecked(!isChecked);
                                if(holder.food_available_switch.isChecked()){
                                    holder.available_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_available_24));
                                }
                                else {
                                    holder.available_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_not_available_24));
                                }
                                hud.dismiss();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(isChecked){
                                holder.available_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_available_24));
                            }else {
                                holder.available_status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_not_available_24));
                            }
                            String tempStr = "";
                            if(isChecked)
                                tempStr = "is now Available";
                            else
                                tempStr = "is not Available now";
                            Toast.makeText(context, ""+foodModelList.get(position).getName() + new StringBuilder(" ").append(tempStr) , Toast.LENGTH_SHORT).show();
                            hud.dismiss();
                        }

                    }
                });*/

            }
        });
        //Event
        holder.setListener((view, pos) -> {

        });
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Unbinder unbinder;
        IRecyclerClickListener listener;
        @BindView(R.id.txt_food_name)
        TextView txt_food_name;
        @BindView(R.id.txt_food_price)
        TextView txt_food_price;
        @BindView(R.id.img_food_image)
        ImageView img_food_image;
        @BindView(R.id.food_desc)
        TextView food_desc;
        @BindView(R.id.food_available_switch)
        SwitchButton food_available_switch;
        @BindView(R.id.available_status)
        ImageView available_status;

        @BindView(R.id.image_veg_non_veg)
        ImageView image_veg_non_veg;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v,getAdapterPosition());
        }
    }
}
