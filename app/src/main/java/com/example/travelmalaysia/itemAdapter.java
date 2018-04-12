package com.example.travelmalaysia;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelmalaysia.PopupWindow;
import com.example.travelmalaysia.R;
import com.example.travelmalaysia.model.Item;
import com.example.travelmalaysia.model.User;

import java.util.List;

/**
 * Created by User on 6/4/2018.
 */

public class itemAdapter extends BaseAdapter {

    private Context mContext;
    private List<Item> mitemList;
    private User user;
    private String JSONDATA;

    public itemAdapter(Context mContext, List<Item> mitemList, User user, String JSONDATA) {
        this.mContext = mContext;
        this.mitemList = mitemList;
        this.user = new User(user);
        this.JSONDATA = JSONDATA;
    }

    @Override
    public int getCount() {
        return mitemList.size();
    }

    @Override
    public Object getItem(int i) {
        return mitemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.item_list, null);
        ImageView imgview = (ImageView) v.findViewById(R.id.img);
        final TextView nameview = (TextView) v.findViewById(R.id.name);
        TextView descview = (TextView) v.findViewById(R.id.desc);
        TextView priceview = (TextView) v.findViewById(R.id.itemprice);
        imgview.setImageResource(R.mipmap.ic_launcher);
        nameview.setText(mitemList.get(i).getItemname());
        descview.setText(mitemList.get(i).getDescription());
        priceview.setText(String.valueOf(mitemList.get(i).getPrice()));
        v.setTag(mitemList.get(i).getId());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent popup = new Intent(mContext, PopupWindow.class);
                popup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                popup.putExtra("item", mitemList.get(i));
                popup.putExtra("user", user);
                popup.putExtra("JSON", JSONDATA);
                mContext.startActivity(popup);
            }
        });
        return v;
    }
}
