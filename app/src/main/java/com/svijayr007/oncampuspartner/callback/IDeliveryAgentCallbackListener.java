package com.svijayr007.oncampuspartner.callback;


import com.svijayr007.oncampuspartner.model.DeliveryAgentModel;

import java.util.List;

public interface IDeliveryAgentCallbackListener {
     void onDeliveryAgentLoadSuccess(List<DeliveryAgentModel> deliveryAgentModelList);
     void onDeliveryAgentLoadFailed(String message);


}
