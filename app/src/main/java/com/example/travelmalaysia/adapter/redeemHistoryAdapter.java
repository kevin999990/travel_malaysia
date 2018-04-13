package com.example.travelmalaysia.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.travelmalaysia.R;
import com.example.travelmalaysia.model.RedeemCode;

import java.util.List;

public class redeemHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<RedeemCode> redeemList;

    public redeemHistoryAdapter(Context mContext, List<RedeemCode> redeemList) {
        this.mContext = mContext;
        this.redeemList = redeemList;
    }

    @Override
    public int getCount() {
        return redeemList.size();
    }

    @Override
    public Object getItem(int position) {
        return redeemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.redeem_list, null);
        TextView listName = v.findViewById(R.id.redeem_list_name);
        TextView listCode = (TextView) v.findViewById(R.id.redeem_list_code);
        TextView listQuantity = (TextView) v.findViewById(R.id.redeem_list_quantity);

        listName.setText(redeemList.get(position).getItemname());
        listCode.setText(redeemList.get(position).getCode());
        listQuantity.setText(String.valueOf(redeemList.get(position).getQuantity()));
        return v;
    }
}
