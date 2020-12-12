package com.svijayr007.oncampuspartner.ui.orders;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.svijayr007.oncampuspartner.callback.IOrderCallbackListener;
import com.svijayr007.oncampuspartner.common.Common;
import com.svijayr007.oncampuspartner.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderViewModel extends ViewModel implements IOrderCallbackListener {

    private MutableLiveData<List<OrderModel>> orderModelMutableLiveData;
    private MutableLiveData<String> messageError;

    private IOrderCallbackListener listener;

    public OrderViewModel() {
       orderModelMutableLiveData = new MutableLiveData<>();
       messageError = new MutableLiveData<>();
       listener = this;
    }
    public MutableLiveData<String> getMessageError() {
        return messageError;
    }



    public MutableLiveData<List<OrderModel>> getOrderModelMutableLiveData(int status) {
        loadOrderStatus(status);
        return orderModelMutableLiveData;
    }

    public void loadOrderStatus(int status) {
        List<OrderModel> tempList = new ArrayList<>();
        Query orderRef = FirebaseDatabase.getInstance(Common.ordersDB)
                .getReference(Common.ORDER_REF)
                .orderByChild("restaurantId")
                .equalTo(Common.currentPartnerUser.getRestaurant());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (tempList.size() > 0)
                        tempList.clear();
                    for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {
                        OrderModel orderModel = itemSnapShot.getValue(OrderModel.class);
                        orderModel.setKey(itemSnapShot.getKey()); // order ID
                        orderModel.setOrderNumber(itemSnapShot.getKey());//important*
                        if(orderModel.getOrderStatus() == status) {
                                tempList.add(orderModel);
                                Log.i("SERVER", orderModel.getOrderNumber());
                        }
                    }
                    listener.onOrderLoadSuccess(tempList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onOrderLoadFailure(databaseError.getMessage());

            }
        });

    }



    @Override
    public void onOrderLoadSuccess(List<OrderModel> orderModelList) {
        orderModelMutableLiveData.setValue(orderModelList);

    }

    @Override
    public void onOrderLoadFailure(String message) {
            messageError.setValue(message);
    }
}