package com.example.travelmalaysia.model;

/**
 * Created by User on 8/4/2018.
 */

public class RedeemCode {
    private String itemname;
    private String code;

    public RedeemCode(String code, String itemname) {
        this.itemname = itemname;
        this.code = code;
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
