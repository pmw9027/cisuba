package com.eastblue.cisuba.Dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eastblue.cisuba.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PJC on 2017-02-28.
 */

public class MainPopUpDialog extends DialogFragment {

    @BindView(R.id.imv_image)
    ImageView imvPopUp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View v = inflater.inflate(R.layout.dialog_main_popup, container, false);
        ButterKnife.bind(this, v);
        init(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    void init(View v) {
        String bannerLink = getArguments().getString("image");
        Glide.with(getActivity()).load(bannerLink).fitCenter().into(imvPopUp);
    }

    @OnClick(R.id.btn_close)
    public void back() {
        dismiss();
    }
}
