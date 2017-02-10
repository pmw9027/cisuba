package com.eastblue.cisuba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastblue.cisuba.R;

import java.util.ArrayList;

/**
 * Created by PJC on 2017-02-10.
 */

public class NearAdapter extends BaseAdapter {

    private ArrayList<String> mList;
    private Context mContext;

    public NearAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
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
        ViewBinder viewBinder = null;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_near, parent, false);
            viewBinder = new ViewBinder(convertView);
            convertView.setTag(viewBinder);
        } else {
            viewBinder = (ViewBinder) convertView.getTag();
        }

        return convertView;
    }

    private class ViewBinder {
        private TextView tvKm;
        private TextView tvDiscount;
        private TextView tvName;
        private TextView tvAddress;

        public ViewBinder(View v) {
            tvKm = (TextView) v.findViewById(R.id.tv_km);
            tvDiscount = (TextView) v.findViewById(R.id.tv_discount);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvAddress = (TextView) v.findViewById(R.id.tv_address);
        }
    }
}
