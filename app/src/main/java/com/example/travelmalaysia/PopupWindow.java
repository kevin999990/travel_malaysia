package com.example.travelmalaysia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelmalaysia.model.Item;
import com.example.travelmalaysia.model.MyUser;
import com.example.travelmalaysia.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class PopupWindow extends Activity {


    TextView nameview, descriptionview, priceview, quantityview;
    ImageView imgview;
    Button quitbt, addbt, minusbt, redeembt;
    MyUser user;
    Item item;
    RequestQueue requestQueue;
    private int price;
    private int quantities = 1;
    private String JSON_STRING;
    private JSONObject redeemjson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        user = (MyUser) getIntent().getSerializableExtra("user");
        item = (Item) getIntent().getSerializableExtra("item");
        JSON_STRING = getIntent().getStringExtra("JSON");

        imgview = findViewById(R.id.popupimage);
        imgview.setImageResource(R.mipmap.ic_launcher);
        nameview = findViewById(R.id.popupitemname);
        nameview.setText(item.getItemname());
        descriptionview = findViewById(R.id.popupdescription);
        descriptionview.setText(item.getDescription());
        priceview = findViewById(R.id.showprice);
        price = item.getPrice() * quantities;
        priceview.setText(String.valueOf(item.getPrice()));
        quantityview = findViewById(R.id.quantity);
        quantityview.setText(new Integer(quantities).toString());
        quitbt = findViewById(R.id.quit);
        addbt = findViewById(R.id.addquantity);
        minusbt = findViewById(R.id.minusquantity);
        redeembt = findViewById(R.id.redeem);

        quitbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantities < 9 && quantities > 0) {
                    quantities++;
                    price = quantities * item.getPrice();
                    priceview.setText(String.valueOf(price));
                    quantityview.setText(String.valueOf(quantities));
                } else {
                    quantities = 1;
                    price = quantities * item.getPrice();
                    priceview.setText(String.valueOf(price));
                    quantityview.setText(String.valueOf(quantities));
                }
            }
        });
        minusbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantities > 1 && quantities < 10) {
                    quantities--;
                    price = quantities * item.getPrice();
                    priceview.setText(String.valueOf(price));
                    quantityview.setText(String.valueOf(quantities));
                } else {
                    quantities = 9;
                    price = quantities * item.getPrice();
                    priceview.setText(String.valueOf(price));
                    quantityview.setText(String.valueOf(quantities));
                }

            }
        });

        redeembt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (price <= user.getPoints()) {
                    user.setPoints((user.getPoints() - price));


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userid", new Integer(user.getId()).toString());
                        jsonObject.put("itemid", new Integer(item.getId()).toString());
                        jsonObject.put("quantity", new Integer(quantities).toString());
                        Log.d(TAG, "insertcode:" + jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://kvintech.esy.es/insertcode.php", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(PopupWindow.this, "onResponse", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: " + response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PopupWindow.this, "Something Error at insert", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onErrorResponse: " + error.getMessage());
                            Log.e(TAG, "onErrorResponse: " + error);
                            error.printStackTrace();
                        }
                    });
                    requestQueue = Volley.newRequestQueue(PopupWindow.this);
                    requestQueue.add(jsonObjectRequest);

                    Toast.makeText(getApplicationContext(), "Purchase success", Toast.LENGTH_SHORT).show();
                    Intent redeempage = new Intent(PopupWindow.this, CouponRedeem.class);
                    redeempage.putExtra("user", user);
                    startActivity(redeempage);

                } else {
                    Toast.makeText(getApplicationContext(), "No enough point", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }


}