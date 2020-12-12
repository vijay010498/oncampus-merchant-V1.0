package com.svijayr007.oncampuspartner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.callback.IRecyclerClickListener;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.model.SettlementModel;
import com.svijayr007.oncampuspartner.ui.settlement_detail.SettlementDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettlementAdapter extends RecyclerView.Adapter<SettlementAdapter.MyViewHolder> {
    Context context;
    List<SettlementModel> settlementModelList;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    public SettlementAdapter(Context context, List<SettlementModel> settlementModelList) {
        this.context = context;
        this.settlementModelList = settlementModelList;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                                .inflate(R.layout.layout_settlement_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Settlement Date
        calendar.setTimeInMillis(settlementModelList.get(position).getSettlementDate());
        Date date = new Date(settlementModelList.get(position).getSettlementDate());
        holder.txt_settlement_date.setText(new StringBuilder(Common.getDateOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
                .append(" ").append(simpleDateFormat.format(date)));

        //Settlement Id
        holder.txt_settlement_id.setText(new StringBuilder()
        .append("#")
        .append(settlementModelList.get(position).getSettlementId()));

        //Settlement Amount
        holder.txt_settlement_amount.setText(new StringBuilder()
        .append("₹")
        .append(settlementModelList.get(position).getAmount()));

        //Settlement Status
        holder.txt_settlement_status.setText(new StringBuilder()
        .append(settlementModelList.get(position).getSettlementStatus()));

        //Total Order's
        holder.txt_total_orders.setText(new StringBuilder()
        .append(settlementModelList.get(position).getTotalOrders()));

        //Click listener
        holder.setRecyclerClickListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                //Navigate to settlement Detail
                Common.currentSettlementOrder = settlementModelList.get(pos);
                context.startActivity(new Intent(context, SettlementDetailActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return settlementModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;
        @BindView(R.id.txt_settlement_date)
        TextView txt_settlement_date;
        @BindView(R.id.txt_settlement_id)
        TextView txt_settlement_id;
        @BindView(R.id.txt_settlement_amount)
        TextView txt_settlement_amount;
        @BindView(R.id.txt_settlement_status)
        TextView txt_settlement_status;
        @BindView(R.id.txt_total_orders)
        TextView txt_total_orders;


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
