package com.example.travelmalaysia.model;

import java.io.Serializable;

/**
 * Created by User on 6/4/2018.
 */

public class Item implements Serializable {
    private int id;
    private String itemname;
    private String description;
    private int price;

    public Item(int id, String itemname, String description, int price) {
        this.id = id;
        this.itemname = itemname;
        this.description = description;
        this.price = price;
    }

    public Item(Item item) {
        this.id = item.id;
        this.itemname = item.itemname;
        this.description = item.description;
        this.price = item.price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
