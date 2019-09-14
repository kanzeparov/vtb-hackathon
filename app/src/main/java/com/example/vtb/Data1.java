package com.example.vtb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data1 {

    @SerializedName("currencyCode")
    @Expose
    private Integer currencyCode;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(Integer currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
