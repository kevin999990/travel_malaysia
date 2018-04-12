package com.example.travelmalaysia;

import android.accounts.Account;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelmalaysia.model.RedeemCode;
import com.example.travelmalaysia.model.User;
import com.example.travelmalaysia.model.redeemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CouponRedeem extends AppCompatActivity {

    private String RedeemJson;
    private redeemAdapter adapter;
    private List<RedeemCode> redeemlist;
    private JSONArray jsonArray;
    private String code, itemname;
    private ListView redeemlistview;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_redeem);
        RequestQueue requestQueue = Volley.newRequestQueue(CouponRedeem.this);
        user = (User) getIntent().getSerializableExtra("user");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", new Integer(user.getUserid()).toString());
            Log.d(TAG, "getcode:" + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://kvintech.esy.sy/findcode.php", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                processResponse(response);

                Toast.makeText(CouponRedeem.this, "in onrespone", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CouponRedeem.this, "Something Error at ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Log.e(TAG, "onErrorResponse: " + error);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public byte[] getBody() {
                return super.getBody();
            }
        };

        requestQueue.add(jsonObjectRequest);

        redeemlistview = findViewById(R.id.codelist);
        adapter = new redeemAdapter(this.getApplicationContext(), redeemlist);
        redeemlistview.setAdapter(adapter);

    }

    private void processResponse(JSONObject response) {
        try {
            Log.d(TAG, "onResponse: " + response);
            jsonArray = response.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JO = jsonArray.getJSONObject(i);
                code = JO.getString("redeemCode");
                itemname = JO.getString("itemName");

                RedeemCode item = new RedeemCode(code, itemname);
                redeemlist.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
