package com.svijayr007.oncampuspartner.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.suke.widget.SwitchButton;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.callback.IRecyclerClickListener;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.model.DeliveryAgentModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class DeliveryAgentAdapter extends RecyclerView.Adapter<DeliveryAgentAdapter.MyViewHolder> {
    Context context;
    List<DeliveryAgentModel> deliveryAgentModelList;
    private KProgressHUD hud ;


    public DeliveryAgentAdapter(Context context, List<DeliveryAgentModel> deliveryAgentModelList) {
        this.context = context;
        this.deliveryAgentModelList = deliveryAgentModelList;
        hud =  KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .setCancellable(false)
                .setLabel("Loading")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_delivery_agents,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(!TextUtils.isEmpty(deliveryAgentModelList.get(position).getImageUrl())){
            Glide.with(context)
                    .load(deliveryAgentModelList.get(position).getImageUrl())
                    .placeholder(R.drawable.ic_restaurant)
                    .into(holder.delivery_agent_image);
        }

        holder.delivery_agent_name.setText(deliveryAgentModelList.get(position).getName());
        holder.delivery_agent_phone.setText(deliveryAgentModelList.get(position).getPhone());
        holder.delivery_agent_active_switch.setChecked(deliveryAgentModelList.get(position).isActive());

        //call click
        holder.delivery_agent_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(context)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(new StringBuilder("tel:").append(deliveryAgentModelList.get(position).getPhone()).toString()));
                                context.startActivity(intent);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(context, "You must accept this permission to Call user", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        //delivery agent switch listener
        holder.delivery_agent_active_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                hud.show();
                deliveryAgentModelList.get(position).setActive(isChecked);
                Map<String, Object> updateDate = new HashMap<>();
                updateDate.put("active",isChecked);

                //Update to firebase
                FirebaseDatabase.getInstance(Common.deliveryAgentsDB)
                        .getReference(Common.DELIVERY_AGENT_REF)
                        .child(deliveryAgentModelList.get(position).getKey())
                        .updateChildren(updateDate)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Server Busy Try again", Toast.LENGTH_SHORT).show();
                                deliveryAgentModelList.get(position).setActive(!isChecked);
                                holder.delivery_agent_active_switch.setChecked(!isChecked);
                                hud.dismiss();

                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String tempStr = "";
                        if(isChecked)
                            tempStr = "is now Active";
                        else
                            tempStr = "is now Offline";
                        Toast.makeText(context, ""+deliveryAgentModelList.get(position).getName() + new StringBuilder(" ").append(tempStr) , Toast.LENGTH_SHORT).show();
                        hud.dismiss();

                    }
                });


            }
        });

        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                Toast.makeText(context, ""+deliveryAgentModelList.get(pos).getName(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return deliveryAgentModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.delivery_agent_name)
        TextView delivery_agent_name;
        @BindView(R.id.delivery_agent_phone)
        TextView delivery_agent_phone;
        @BindView(R.id.delivery_agent_image)
        CircleImageView delivery_agent_image;
        @BindView(R.id.delivery_agent_active_switch)
        SwitchButton delivery_agent_active_switch;

        Unbinder unbinder;

        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClickListener(view,getAdapterPosition());
        }
    }
}
