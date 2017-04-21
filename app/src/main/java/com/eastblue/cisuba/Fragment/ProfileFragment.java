package com.eastblue.cisuba.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eastblue.cisuba.Activity.LoginActivity;
import com.eastblue.cisuba.Activity.MainActivity;
import com.eastblue.cisuba.Activity.NoticeActivity;
import com.eastblue.cisuba.Activity.RequestPartnerActivity;
import com.eastblue.cisuba.R;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.Session;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.helper.log.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by PJC on 2017-02-07.
 */

public class ProfileFragment extends Fragment {

    public static TextView nickname;
    public static CircleImageView profileimage;
    public static ImageButton logout;
    Bitmap bitmap;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);

        nickname = (TextView)rootView.findViewById(R.id.tv_name);
        profileimage = (CircleImageView)rootView.findViewById(R.id.imv_profile);
        logout = (ImageButton)rootView.findViewById(R.id.btn_logout);
        if(Session.getCurrentSession().isClosed()) {
            if (LoginActivity.mOAuthLoginModule != null) {
                if (LoginActivity.mOAuthLoginModule.getState(getActivity()).toString().equals("OK")) {
                } else {
                    logout.setEnabled(false);
                }
            } else {
                logout.setEnabled(false);
            }
        } else {
            if (LoginActivity.mOAuthLoginModule != null) {
                if (LoginActivity.mOAuthLoginModule.getState(getActivity()).toString().equals("OK")) {
                } else {
                    logout.setEnabled(false);
                }
            } else {
                logout.setEnabled(false);
            }
        }
        requestMe();

        return rootView;
    }

    @OnClick(R.id.imv_profile)
    void openLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @OnClick(R.id.btn_logout)
    void logout() {

        // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니다?")
                //.setIcon()
                .setPositiveButton("예",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserManagement.requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                requestMe();
                                MainActivity.profileimage.setEnabled(true);
                                profileimage.setEnabled(true);
                            }
                        });

                        if(LoginActivity.mOAuthLoginModule != null) {
                            LoginActivity.mOAuthLoginModule.logout(LoginActivity.mContext);
                            nickname.setText("로그인을 하세요.");
                            MainActivity.nickname.setText("로그인을 하세요.");
                            profileimage.setImageResource(R.drawable.ic_launcher);
                            MainActivity.profileimage.setImageResource(R.drawable.ic_launcher);
                            MainActivity.profileimage.setEnabled(true);
                            profileimage.setEnabled(true);
                            logout.setEnabled(false);
                        }

                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();

        /*
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                nickname.setText("로그인을 하세요.");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        */
    }

    @Override
    public void onResume() {
        //requestMe();
        /*
        if(LoginActivity.mOAuthLoginModule != null) {
            if(LoginActivity.mOAuthLoginModule.getState(getActivity()).toString().equals("OK")) {
                ProfileFragment.nickname.setText(LoginActivity.nickname);
                MainActivity.nickname.setText(LoginActivity.nickname);
                System.out.println("resume -- "+LoginActivity.nickname);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bitmap = getBitmap(LoginActivity.profile_image);
                        } catch (Exception e) {

                        } finally {
                            if(bitmap != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProfileFragment.profileimage.setImageBitmap(bitmap);
                                        MainActivity.profileimage.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        }*/
        super.onResume();
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

    @OnClick(R.id.btn_invite_kakao)
    void inviteKakao() {
        final KakaoLink kakaoLink;
        try {
            kakaoLink = KakaoLink.getKakaoLink(getActivity());
            final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            kakaoTalkLinkMessageBuilder.addText(getResources().getString(R.string.share_message));
            kakaoTalkLinkMessageBuilder.addImage("http://cisuba.net/uploads/kakao/kakao_banner.jpg", 1000, 1000);
            kakaoTalkLinkMessageBuilder.addWebButton("앱 다운로드하기", "https://play.google.com/store/apps/details?id=com.eastblue.cisuba&hl=ko");
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, getActivity());
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    /*private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {    //나머지 오류
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                //redirectLoginActivity();

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if(result == ErrorCode.CLIENT_ERROR_CODE) {
                    //finish();
                } else {
                    //redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {  //세션이 닫힌 경우
                //redirectLoginActivity();
            }

            @Override
            public void onSuccess(final UserProfile userProfile) {    //성공
                Logger.d("UserProfile : " + userProfile);

                nickname.setText(userProfile.getNickname());

                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bitmap = getBitmap(userProfile.getThumbnailImagePath());
                        } catch (Exception e) {

                        } finally {
                            if(bitmap != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        profileimage.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        }
                    }
                }).start();

            }

            @Override
            public void onNotSignedUp() {   //가입되지 않은 경우
                //showSignup();
            }
        });
    }*/

    private void requestMe() {
        final List<String> propertyKeys = new ArrayList<String>();
        propertyKeys.add("kaccount_email");
        propertyKeys.add("nickname");
        propertyKeys.add("profile_image");
        propertyKeys.add("thumbnail_image");

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                //redirectLoginActivity();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                nickname.setText("로그인을 하세요.");
                MainActivity.nickname.setText("로그인을 하세요.");
                profileimage.setImageResource(R.drawable.ic_launcher);
                MainActivity.profileimage.setImageResource(R.drawable.ic_launcher);
                //redirectLoginActivity();
            }

            @Override
            public void onSuccess(final UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);

                nickname.setText(userProfile.getNickname());
                MainActivity.nickname.setText(userProfile.getNickname());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bitmap = getBitmap(userProfile.getThumbnailImagePath());
                        } catch (Exception e) {

                        } finally {
                            if(bitmap != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        profileimage.setImageBitmap(bitmap);
                                        MainActivity.profileimage.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        }
                    }
                }).start();

            }

            @Override
            public void onNotSignedUp() {
                // showSignup();
            }
        }, propertyKeys, false);
    }

    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try {
            imgUrl = new URL(url);
            connection = (HttpURLConnection)imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            retBitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }
}