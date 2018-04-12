package com.example.travelmalaysia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelmalaysia.model.Item;
import com.example.travelmalaysia.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayShop extends AppCompatActivity {
    private ListView itemlistview;
    private itemAdapter adapter;
    private List<Item> listitem;
    private String JSON_STRING;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private String itemname, description;
    private int itemid;
    private double itemprice;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayshop);
        JSON_STRING = getIntent().getStringExtra("JSON_STRING");
        TextView walletview = findViewById(R.id.usermoney);
        user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            user = new User(1, "John", 50.00);
        }
        walletview.setText("$" + user.getWallet());
        listitem = new ArrayList<Item>();
        if (JSON_STRING == null && listitem.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No have any data", Toast.LENGTH_SHORT).show();
        } else if (listitem.isEmpty()) {
            try {
                jsonObject = new JSONObject(JSON_STRING);
                jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject JO = jsonArray.getJSONObject(i);
                    itemid = JO.getInt("itemId");
                    itemname = JO.getString("itemName");
                    description = JO.getString("itemDesc");
                    itemprice = JO.getDouble("itemPrice");

                    Item item = new Item(itemid, itemname, description, itemprice);
                    listitem.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        itemlistview = findViewById(R.id.list_test);
        adapter = new itemAdapter(this.getApplicationContext(), listitem, user, JSON_STRING);
        itemlistview.setAdapter(adapter);
    }
}
