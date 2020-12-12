package com.svijayr007.oncampuspartner.model;

public class DeliveryOrderModel {
    private String agentId;
    private OrderModel orderModel;
    private DeliveryAgentModel deliveryAgentModel;
    private PartnerUserModel partnerUserModel;
    private String key;
    private int deliveryStatus;


    public DeliveryOrderModel() {
    }


    public PartnerUserModel getPartnerUserModel() {
        return partnerUserModel;
    }

    public void setPartnerUserModel(PartnerUserModel partnerUserModel) {
        this.partnerUserModel = partnerUserModel;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public OrderModel getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(OrderModel orderModel) {
        this.orderModel = orderModel;
    }

    public DeliveryAgentModel getDeliveryAgentModel() {
        return deliveryAgentModel;
    }

    public void setDeliveryAgentModel(DeliveryAgentModel deliveryAgentModel) {
        this.deliveryAgentModel = deliveryAgentModel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
