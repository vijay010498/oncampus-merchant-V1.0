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
import com.svijayr007.oncampuspartner.eventBus.ResMenuScrollEvent;
import com.svijayr007.oncampuspartner.model.CategoryModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyFabMenuAdapter extends RecyclerView.Adapter<MyFabMenuAdapter.MyViewHolder> {
    Context context;
    List<CategoryModel> categoryModelList;

    public MyFabMenuAdapter(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_category_items_fab_menu,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(categoryModelList.get(position).getImage())
                .placeholder(R.drawable.ic_restaurant_menu_green_24dp)
                .into(holder.category_image);
        holder.category_name.setText(new StringBuilder(categoryModelList.get(position).getName()));
        holder.setListener((view, pos) -> {
            //EventBus.getDefault().postSticky(new CategoryClick(true,categoryModelList.get(pos)));
            EventBus.getDefault().post(new ResMenuScrollEvent(position));
        });

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class   MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;
        @BindView(R.id.txt_category)
        TextView category_name;
        @BindView(R.id.category_image)
        CircleImageView category_image;
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
        public void onClick(View v) {
            listener.onItemClickListener(v,getAdapterPosition());
        }
    }
}
