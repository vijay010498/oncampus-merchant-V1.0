package com.svijayr007.oncampuspartner.model;

import java.util.Map;

public class SettlementModel {
    private String key,settlementId,settlementStatus;
    Map<String , OrderModel> orders;
    private double amount;
    private long settlementDate;
    private int totalOrders;

    public SettlementModel() {
    }


    public String getKey() {
        return key;
    }

    public Map<String, OrderModel> getOrders() {
        return orders;
    }

    public void setOrders(Map<String, OrderModel> orders) {
        this.orders = orders;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(String settlementId) {
        this.settlementId = settlementId;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(long settlementDate) {
        this.settlementDate = settlementDate;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
}
