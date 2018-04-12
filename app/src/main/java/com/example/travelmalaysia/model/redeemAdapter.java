package com.example.travelmalaysia.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelmalaysia.R;

import java.util.List;

/**
 * Created by User on 10/4/2018.
 */

public class redeemAdapter extends BaseAdapter {
    private Context mContext;
    private List<RedeemCode> redeemlist;

    public redeemAdapter(Context mContext, List<RedeemCode> redeemlist) {
        this.mContext = mContext;
        this.redeemlist = redeemlist;
    }

    @Override
    public int getCount() {
        return redeemlist.size();
    }

    @Override
    public Object getItem(int i) {
        return redeemlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.redeemcodelist, null);
        ImageView imgview = (ImageView) v.findViewById(R.id.itemimg);
        TextView nameview = (TextView) v.findViewById(R.id.redeemitemname);
        TextView codeview = (TextView) v.findViewById(R.id.redeemcode);
        imgview.setImageResource(R.mipmap.ic_launcher);
        nameview.setText(redeemlist.get(i).getItemname());
        codeview.setText(redeemlist.get(i).getCode());
        v.setTag(redeemlist.get(i).getCode());
        return v;
    }
}
