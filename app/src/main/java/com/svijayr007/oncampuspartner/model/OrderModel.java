package com.svijayr007.oncampuspartner.model;

import java.util.List;

public class OrderModel {

    //order key
    private String key;

    private String userId, userName, userEmail,userPhone, shippingAddress, comment, transactionId;
    private String paymentMode,transactionTime,paymentMessage,paymentSignature,transactionStatus;
    private String OTP;
    private String restaurantId, restaurantName,restaurantAddress,restaurantPhone;

    private double lat,lng,totalPayment,onCampusCommissionAmount,onCampusCommissionPercentage,payToRestaurantAfterCommissionAmount,deliveryCharges,packingCharges,onlyFoodPrice;

    private List<CartItem> cartItemList;
    private long createDate;
    private String orderNumber;
    private  String userToken;
    private int orderStatus;
    private boolean isPickup;


    public OrderModel() {
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public double getPackingCharges() {
        return packingCharges;
    }

    public void setPackingCharges(double packingCharges) {
        this.packingCharges = packingCharges;
    }

    public double getOnlyFoodPrice() {
        return onlyFoodPrice;
    }

    public void setOnlyFoodPrice(double onlyFoodPrice) {
        this.onlyFoodPrice = onlyFoodPrice;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getPaymentMessage() {
        return paymentMessage;
    }

    public void setPaymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
    }

    public String getPaymentSignature() {
        return paymentSignature;
    }

    public void setPaymentSignature(String paymentSignature) {
        this.paymentSignature = paymentSignature;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getOnCampusCommissionAmount() {
        return onCampusCommissionAmount;
    }

    public void setOnCampusCommissionAmount(double onCampusCommissionAmount) {
        this.onCampusCommissionAmount = onCampusCommissionAmount;
    }

    public double getOnCampusCommissionPercentage() {
        return onCampusCommissionPercentage;
    }

    public void setOnCampusCommissionPercentage(double onCampusCommissionPercentage) {
        this.onCampusCommissionPercentage = onCampusCommissionPercentage;
    }

    public double getPayToRestaurantAfterCommissionAmount() {
        return payToRestaurantAfterCommissionAmount;
    }

    public void setPayToRestaurantAfterCommissionAmount(double payToRestaurantAfterCommissionAmount) {
        this.payToRestaurantAfterCommissionAmount = payToRestaurantAfterCommissionAmount;
    }



    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
