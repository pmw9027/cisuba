package com.eastblue.cisuba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eastblue.cisuba.Manager.NetworkManager;
import com.eastblue.cisuba.Model.ProductModel;
import com.eastblue.cisuba.Network.Product;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.String.LocationCode;

import java.util.ArrayList;

/**
 * Created by PJC on 2017-02-10.
 */

public class NearAdapter extends BaseAdapter {

    private ArrayList<ProductModel> mList;
    private Context mContext;

    private static final int VIEW_COUNT = 2;
    private static final int VIEW_TYPE_FREE = 0;
    private static final int VIEW_TYPE_CHARGE = 1;

    public NearAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void setArray(ArrayList<ProductModel> array) {
        mList = array;
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
        ChargeViewBinder chargeViewBinder = null;
        FreeViewBinder freeViewBinder = null;

        if(convertView == null) {

            if(getItemViewType(position) == VIEW_TYPE_CHARGE) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_near, parent, false);
                chargeViewBinder = new ChargeViewBinder(convertView);
                convertView.setTag(chargeViewBinder);
            }
            else if(getItemViewType(position) == VIEW_TYPE_FREE) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_near_free, parent, false);
                freeViewBinder = new FreeViewBinder(convertView);
                convertView.setTag(freeViewBinder);
            }

        } else {

            if(getItemViewType(position) == VIEW_TYPE_CHARGE) {
                chargeViewBinder = (ChargeViewBinder) convertView.getTag();
                chargeViewBinder.bindObject(mList.get(position));
            }
            else if(getItemViewType(position) == VIEW_TYPE_FREE) {
                freeViewBinder = (FreeViewBinder) convertView.getTag();
                freeViewBinder.bindObject(mList.get(position));
            }
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).isFreePartner) {
            return VIEW_TYPE_FREE;
        } else {
            return VIEW_TYPE_CHARGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_COUNT;
    }

    private class ChargeViewBinder {
        private ImageView imvImage;
        private TextView tvKm;
        private TextView tvDiscount;
        private TextView tvName;
        private TextView tvAddress;
        private TextView tvPriceMorning;
        private TextView tvPriceLunch;
        private TextView tvPriceDinner;

        public ChargeViewBinder(View v) {
            imvImage = (ImageView) v.findViewById(R.id.imv_image);
            tvKm = (TextView) v.findViewById(R.id.tv_km);
            tvDiscount = (TextView) v.findViewById(R.id.tv_discount);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvAddress = (TextView) v.findViewById(R.id.tv_address);
            tvPriceMorning = (TextView) v.findViewById(R.id.tv_price_morning);
            tvPriceLunch = (TextView) v.findViewById(R.id.tv_price_lunch);
            tvPriceDinner = (TextView) v.findViewById(R.id.tv_price_dinner);
        }

        public void bindObject(ProductModel item) {
            tvName.setText("["+LocationCode.getInstance().getLocation(item.gubunAdress) + "] " +item.partnerName);
            tvAddress.setText(item.shortAddress);
            tvDiscount.setText(item.discount + " 원 할인");
            tvPriceMorning.setText("조조 " + Integer.parseInt(item.morningPrice) + "원");
            tvPriceLunch.setText("평일 " + Integer.parseInt(item.lunchPrice) + "원");
            tvPriceDinner.setText("야간 " + Integer.parseInt(item.dinnerPrice) + "원");

            Glide.with(mContext).load(NetworkManager.SERVER_URL + item.mainThumbnail).centerCrop().into(imvImage);
        }
    }

    private class FreeViewBinder {
        private TextView tvName;
        private TextView tvAddress;
        private TextView tvPriceMorning;
        private TextView tvPriceLunch;
        private TextView tvPriceDinner;

        public FreeViewBinder(View v) {
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvAddress = (TextView) v.findViewById(R.id.tv_address);
            tvPriceMorning = (TextView) v.findViewById(R.id.tv_price_morning);
            tvPriceLunch = (TextView) v.findViewById(R.id.tv_price_lunch);
            tvPriceDinner = (TextView) v.findViewById(R.id.tv_price_dinner);
        }

        public void bindObject(ProductModel item) {
            tvName.setText("["+LocationCode.getInstance().getLocation(item.gubunAdress) + "] " +item.partnerName);
            tvAddress.setText(item.shortAddress);
            tvPriceMorning.setText("조조 " + Integer.parseInt(item.morningPrice) + "원");
            tvPriceLunch.setText("평일 " + Integer.parseInt(item.lunchPrice) + "원");
            tvPriceDinner.setText("야간 " + Integer.parseInt(item.dinnerPrice) + "원");
        }
    }
}
