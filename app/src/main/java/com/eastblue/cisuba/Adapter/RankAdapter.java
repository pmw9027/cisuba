package com.eastblue.cisuba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.eastblue.cisuba.R;

import java.util.ArrayList;

/**
 * Created by PJC on 2017-02-17.
 * This adapter is using in HomeFragment TOP RANK
 */

public class RankAdapter extends BaseAdapter {

    private ArrayList<String> mList;
    private Context mContext;

    public RankAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void addItem(String aa) {
        mList.add("dfdf");
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_top, parent, false);
        }
        return convertView;
    }
}
