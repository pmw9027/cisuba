package com.eastblue.cisuba.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eastblue.cisuba.Activity.NoticeActivity;
import com.eastblue.cisuba.Activity.RequestPartnerActivity;
import com.eastblue.cisuba.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PJC on 2017-02-07.
 */

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.btn_request_partner)
    void requestPartner() {
        startActivity(new Intent(getActivity(), RequestPartnerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @OnClick(R.id.lin_notice)
    void openNotice() {
        startActivity(new Intent(getActivity(), NoticeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("TYPE", "NOTICE"));
    }

    @OnClick(R.id.lin_faq)
    void openFaq() {
        startActivity(new Intent(getActivity(), NoticeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("TYPE", "NOTICE"));
    }
}
