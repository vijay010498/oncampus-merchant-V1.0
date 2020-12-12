package com.svijayr007.oncampuspartner.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.callback.IRecyclerClickListener;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.common.SpacesItemDecoration;
import com.svijayr007.oncampuspartner.model.DeliveryAgentModel;
import com.svijayr007.oncampuspartner.model.OrderModel;
import com.svijayr007.oncampuspartner.ui.deliveryAgents.DeliveryAgentViewModel;
import com.svijayr007.oncampuspartner.ui.order_detail.OrderDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import spencerstudios.com.bungeelib.Bungee;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    Context context;
    List<OrderModel> orderModelList;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private KProgressHUD hud;
    private AlertDialog dialogsOtp;
    private DeliveryAgentViewModel deliveryAgentViewModel;
    private  AlertDialog dialogDeliveryAgent;


    public MyOrderAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .setCancellable(false)
                .setLabel("Loading")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
        deliveryAgentViewModel = ViewModelProviders.of((FragmentActivity) context).get(DeliveryAgentViewModel.class);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
        .inflate(R.layout.layout_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*****************************************************************************************************/
        //Set data
        holder.txt_order_name.setText(new StringBuilder()
        .append(orderModelList.get(position).getUserName()));

        holder.txt_order_number.setText(new StringBuilder()
        .append("#")
        .append(orderModelList.get(position).getOrderNumber()));

        if(!orderModelList.get(position).isPickup())
            holder.txt_delivery_location.setText(new StringBuilder()
            .append(orderModelList.get(position).getShippingAddress()));
        else
            holder.txt_delivery_location.setVisibility(View.GONE);

        holder.txt_order_amount_restaurant_settlement.setText(new StringBuilder()
        .append("₹")
        .append(orderModelList.get(position).getPayToRestaurantAfterCommissionAmount()));

/*****************************************************************************************************/
        //Order date
        calendar.setTimeInMillis(orderModelList.get(position).getCreateDate());
        Date date = new Date(orderModelList.get(position).getCreateDate());
        holder.txt_order_date.setText(new StringBuilder(Common.getDateOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
                .append(" ").append(simpleDateFormat.format(date)));

        /*****************************************************************************************************/
        //Order items
        int numberOfItems = orderModelList.get(position).getCartItemList().size();
        holder.txt_order_items.setText("");
        for(int i=0;i<numberOfItems;i++){
            holder.txt_order_items.append(new StringBuilder()
            .append(orderModelList.get(position).getCartItemList().get(i).getFoodName())
            .append(" x ")
            .append(orderModelList.get(position).getCartItemList().get(i).getFoodQuantity()));

            if(i+1 < numberOfItems)
                holder.txt_order_items.append(", \n");
            else
                holder.txt_order_items.append(".");
        }
        //order type
        if(orderModelList.get(position).isPickup()){
            holder.txt_order_type.setText(new StringBuilder()
            .append(" Pickup "));
        }
        else {
            holder.txt_order_type.setText(new StringBuilder()
                    .append(" Delivery "));
        }
        /*****************************************************************************************************/
        //Cooking instructions
        if(!orderModelList.get(position).getComment().equals("")){
            holder.txt_order_instructions.setVisibility(View.VISIBLE);
            holder.txt_order_instructions.setText(new StringBuilder()
            .append(orderModelList.get(position).getComment()));
        }else {
            holder.txt_order_instructions.setVisibility(View.GONE);
        }

        /*****************************************************************************************************/
        //Current status
        if(orderModelList.get(position).getOrderStatus() == 3){
            if(orderModelList.get(position).isPickup()){
                holder.txt_orderCurrent_status.setText(new StringBuilder()
                        .append("Order Picked up!"));
            }
            else
                holder.txt_orderCurrent_status.setText(new StringBuilder()
                        .append("Order Delivered!"));
        }
        else
            holder.txt_orderCurrent_status.setText(new StringBuilder()
                .append(Common.convertStatusToText(orderModelList.get(position).getOrderStatus())));
        /*****************************************************************************************************/
        //Order number animation
        if(orderModelList.get(position).getOrderStatus() != 3){
            //Not delivered
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.blink);
            animation.setDuration(2000);
            holder.txt_order_number.startAnimation(animation);
        }


        /*****************************************************************************************************/

        //Status update layout
        if(orderModelList.get(position).getOrderStatus() == 0){
            //Order just placed
            holder.txt_status_accept.setVisibility(View.VISIBLE);
            holder.txt_status_decline.setVisibility(View.VISIBLE);
        }
        else if(orderModelList.get(position).getOrderStatus() == 1){
            //Preparing
            holder.txt_status_accept.setVisibility(View.GONE);
            holder.txt_status_decline.setVisibility(View.GONE);
            if(!orderModelList.get(position).isPickup()) {
                holder.txt_status_out_for_delivery.setVisibility(View.VISIBLE);
                holder.txt_status_ready_for_pickup.setVisibility(View.GONE);
            }else {
                holder.txt_status_out_for_delivery.setVisibility(View.GONE);
                holder.txt_status_ready_for_pickup.setVisibility(View.VISIBLE);
            }
        }
        else if(orderModelList.get(position).getOrderStatus() == 2){
            //Out for delivery only track delivery boy
            holder.txt_status_accept.setVisibility(View.GONE);
            holder.txt_status_decline.setVisibility(View.GONE);
            holder.txt_status_out_for_delivery.setVisibility(View.GONE);
        }
        else if(orderModelList.get(position).getOrderStatus() == 3){
            //Order delivered
            holder.status_update_layout.setVisibility(View.GONE);
            holder.txt_order_instructions.setVisibility(View.GONE);

        }
        else if(orderModelList.get(position).getOrderStatus() == -2){
            //Ready for pickup
            holder.txt_status_accept.setVisibility(View.GONE);
            holder.txt_status_decline.setVisibility(View.GONE);
            holder.txt_status_deliver_now.setVisibility(View.VISIBLE);
        }
        else if(orderModelList.get(position).getOrderStatus() == -1){
            //Order cancelled by partner
            holder.status_update_layout.setVisibility(View.GONE);
        }
        else if(orderModelList.get(position).getOrderStatus() == -3){
            //Order cancelled by user
            holder.status_update_layout.setVisibility(View.GONE);

        }

        //status listener

        //accept clicked
        holder.txt_status_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                builder.setTitle("Accept Order");
                builder.setMessage("Do you want to accept this order");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hud.show();
                        Map<String, Object> updateStatus = new HashMap<>();
                        updateStatus.put("orderStatus",1);
                        FirebaseDatabase.getInstance(Common.ordersDB).getReference(Common.ORDER_REF)
                                .child(orderModelList.get(position).getOrderNumber())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            FirebaseDatabase.getInstance(Common.ordersDB).getReference(Common.ORDER_REF)
                                                    .child(orderModelList.get(position).getOrderNumber())
                                                    .updateChildren(updateStatus)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()) {
                                                                hud.dismiss();
                                                                Toast.makeText(context, "Order has been accepted", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else {
                                                                hud.dismiss();
                                                                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    hud.dismiss();
                                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                        else {
                                            hud.dismiss();
                                            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        hud.dismiss();
                                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                AlertDialog dialogs = builder.create();
                dialogs.show();
            }
        });

        //Decline status
        holder.txt_status_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                builder.setTitle("Cancel order!");
                builder.setMessage("Do you want to cancel this order?");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hud.show();
                        Map<String, Object> updateStatus = new HashMap<>();
                        updateStatus.put("orderStatus",-1);

                        //Update Refund status
                        FirebaseDatabase.getInstance(Common.ordersDB)
                                .getReference(Common.ORDER_REF)
                                .child(orderModelList.get(position).getOrderNumber())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            FirebaseDatabase.getInstance(Common.ordersDB).getReference(Common.ORDER_REF)
                                                    .child(orderModelList.get(position).getOrderNumber())
                                                    .updateChildren(updateStatus)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                hud.dismiss();
                                                                Toast.makeText(context, "Order has been cancelled", Toast.LENGTH_SHORT).show();
                                                            }else {
                                                                hud.dismiss();
                                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    hud.dismiss();
                                                    Toast.makeText(context, "Something went wrong" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                });
                AlertDialog dialogs = builder.create();
                dialogs.show();
            }
        });

        //out for delivery
        holder.txt_status_out_for_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                builder.setTitle("Change status");
                builder.setMessage("Do you want to change status to OUT FOR DELIVERY");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Show Delivery Agents first
                        hud.show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                        builder.setTitle("Select Delivery Agent");
                        View view = LayoutInflater.from(context).inflate(R.layout.layout_delivery_agent,null);
                        RecyclerView recycler_delivery_agent = view.findViewById(R.id.recycler_deliveryAgent);
                        deliveryAgentViewModel.getMessageError().observe((LifecycleOwner) context, new Observer<String>() {
                            @Override
                            public void onChanged(String error) {
                                hud.dismiss();
                                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                            }
                        });
                        deliveryAgentViewModel.getDeliveryAgentMutableLiveData().observe((LifecycleOwner) context, new Observer<List<DeliveryAgentModel>>() {
                            @Override
                            public void onChanged(List<DeliveryAgentModel> deliveryAgentModels) {
                                DeliveryAgentOutForDeliveryAdapter deliveryAgentOutForDeliveryAdapter = new DeliveryAgentOutForDeliveryAdapter(context, deliveryAgentModels,orderModelList.get(position));
                                recycler_delivery_agent.setAdapter(deliveryAgentOutForDeliveryAdapter);
                                recycler_delivery_agent.setLayoutManager(new LinearLayoutManager(context));
                                recycler_delivery_agent.setVerticalScrollBarEnabled(true);
                                recycler_delivery_agent.addItemDecoration(new SpacesItemDecoration(5));
                                hud.dismiss();

                            }
                        });
                        builder.setView(view);
                        dialogDeliveryAgent = builder.create();
                        dialogDeliveryAgent.show();


                    }
                });
                AlertDialog dialogs = builder.create();
                dialogs.show();
            }
        });

        //Ready for pickup
        holder.txt_status_ready_for_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                builder.setTitle("Change Status");
                builder.setMessage("Do you want to change status to READY FOR PICKUP");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hud.show();
                        Map<String, Object> updateStatus = new HashMap<>();
                        updateStatus.put("orderStatus",-2);
                        FirebaseDatabase.getInstance(Common.ordersDB).getReference(Common.ORDER_REF)
                                .child(orderModelList.get(position).getOrderNumber())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            FirebaseDatabase.getInstance(Common.ordersDB).getReference(Common.ORDER_REF)
                                                    .child(orderModelList.get(position).getOrderNumber())
                                                    .updateChildren(updateStatus)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                hud.dismiss();
                                                                Toast.makeText(context, "Order status changed to ready for pickup", Toast.LENGTH_SHORT).show();
                                                            }else {
                                                                hud.dismiss();
                                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    hud.dismiss();
                                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }else {
                                            hud.dismiss();
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        hud.dismiss();
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                AlertDialog dialogs = builder.create();
                dialogs.show();
            }
        });

        //Deliver now
        holder.txt_status_deliver_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog);
                builder.setTitle("Complete Delivery");
                View view = LayoutInflater.from(context).inflate(R.layout.layout_delivery_pickup,null);
                PinView optPin = view.findViewById(R.id.deliveryOtp);
                optPin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.toString().length() == 6){
                            hud.show();
                            //Check otp
                            if(s.toString().equals(orderModelList.get(position).getOTP())){
                                //Otp matches
                                dialogsOtp.dismiss();
                                Map<String, Object> updateStatus = new HashMap<>();
                                updateStatus.put("orderStatus",3);
                                FirebaseDatabase.getInstance(Common.ordersDB).getReference(Common.ORDER_REF)
                                        .child(orderModelList.get(position).getOrderNumber())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    FirebaseDatabase.getInstance(Common.ordersDB).getReference(Common.ORDER_REF)
                                                            .child(orderModelList.get(position).getOrderNumber())
                                                            .updateChildren(updateStatus)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        hud.dismiss();
                                                                        Toast.makeText(context, "Order delivered", Toast.LENGTH_SHORT).show();
                                                                    }else {
                                                                        hud.dismiss();
                                                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            hud.dismiss();
                                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });

                                                }else {
                                                    hud.dismiss();
                                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                hud.dismiss();
                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }
                            else {
                                //Otp failed
                                hud.dismiss();
                                optPin.setError("Wrong OTP Enter Again");
                                optPin.setText("");
                                return;
                            }


                        }

                    }
                });
                builder.setView(view);
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogsOtp = builder.create();
                dialogsOtp.show();
            }
        });

        //Call user
        holder.txt_call_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(context)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(new StringBuilder("tel:").append(orderModelList.get(position).getUserPhone()).toString()));
                                context.startActivity(intent);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(context, "You must enable this permission to call user", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        holder.setRecyclerClickListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                if(orderModelList.get(pos).getOrderStatus() != -1 && orderModelList.get(pos).getOrderStatus() != -3) {
                    Common.currentDetailOrder = orderModelList.get(pos);
                    Intent orderDetailIntent = new Intent(context, OrderDetailActivity.class);
                    context.startActivity(orderDetailIntent);
                    Bungee.slideLeft(context);
                }
                else {
                    Toast.makeText(context, "Order Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.txt_order_name)
        TextView txt_order_name;
        @BindView(R.id.txt_order_number)
        TextView txt_order_number;
        @BindView(R.id.txt_delivery_location)
        TextView txt_delivery_location;
        @BindView(R.id.txt_order_amount_restaurant_settlement)
        TextView txt_order_amount_restaurant_settlement;
        @BindView(R.id.txt_order_items)
        TextView txt_order_items;
        @BindView(R.id.txt_order_date)
        TextView txt_order_date;
        @BindView(R.id.status_update_layout)
        LinearLayout status_update_layout;
        @BindView(R.id.txt_status_accept)
        TextView txt_status_accept;
        @BindView(R.id.txt_status_decline)
        TextView txt_status_decline;
        @BindView(R.id.txt_status_out_for_delivery)
        TextView txt_status_out_for_delivery;
        @BindView(R.id.txt_order_type)
        TextView txt_order_type;
        @BindView(R.id.txt_status_ready_for_pickup)
        TextView txt_status_ready_for_pickup;
        @BindView(R.id.txt_order_instructions)
        TextView txt_order_instructions;
        @BindView(R.id.txt_status_deliver_now)
        TextView txt_status_deliver_now;
        @BindView(R.id.txt_orderCurrent_status)
        TextView txt_orderCurrent_status;
        @BindView(R.id.txt_call_user)
        TextView txt_call_user;
        IRecyclerClickListener recyclerClickListener;

        public void setRecyclerClickListener(IRecyclerClickListener recyclerClickListener) {
            this.recyclerClickListener = recyclerClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerClickListener.onItemClickListener(view,getAdapterPosition());
        }

    }




}
