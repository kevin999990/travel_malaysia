package com.example.travelmalaysia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.travelmalaysia.R;
import com.example.travelmalaysia.model.MyPlace;

import java.util.List;

public class locationHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<MyPlace> myPlaceList;

    public locationHistoryAdapter(Context mContext, List<MyPlace> myPlaceList) {
        this.mContext = mContext;
        this.myPlaceList = myPlaceList;
    }


    @Override
    public int getCount() {
        return myPlaceList.size();
    }

    @Override
    public Object getItem(int position) {
        return myPlaceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myPlaceList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.activity_location_history, null);


        return view;
    }
}
