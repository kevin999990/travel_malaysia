package com.example.travelmalaysia;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelmalaysia.adapter.itemAdapter;
import com.example.travelmalaysia.model.Item;
import com.example.travelmalaysia.model.MyUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private int itemprice;
    private MyUser mUser;
    private View mProgressView;
    private View mShopView;
    private String resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayshop);
        mUser = (MyUser) getIntent().getSerializableExtra("user");

        mShopView = findViewById(R.id.shop_view);
        mProgressView = findViewById(R.id.shop_progress_layout);

        new BackgroundTask().execute();

    }

    private void displayShop() {
        TextView walletview = findViewById(R.id.usermoney);


        //todo getuser :::user = (User) getIntent().getSerializableExtra("user");

        // walletview.setText("$" + user.getWallet());
        walletview.setText("$" + mUser.getPoints());
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
                    itemprice = JO.getInt("itemPrice");

                    Item item = new Item(itemid, itemname, description, itemprice);
                    listitem.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        itemlistview = findViewById(R.id.list_test);
        adapter = new itemAdapter(this.getApplicationContext(), listitem, mUser, JSON_STRING);
        itemlistview.setAdapter(adapter);
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mShopView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://kvintech.esy.es/getitem.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            showProgress(true);
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String stringread;
                while ((stringread = bufferedReader.readLine()) != null) {
                    stringBuilder.append(stringread + "\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString().trim();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO: 4/13/2018

            JSON_STRING = result;
            showProgress(false);
            displayShop();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
