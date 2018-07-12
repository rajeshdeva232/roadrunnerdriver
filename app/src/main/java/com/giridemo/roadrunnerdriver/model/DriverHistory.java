package com.giridemo.roadrunnerdriver.model;

public class DriverHistory {

    String name, address, mobile, totalamount;

    public DriverHistory(String name, String address, String mobile, String totalamount) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.totalamount = totalamount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }
}
