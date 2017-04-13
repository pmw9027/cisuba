package com.eastblue.cisuba.Adapter;

import android.content.Context;
import android.net.Network;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eastblue.cisuba.Manager.NetworkManager;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.R;

import java.util.ArrayList;

/**
 * Created by PJC on 2017-02-17.
 * This adapter is using in HomeFragment TOP RANK
 */

public class RankAdapter extends BaseAdapter {

    private ArrayList<ProductModel> mList;
    private Context mContext;

    public RankAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void setArray(ArrayList<ProductModel> array) {
        mList = array;
    }
    public void clearList() {
        mList.clear();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_top, parent, false);
            viewBinder = new ViewBinder(convertView);
            convertView.setTag(viewBinder);
        } else {
            viewBinder = (ViewBinder) convertView.getTag();
        }

        viewBinder.bindObject(mList.get(position));

        return convertView;
    }

    public class ViewBinder {
        ImageView imvImage;
        TextView tvName;

        public ViewBinder(View v) {
            imvImage = (ImageView) v.findViewById(R.id.imv_image);
            tvName = (TextView) v.findViewById(R.id.tv_name);
        }

        public void bindObject(ProductModel item) {
            //f(!item.isFreePartner) {
                tvName.setText(item.partnerName);
            //}
            Glide.with(mContext).load(NetworkManager.SERVER_URL + item.mainThumbnail).centerCrop().into(imvImage);
        }
    }
}
