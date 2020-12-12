package com.svijayr007.oncampuspartner.ui.order_detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.svijayr007.oncampuspartner.R;
import com.svijayr007.oncampuspartner.adapter.MyCartBillAdapter;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.common.SpacesItemDecoration;
import com.svijayr007.oncampuspartner.model.OrderModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends AppCompatActivity {
    private KProgressHUD hud;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private OrderModel currentDetailOrder = Common.currentDetailOrder;

    //All UI Bindings
    @BindView(R.id.title_order_number)
    TextView title_order_number;

    @BindView(R.id.text_user_name)
    TextView text_user_name;

    @BindView(R.id.text_order_time)
    TextView text_order_time;

    @BindView(R.id.text_order_type)
    TextView text_order_type;


    @BindView(R.id.text_order_id)
    TextView text_order_id;

    @BindView(R.id.text_order_status)
    TextView text_order_status;

    @BindView(R.id.recycler_bill_cart_detail)
    RecyclerView recycler_bill_cart_detail;

    private MyCartBillAdapter myCartBillAdapter;

    @BindView(R.id.text_food_total)
    TextView text_food_total;

    @BindView(R.id.text_percentage)
    TextView text_percentage;

    @BindView(R.id.text_percentage_amount)
    TextView text_percentage_amount;

    @BindView(R.id.text_packing_charges)
    TextView text_packing_charges;


    @BindView(R.id.text_total_Settlement)
    TextView text_total_Settlement;

    @BindView(R.id.text_delivery_price)
    TextView text_delivery_price;

    @BindView(R.id.text_cooking_info)
    TextView text_cooking_info;

    @BindView(R.id.text_delivery_location)
    TextView text_delivery_location;

    @BindView(R.id.text_item_after_percentage)
    TextView text_item_after_percentage;

    @BindView(R.id.image_close)
    ImageView image_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        hud = KProgressHUD.create(OrderDetailActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                .setCancellable(false)
                .setLabel("Loading")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f).show();
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        recycler_bill_cart_detail.setHasFixedSize(true);
        recycler_bill_cart_detail.setLayoutManager(new LinearLayoutManager(this));
        recycler_bill_cart_detail.addItemDecoration(new SpacesItemDecoration(5));

        initUI();
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

    private void initUI() {
        title_order_number.setText(new StringBuilder()
        .append(currentDetailOrder.getOrderNumber()));

        text_user_name.setText(new StringBuilder()
        .append(currentDetailOrder.getUserName()));

        //Order type
        if(currentDetailOrder.isPickup()){
            //Pickup order
            text_order_type.setText(new StringBuilder()
            .append("PICKUP"));
           text_delivery_location.setVisibility(View.GONE);
        }
        else {
            text_order_type.setText(new StringBuilder()
            .append("DELIVERY"));
            text_delivery_location.setVisibility(View.VISIBLE);
            text_delivery_location.setText(currentDetailOrder.getShippingAddress());
        }


        //Order date
        calendar.setTimeInMillis(currentDetailOrder.getCreateDate());
        Date date = new Date(currentDetailOrder.getCreateDate());

        text_order_time.setText(new StringBuilder()
                .append(Common.getDateOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
                .append(" ").append(simpleDateFormat.format(date)));


        text_order_id.setText(new StringBuilder()
        .append("#")
        .append(currentDetailOrder.getOrderNumber()));

        //Order status
        if(currentDetailOrder.getOrderStatus() == 3){
            if(currentDetailOrder.isPickup()){
                text_order_status.setText(new StringBuilder()
                        .append("\"")
                        .append("Picked Up!")
                        .append("\""));
            }
            else {
                text_order_status.setText(new StringBuilder()
                        .append("\"")
                        .append("Delivered")
                        .append("\""));
            }

        }
        else
            text_order_status.setText(new StringBuilder()
            .append("\"")
            .append(Common.convertStatusToText(currentDetailOrder.getOrderStatus()))
            .append("\""));

        myCartBillAdapter = new MyCartBillAdapter(OrderDetailActivity.this,currentDetailOrder.getCartItemList());
        recycler_bill_cart_detail.setAdapter(myCartBillAdapter);

        //order items and price
        /*int numberOfItems = currentDetailOrder.getCartItemList().size();
        order_item_details.setText("");
        order_item_price_details.setText("");
        for(int i=0;i<numberOfItems;i++){

            //Append item and price
            order_item_details.append(new StringBuilder()
                    .append(currentDetailOrder.getCartItemList().get(i).getFoodName())
                    .append(" X ")
                    .append(currentDetailOrder.getCartItemList().get(i).getFoodQuantity()));

            order_item_price_details.append(new StringBuilder("₹")
                    .append(currentDetailOrder.getCartItemList().get(i).getFoodPrice() * currentDetailOrder.getCartItemList().get(i).getFoodQuantity()));
            if(i+1 < numberOfItems) {
                order_item_details.append("\n");
                order_item_price_details.append("\n");
            }
        }*/

        //Only food price
        text_food_total.setText(new StringBuilder()
        .append("₹")
        .append(currentDetailOrder.getOnlyFoodPrice()));

        //Percentage
        text_percentage.setText(new StringBuilder()
        .append("- %")
        .append(currentDetailOrder.getOnCampusCommissionPercentage()));

        //Percentage Amount
        text_percentage_amount.setText(new StringBuilder()
        .append("- ₹")
        .append(currentDetailOrder.getOnCampusCommissionAmount()));

        //After Percentage Amount
        text_item_after_percentage.setText(new StringBuilder()
        .append("₹")
        .append((currentDetailOrder.getOnlyFoodPrice() - currentDetailOrder.getOnCampusCommissionAmount())));

        //Packing Charges
        text_packing_charges.setText(new StringBuilder()
        .append("₹")
        .append(currentDetailOrder.getPackingCharges()));

        //Delivery charges
        text_delivery_price.setText(new StringBuilder()
        .append("₹")
        .append(currentDetailOrder.getDeliveryCharges()));

        text_total_Settlement.setText(new StringBuilder()
        .append("₹")
        .append(currentDetailOrder.getPayToRestaurantAfterCommissionAmount()));

        //Cooking info
        if(!TextUtils.isEmpty(currentDetailOrder.getComment())){
            text_cooking_info.setText(new StringBuilder()
                    .append(currentDetailOrder.getComment()));
        }
        else {
            text_cooking_info.setText(new StringBuilder()
                    .append("No cooking instructions"));
        }
        hud.dismiss();


    }
}