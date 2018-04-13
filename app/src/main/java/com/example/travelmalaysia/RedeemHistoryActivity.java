package com.example.travelmalaysia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelmalaysia.adapter.redeemHistoryAdapter;
import com.example.travelmalaysia.model.MyUser;
import com.example.travelmalaysia.model.RedeemCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RedeemHistoryActivity extends AppCompatActivity {
    private static final String TAG = "RedeemHistoryActivity";
    private ListView redeemListView;
    private MyUser mUser;
    private String url;
    private List<RedeemCode> redeemCodeList;
    private redeemHistoryAdapter redeemHistoryAdapter;
    private View mProgressView;
    private View mMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_history);

        url = getString(R.string.url_redeem_control);

        mUser = (MyUser) getIntent().getSerializableExtra("user");

        mMain = findViewById(R.id.redeem_main_layout);
        mProgressView = findViewById(R.id.redeem_progress_layout);

        getRedeemResult();

    }

    private void getRedeemResult() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        showProgress(true);
        //create JSONObject to pass to PHP Server for checking
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", mUser.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            if (response.getString("result").equalsIgnoreCase("success")) {
                                JSONArray redeemObject = response.getJSONArray("redeem");
                                redeemCodeList = new ArrayList<>();

                                for (int i = 0; i < redeemObject.length(); i++) {
                                    JSONObject jo = redeemObject.getJSONObject(i);
                                    String itemname = jo.getString("itemName");
                                    String code = jo.getString("redeemCode");
                                    int quantity = jo.getInt("redeemQuantity");
                                    RedeemCode redeemCode = new RedeemCode(itemname, code, quantity);
                                    redeemCodeList.add(redeemCode);
                                }
                                showProgress(false);
                                createAdapter();
                            } else {
                                Toast.makeText(RedeemHistoryActivity.this, "Cannot retrive result", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something Error at return value()", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Log.e(TAG, "onErrorResponse: " + error);
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }


    private void createAdapter() {
        if (redeemCodeList.size() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();

        } else {
            redeemListView = findViewById(R.id.redeem_his_list_view);
            redeemHistoryAdapter = new redeemHistoryAdapter(getApplicationContext(), redeemCodeList);
            redeemListView.setAdapter(redeemHistoryAdapter);
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mMain.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
