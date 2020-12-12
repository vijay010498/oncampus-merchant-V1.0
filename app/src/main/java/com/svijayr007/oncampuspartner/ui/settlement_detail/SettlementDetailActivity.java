package com.svijayr007.oncampuspartner.ui.settlement_detail;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.adapter.MyOrderAdapter;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.common.SpacesItemDecoration;
import com.svijayr007.oncampuspartner.model.OrderModel;
import com.svijayr007.oncampuspartner.model.SettlementModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettlementDetailActivity extends AppCompatActivity {
    private SettlementModel currentSettlement = Common.currentSettlementOrder;
    private List<OrderModel> successfulOrders = new ArrayList<>();

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    @BindView(R.id.image_close)
    ImageView image_close;

    @BindView(R.id.title_settlement_number)
    TextView title_settlement_number;

    @BindView(R.id.text_settlement_id)
    TextView text_settlement_id;

    @BindView(R.id.text_settlement_status)
    TextView text_settlement_status;

    @BindView(R.id.text_total_Settlement)
    TextView text_total_Settlement;

    @BindView(R.id.recycler_settlement_orders)
    RecyclerView recycler_settlement_orders;

    @BindView(R.id.text_total_orders)
    TextView text_total_orders;

    @BindView(R.id.text_first_order_date)
    TextView text_first_order_date;

    @BindView(R.id.text_last_order_date)
    TextView text_last_order_date;

    @BindView(R.id.text_settlement_date)
    TextView text_settlement_date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_detail);
        ButterKnife.bind(this);


        //init
        recycler_settlement_orders.setHasFixedSize(true);
        recycler_settlement_orders.setLayoutManager(new LinearLayoutManager(this));
        recycler_settlement_orders.addItemDecoration(new SpacesItemDecoration(5));
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

        //Horizontal progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setTitle("Fetching Order's");

        new Thread(new Runnable() {
            int progress = 0;
            @Override
            public void run() {
                while(progress <= 100){
                    progressDialog.setProgress(progress);
                    if(progress == 100){
                        progressDialog.dismiss();
                    }
                    try {
                        Thread.sleep(35);

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    progress++;
                }


            }
        }).start();

        setUiData();
        setListener();

    }

    private void setListener() {
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setUiData() {
        //First fetch successful order's
        if(currentSettlement.getOrders() != null) {
            for (String key : currentSettlement.getOrders().keySet()) {
                OrderModel tempModel = currentSettlement.getOrders().get(key);
                tempModel.setKey(key);
                tempModel.setOrderNumber(key);
                successfulOrders.add(tempModel);
            }
        }

         //Sort based on create date
        Collections.sort(successfulOrders, new Comparator<OrderModel>() {
            @Override
            public int compare(OrderModel orderModel, OrderModel t1) {
                return Long.compare(orderModel.getCreateDate(),t1.getCreateDate());
            }
        });

         //First  order
        if(successfulOrders.size() > 0) {
            Date date1 = new Date(successfulOrders.get(0).getCreateDate());
            text_first_order_date.setText(new StringBuilder()
                    .append(simpleDateFormat.format(date1)));
        }

        //Last order
        if(successfulOrders.size() > 0) {
            Date date2 = new Date(successfulOrders.get(successfulOrders.size() - 1).getCreateDate());
            text_last_order_date.setText(new StringBuilder()
                    .append(simpleDateFormat.format(date2)));
        }


         //Settlement Number / key
        title_settlement_number.setText(new StringBuilder("#")
        .append(currentSettlement.getKey()));

        //Settlement Number / key
        text_settlement_id.setText(new StringBuilder("#")
                .append(currentSettlement.getKey()));

        text_settlement_status.setText(new StringBuilder()
        .append(currentSettlement.getSettlementStatus()));

        //Recycler orders
        MyOrderAdapter myOrderAdapter  = new MyOrderAdapter(this,successfulOrders);
        recycler_settlement_orders.setAdapter(myOrderAdapter);

        //Amount
        text_total_Settlement.setText(new StringBuilder()
        .append("₹")
        .append(currentSettlement.getAmount()));

        //settlement Date
        calendar.setTimeInMillis(currentSettlement.getSettlementDate());
        Date date = new Date(currentSettlement.getSettlementDate());
        text_settlement_date.setText(new StringBuilder(Common.getDateOfWeek(calendar.get(calendar.DAY_OF_WEEK)))
        .append(" ")
        .append(simpleDateFormat.format(date)));

        //Total orders
        text_total_orders.setText(new StringBuilder()
        .append(currentSettlement.getTotalOrders()));






    }
}