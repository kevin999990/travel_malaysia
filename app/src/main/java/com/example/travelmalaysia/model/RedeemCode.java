package com.example.travelmalaysia.model;


public class RedeemCode {
    private String itemname;
    private String code;
    private int quantity;

    public RedeemCode(String code, String itemname) {
        this.itemname = itemname;
        this.code = code;
    }

    public RedeemCode(String itemname, String code, int quantity) {

        this.itemname = itemname;
        this.code = code;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
