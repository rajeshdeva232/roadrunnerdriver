package com.giridemo.roadrunnerdriver.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class OrderHistory {

    private String addresstoDelevery,delivermobileNo,delivername,landmark,orderedTime,totalamount,doorNo,key,username,usermobileno
            ,time;
    private LatLng deleveryLatlng;
    private ArrayList<GetItemlist> getItemlistArrayList=new ArrayList<>();
    private int deleveryStatus;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDeleveryStatus() {
        return deleveryStatus;
    }

    public void setDeleveryStatus(int deleveryStatus) {
        this.deleveryStatus = deleveryStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermobileno() {
        return usermobileno;
    }

    public void setUsermobileno(String usermobileno) {
        this.usermobileno = usermobileno;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public ArrayList<GetItemlist> getGetItemlistArrayList() {
        return getItemlistArrayList;
    }

    public void setGetItemlistArrayList(ArrayList<GetItemlist> getItemlistArrayList) {
        this.getItemlistArrayList = getItemlistArrayList;
    }

    public LatLng getDeleveryLatlng() {
        return deleveryLatlng;
    }

    public void setDeleveryLatlng(LatLng deleveryLatlng) {
        this.deleveryLatlng = deleveryLatlng;
    }

    public String getAddresstoDelevery() {
        return addresstoDelevery;
    }

    public void setAddresstoDelevery(String addresstoDelevery) {
        this.addresstoDelevery = addresstoDelevery;
    }

    public String getDelivermobileNo() {
        return delivermobileNo;
    }

    public void setDelivermobileNo(String delivermobileNo) {
        this.delivermobileNo = delivermobileNo;
    }

    public String getDelivername() {
        return delivername;
    }

    public void setDelivername(String delivername) {
        this.delivername = delivername;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

}
