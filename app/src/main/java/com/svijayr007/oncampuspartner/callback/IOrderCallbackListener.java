package com.svijayr007.oncampuspartner.callback;

import com.svijayr007.oncampuspartner.model.OrderModel;

import java.util.List;

public interface IOrderCallbackListener {
    void onOrderLoadSuccess(List<OrderModel> orderModelList);
    void onOrderLoadFailure(String message);
}
