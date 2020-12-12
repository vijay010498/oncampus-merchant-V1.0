package com.svijayr007.oncampuspartner.eventBus;

import com.svijayr007.oncampuspartner.model.DeliveryAgentModel;
import com.svijayr007.oncampuspartner.model.OrderModel;

public class DeliveryAgentOutForDeliveryEvent {
    private DeliveryAgentModel selectedDeliveryAgent;
    private OrderModel deliveryOrder;


    public DeliveryAgentOutForDeliveryEvent(DeliveryAgentModel selectedDeliveryAgent, OrderModel deliveryOrder) {
        this.selectedDeliveryAgent = selectedDeliveryAgent;
        this.deliveryOrder = deliveryOrder;
    }

    public DeliveryAgentModel getSelectedDeliveryAgent() {
        return selectedDeliveryAgent;
    }

    public void setSelectedDeliveryAgent(DeliveryAgentModel selectedDeliveryAgent) {
        this.selectedDeliveryAgent = selectedDeliveryAgent;
    }

    public OrderModel getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(OrderModel deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }
}
