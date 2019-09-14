package com.example.vtb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceReq {

    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("currencyCode")
    @Expose
    private Integer currencyCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("payer")
    @Expose
    private String payer;
    @SerializedName("recipient")
    @Expose
    private String recipient;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(Integer currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

}