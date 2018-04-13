///*
//package com.example.travelmalaysia;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.LinearLayout;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//public class RetriveItemData extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LinearLayout linearLayout = new LinearLayout(this);
//        setContentView(linearLayout);
////        setContentView(R.layout.activity_main);
//        new BackgroundTask().execute();
//
//    }
//
//
//    class BackgroundTask extends AsyncTask<Void, Void, String> {
//
//        String json_url;
//
//        @Override
//        protected void onPreExecute() {
//            json_url = "http://kvintech.esy.es/getitem.php";
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            StringBuilder stringBuilder = new StringBuilder();
//
//            try {
//                URL url = new URL(json_url);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String stringread;
//                while ((stringread = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(stringread + "\n");
//
//                }
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//                return stringBuilder.toString().trim();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return stringBuilder.toString().trim();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Intent activity = new Intent(RetriveItemData.this, DisplayShop.class);
//            activity.putExtra("JSON_STRING", result);
//            startActivity(activity);
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//        }
//
//    }
//}
//
//
//
//
//*/
