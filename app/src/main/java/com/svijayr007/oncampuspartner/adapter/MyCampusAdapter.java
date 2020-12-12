package com.svijayr007.oncampuspartner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.callback.IRecyclerClickListener;
import com.svijayr007.oncampuspartner.eventBus.CampusSelectedEvent;
import com.svijayr007.oncampuspartner.model.CampusModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyCampusAdapter extends RecyclerView.Adapter<MyCampusAdapter.MyViewHolder> {
    Context context;
    List<CampusModel> campusModelList;

    public MyCampusAdapter(Context context, List<CampusModel> campusModelList) {
        this.context = context;
        this.campusModelList = campusModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_campus_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(campusModelList.get(position).getImageUrl())
                .placeholder(R.drawable.ic_restaurant)
                .into(holder.campus_image);
        holder.campus_name.setText(new StringBuilder()
                .append(campusModelList.get(position).getName()));
        holder.campus_address.setText(new StringBuilder()
                .append(campusModelList.get(position).getAddress()));
        holder.campus_no_of_restaurants.setText(new StringBuilder()
                .append(campusModelList.get(position).getNumber_of_restaurants())
                .append(" Restaurants"));

        holder.setListener((view, pos) -> {
            EventBus.getDefault().post(new CampusSelectedEvent(campusModelList.get(pos)));
        });


    }

    @Override
    public int getItemCount() {
        return campusModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;
        @BindView(R.id.campus_image)
        CircleImageView campus_image;
        @BindView(R.id.campus_name)
        TextView campus_name;
        @BindView(R.id.campus_address)
        TextView campus_address;
        @BindView(R.id.campus_no_of_restaurants)
        TextView campus_no_of_restaurants;


        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onItemClickListener(view, getAdapterPosition());
        }
    }
}