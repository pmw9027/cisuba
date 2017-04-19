package com.eastblue.cisuba.Adapter;

import android.content.Context;
import android.location.Location;
import android.util.Log;
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
import com.eastblue.cisuba.String.LocationCode;

import java.util.ArrayList;

/**
 * Created by PJC on 2017-02-10.
 */

public class NearSelectAdapter extends BaseAdapter {

    private ArrayList<ProductModel> mList;
    private Context mContext;
    private Location mLocation;

    private static final int VIEW_COUNT = 2;
    private static final int VIEW_TYPE_FREE = 0;
    private static final int VIEW_TYPE_CHARGE = 1;

    public NearSelectAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        mLocation = null;
    }

    public void setArray(ArrayList<ProductModel> array) {
        mList = array;
    }

    public void setLocation(double lat, double lng) {
        mLocation = new Location("MY GEO");
        mLocation.setLatitude(lat);
        mLocation.setLongitude(lng);
    }
    public void setItem(int index, ProductModel item) {
        mList.set(index,item);
        notifyDataSetChanged();
    }
    public void addItem(ProductModel item) {
        mList.add(item);
    }
    public void add(int index, ProductModel item)
    {
        mList.add(index, item);
        notifyDataSetChanged();
    }


    public void removeAll() {
        mList.clear();;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    public Object getItem(String title) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).partnerName.contains(title)) {
                return mList.get(i);
            }
        }
        return null;
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
            if (getItemViewType(position) == VIEW_TYPE_CHARGE) {
                chargeViewBinder = (ChargeViewBinder) convertView.getTag();
            } else if (getItemViewType(position) == VIEW_TYPE_FREE) {
                freeViewBinder = (FreeViewBinder) convertView.getTag();
            }
        }

        if (getItemViewType(position) == VIEW_TYPE_CHARGE) {
            chargeViewBinder.bindObject(mList.get(position));
        } else if (getItemViewType(position) == VIEW_TYPE_FREE) {
            freeViewBinder.bindObject(mList.get(position));
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

            // Calc the KM
            if(mLocation != null) {
                Log.d("cat", "cat");
                if(item.lat != null && item.lng != null) {
                    Location target = new Location("TARGET");
                    target.setLatitude(Double.parseDouble(item.lat));
                    target.setLongitude(Double.parseDouble(item.lng));

                    int distance = (int) mLocation.distanceTo(target);

                    int km = 0;
                    if(distance >= 1000) {
                         km = distance / 1000;
                    }
                    int meter = distance % 1000 / 3;
                    tvKm.setText(km + "." + meter + " Km");
                }
            } else {
                tvKm.setVisibility(View.INVISIBLE);
            }

            Glide.with(mContext).load(NetworkManager.SERVER_URL + item.mainThumbnail).centerCrop().into(imvImage);
        }
    }

    private class FreeViewBinder {
        private TextView tvKm;
        private TextView tvName;
        private TextView tvAddress;
        private TextView tvPriceMorning;
        private TextView tvPriceLunch;
        private TextView tvPriceDinner;

        public FreeViewBinder(View v) {
            tvKm = (TextView) v.findViewById(R.id.tv_km);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvAddress = (TextView) v.findViewById(R.id.tv_address);
            tvPriceMorning = (TextView) v.findViewById(R.id.tv_price_morning);
            tvPriceLunch = (TextView) v.findViewById(R.id.tv_price_lunch);
            tvPriceDinner = (TextView) v.findViewById(R.id.tv_price_dinner);
        }

        public void bindObject(ProductModel item) {
            tvName.setText("["+item.highlightAddress + "] " +item.partnerName);
            tvAddress.setText(item.detailAddress);
            tvPriceMorning.setText("조조 " + Integer.parseInt(item.morningPrice) + "원");
            tvPriceLunch.setText("평일 " + Integer.parseInt(item.lunchPrice) + "원");
            tvPriceDinner.setText("야간 " + Integer.parseInt(item.dinnerPrice) + "원");

            if(mLocation != null) {

                if(item.lat != null && item.lng != null) {

                    Location target = new Location("TARGET");
                    target.setLatitude(Double.parseDouble(item.lat));
                    target.setLongitude(Double.parseDouble(item.lng));

                    int distance = (int) mLocation.distanceTo(target);

                    int km = 0;
                    if(distance >= 1000) {
                        km = distance / 1000;
                    }
                    int meter = distance % 1000;
                    if(meter > 0) {
                        meter = meter / 100;
                    }
                    String distance_str = String.format("%d.%01d Km", km, meter);
                    tvKm.setText(distance_str);
                }
            } else {
                Log.d("null", "null");
                tvKm.setVisibility(View.GONE);
            }
        }
    }

    public float distance (float lat_a, float lng_a, float lat_b, float lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }
}
