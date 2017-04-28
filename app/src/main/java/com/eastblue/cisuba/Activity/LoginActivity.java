package com.eastblue.cisuba.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eastblue.cisuba.Fragment.ProfileFragment;
import com.eastblue.cisuba.Kakao.KakaoLoginControl;
import com.eastblue.cisuba.Manager.NetworkManager;
import com.eastblue.cisuba.Model.CodeModel;
import com.eastblue.cisuba.Model.UserModel;
import com.eastblue.cisuba.Network.User;
import com.eastblue.cisuba.R;
import com.eastblue.cisuba.Util.HttpUtil;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
//import com.kakao.usermgmt.response.model.User;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.StoryProtocol;
import com.kakao.util.helper.TalkProtocol;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler;
import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {// implements View.OnClickListener {

    private ISessionCallback callback;

    private static final String TAG = "OAuthSampleActivity";

    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "CVx3JW6nJ6ZFulFUzCYj";
    private static String OAUTH_CLIENT_SECRET = "aTQXyVrHVK";
    private static String OAUTH_CLIENT_NAME = "네이버로 로그인하기";


    public static OAuthLogin mOAuthLoginModule;
    public static Context mContext;

    private OAuthLoginButton mOAuthLoginButton;

    public static String email = "";
    public static String nickname = "";
    String enc_id = "";
    public static String profile_image = "";
    String age = "";
    String gender = "";
    String id = "";
    public static String name = "";
    String birthday = "";

    String accessToken = "";
    String tokenType;

    Bitmap bitmap;
    private ImageButton close;

    private ViewGroup naverLoginButton;
    private TextView tvnaver;
    private ImageView ivnaver;

    private ViewGroup kakaoLoginButton;
    private TextView tvkakao;
    private ImageView ivkakao;

    TextView join;

    EditText et_eamil, et_password;

    public static String login_user_name = "";

    public static boolean ISLOGIN = false;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    String loginId, loginPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        /*
        setting = getSharedPreferences("login_setting",MODE_PRIVATE);
        editor = setting.edit();

        loginId = setting.getString("inputId",null);
        loginPwd = setting.getString("inputPwd",null);
        */

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        //InitializeNaverAPI();

        /*et_eamil = (EditText) findViewById(R.id.input_email);
        et_password = (EditText) findViewById(R.id.input_pass);*/


        tvnaver = (TextView) findViewById(R.id.naver_text);
        ivnaver = (ImageView) findViewById(R.id.naver_symbol);

        naverLoginButton = (ViewGroup) findViewById(R.id.naver_login);
        naverLoginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ivnaver.setImageResource(R.drawable.ic_naver_selected);
                        naverLoginButton.setBackgroundResource(R.drawable.box_selected);
                        tvnaver.setTextColor(Color.parseColor("#1a9eb8"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        ivnaver.setImageResource(R.drawable.ic_naver);
                        naverLoginButton.setBackgroundResource(R.drawable.box);
                        tvnaver.setTextColor(Color.BLACK);

                        break;
                    }
                }
                return false;
            }
        });

        tvkakao = (TextView) findViewById(R.id.kakao_text);
        ivkakao = (ImageView) findViewById(R.id.kakao_symbol);

        kakaoLoginButton = (ViewGroup) findViewById(R.id.kakaologin);
        kakaoLoginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ivkakao.setImageResource(R.drawable.ic_kakao_selected);
                        kakaoLoginButton.setBackgroundResource(R.drawable.box_selected);
                        tvkakao.setTextColor(Color.parseColor("#1a9eb8"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        ivkakao.setImageResource(R.drawable.ic_kakao);
                        kakaoLoginButton.setBackgroundResource(R.drawable.box);
                        tvkakao.setTextColor(Color.BLACK);

                        break;
                    }
                }
                return false;
            }
        });

        kakaoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new KakaoLoginControl(LoginActivity.this).call();
                //callback = new SessionCallback();
                //Session.getCurrentSession().addCallback(callback);
                //Session.getCurrentSession().checkAndImplicitOpen();

                requestMe();
            }
        });

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
        initSetting();

        /*close = (ImageButton) findViewById(R.id.login_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
*/
        /*join = (TextView) findViewById(R.id.btn_join);
        join.setText(Html.fromHtml("<u>" + "회원가입" + "</u>"));*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    @OnClick(R.id.login_close)
    void loginclose() {
        finish();
    }

    /*@OnClick(R.id.email_login)
    void login_email() {
        *//*
        String key = "cisuba";
        String en,de;
        System.out.println("email : " + et_eamil.getText().toString());
        System.out.println("pass  : " + et_password.getText().toString());
        try {
            en = Encrypt(et_password.getText().toString(), "hello");
            System.out.println("pass encrypte : " + en);
            en = Encrypt(et_password.getText().toString(), key);
            System.out.println("pass encrypte : " + en);
            de = Decrypt(en,key);
            System.out.println("pass decrypte : " + de);
        } catch (Exception e) {
            e.printStackTrace();
        }
        *//*

        String ID = et_eamil.getText().toString();
        String PW = et_password.getText().toString();
        *//*
        editor.putString("ID", ID);
        editor.putString("PW", PW);
        editor.putBoolean("Auto_Login_enabled", true);
        editor.commit();
        *//*
        System.out.println("login_test");

        HttpUtil.api(User.class).requsetLogin(
                et_eamil.getText().toString(),
                et_password.getText().toString(),
                new Callback<CodeModel>() {
                    @Override
                    public void success(CodeModel codeModel, Response response) {
                        if (codeModel.code.equals("1")) {
                            Toast.makeText(LoginActivity.this, "login", Toast.LENGTH_SHORT).show();
                            *//*System.out.println("login_test user -"+codeModel.user);*//*
                            System.out.println("login_test succeess user_name "+codeModel.user_name);
                            System.out.println("login_test succeess code = " + codeModel.code);
                            System.out.println("login_test succeess message = "+codeModel.message);
                            //finish();
                        } else {
                            System.out.println("login_test fail code = " + codeModel.code);
                            System.out.println("login_test fail message = "+codeModel.message);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.println("login_test failure");
                        System.out.println(error);
                    }
                });
    }*/

   /* @OnClick(R.id.btn_join)
    void toJoin() {
        startActivity(new Intent(this, JoinActivity.class));
    }

    private void requestAccessTokenInfo() {
        AuthService.requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(AccessTokenInfoResponse result) {
                Log.d("login", result.toString());
            }
        });
    }*/

    private void requestInformation() {

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("TAG", "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                String profileUrl = userProfile.getProfileImagePath();
                String userId = String.valueOf(userProfile.getId());
                String userName = userProfile.getNickname();
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    System.out.println(errorResult.toString() + "tttt");
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                    Log.e("UserProfile", userProfile.toString());
                    System.out.println("login success");
                    MainActivity.profileimage.setEnabled(false);
                    ProfileFragment.profileimage.setEnabled(false);
                    ProfileFragment.logout.setEnabled(true);
                    requestMe();
                    finish();
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때


            System.out.println(exception);
        }
    }

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
                ProfileFragment.nickname.setText("로그인을 하세요.");
                MainActivity.nickname.setText("로그인을 하세요.");
                ProfileFragment.profileimage.setImageResource(R.drawable.ic_launcher);
                MainActivity.profileimage.setImageResource(R.drawable.ic_launcher);
                //redirectLoginActivity();
            }

            @Override
            public void onSuccess(final UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);

                ProfileFragment.nickname.setText(userProfile.getNickname());
                MainActivity.nickname.setText(userProfile.getNickname());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bitmap = getBitmap(userProfile.getThumbnailImagePath());
                        } catch (Exception e) {

                        } finally {
                            if (bitmap != null) {
                                runOnUiThread(new Runnable() {
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

            @Override
            public void onNotSignedUp() {
                // showSignup();
            }
        }, propertyKeys, false);
    }


    /**
     * Maver
     */
    /*
    private void InitializeNaverAPI( )
    {
        mOAuthLoginModule = OAuthLogin.getInstance( );
        mOAuthLoginModule.init(
                this,
                "CVx3JW6nJ6ZFulFUzCYj",
                "aTQXyVrHVK",
                "네이버 아이디로 로그인"
        );

        // 네이버 로그인 버튼 리스너 등록
        OAuthLoginButton naverLoginButton = ( OAuthLoginButton ) findViewById( R.id.buttonOAuthLoginImg );
        naverLoginButton.setOAuthLoginHandler( new OAuthLoginHandler( )
        {
            @Override
            public void run(final boolean b )
            {
                if ( b )
                {
                    final String token = mOAuthLoginModule.getAccessToken( LoginActivity.this );
                    new Thread( new Runnable( )
                    {
                        @Override
                        public void run( )
                        {
                            String response = mOAuthLoginModule.requestApi( LoginActivity.this, token, "https://openapi.naver.com/v1/nid/me" );
                            try
                            {
                                JSONObject json = new JSONObject( response );
                                // response 객체에서 원하는 값 얻어오기
                                String email = json.getJSONObject( "response" ).getString( "email" );
                                // 액티비티 이동 등 원하는 함수 호출
                                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //startActivity(intent);
                                new RequestApiTask().execute(); //로그인이 성공하면  네이버에 계정값들을 가져온다.
                                finish();
                            } catch ( JSONException e )
                            {
                                e.printStackTrace( );
                            }
                        }
                    } ).start( );
                }
                else
                {
                }
            }
        } );
    }*/
    private void initSetting() {
        //OAuthLoginButton naverLoginButton = ( OAuthLoginButton ) findViewById( R.id.buttonOAuthLoginImg );
        ViewGroup naverLoginButton = (ViewGroup) findViewById(R.id.naver_login);
        //final Button naverLoginButton = (Button) findViewById(R.id.naver_login);
        naverLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this,
                        mOAuthLoginHandler);
            }
        });
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                accessToken = mOAuthLoginModule.getAccessToken(mContext);
                String refreshToken = mOAuthLoginModule
                        .getRefreshToken(mContext);
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                tokenType = mOAuthLoginModule.getTokenType(mContext);

                Log.d("myLog", "accessToken  " + accessToken);
                Log.d("myLog", "refreshToken  " + refreshToken);
                Log.d("myLog",
                        "String.valueOf(expiresAt)  "
                                + String.valueOf(expiresAt));
                Log.d("myLog", "tokenType  " + tokenType);
                Log.d("myLog",
                        "mOAuthLoginInstance.getState(mContext).toString()  "
                                + mOAuthLoginModule.getState(mContext)
                                .toString());

                new RequestApiTask().execute(); //로그인이 성공하면  네이버에 계정값들을 가져온다.
                finish();
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(
                        mContext).getCode();
                String errorDesc = mOAuthLoginModule
                        .getLastErrorDesc(mContext);
                // Toast.makeText(mContext, "errorCode:" + errorCode +
                // ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();

                Toast.makeText(LoginActivity.this, "로그인이 취소/실패 하였습니다.!",
                        Toast.LENGTH_SHORT).show();
            }
        }

        ;
    };

    private class RequestApiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginModule.getAccessToken(mContext);
            Pasingversiondata(mOAuthLoginModule.requestApi(mContext, at, url));

            return null;
        }

        protected void onPostExecute(Void content) {
            Log.d("myLog", "email " + email);
            Log.d("myLog", "name " + name);
            Log.d("myLog", "nickname " + nickname);

            ProfileFragment.nickname.setText(name);
            MainActivity.nickname.setText(name);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bitmap = getBitmap(profile_image);
                    } catch (Exception e) {

                    } finally {
                        if (bitmap != null) {
                            runOnUiThread(new Runnable() {
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

            MainActivity.profileimage.setEnabled(false);
            ProfileFragment.profileimage.setEnabled(false);
            ProfileFragment.logout.setEnabled(true);

            if (email == null) {
                Toast.makeText(LoginActivity.this,
                        "로그인 실패하였습니다.  잠시후 다시 시도해 주세요!!", Toast.LENGTH_SHORT)
                        .show();
            } else {

            }
        }

        private void Pasingversiondata(String data) { // xml 파싱
            String f_array[] = new String[9];

            try {
                XmlPullParserFactory parserCreator = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                InputStream input = new ByteArrayInputStream(
                        data.getBytes("UTF-8"));
                parser.setInput(input, "UTF-8");

                int parserEvent = parser.getEventType();
                String tag;
                boolean inText = false;
                boolean lastMatTag = false;

                int colIdx = 0;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();
                            if (tag.compareTo("xml") == 0) {
                                inText = false;
                            } else if (tag.compareTo("data") == 0) {
                                inText = false;
                            } else if (tag.compareTo("result") == 0) {
                                inText = false;
                            } else if (tag.compareTo("resultcode") == 0) {
                                inText = false;
                            } else if (tag.compareTo("message") == 0) {
                                inText = false;
                            } else if (tag.compareTo("response") == 0) {
                                inText = false;
                            } else {
                                inText = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            tag = parser.getName();
                            if (inText) {
                                if (parser.getText() == null) {
                                    f_array[colIdx] = "";
                                } else {
                                    f_array[colIdx] = parser.getText().trim();
                                }

                                colIdx++;
                            }
                            inText = false;
                            break;
                        case XmlPullParser.END_TAG:
                            tag = parser.getName();
                            inText = false;
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                Log.e("dd", "Error in network call", e);
            }
            email = f_array[7];
            nickname = f_array[0];
            enc_id = f_array[1];
            profile_image = f_array[2];
            age = f_array[3];
            gender = f_array[4];
            id = f_array[5];
            name = f_array[6];
            birthday = f_array[8];
            System.out.println("email:" + email);
            System.out.println("nickname:" + nickname);
            System.out.println("enc_id:" + enc_id);
            System.out.println("profile_image:" + profile_image);
            System.out.println("age:" + age);
            System.out.println("gender:" + gender);
            System.out.println("id:" + id);
            System.out.println("name:" + name);
            System.out.println("birthday:" + birthday);
        }
    }

    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try {
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            retBitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }


    void getItem(String email) {
        System.out.println("login_test");

        HttpUtil.api(User.class).get_user_inform(email, new Callback<UserModel>() {
            @Override
            public void success(UserModel userModel, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public static String Decrypt(String text, String key) throws Exception

    {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] keyBytes = new byte[16];

        byte[] b = key.getBytes("UTF-8");

        int len = b.length;

        if (len > keyBytes.length) len = keyBytes.length;

        System.arraycopy(b, 0, keyBytes, 0, len);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);


//               BASE64Decoder decoder = new BASE64Decoder();

//               Base64.decode(input, flags)

//               byte [] results = cipher.doFinal(decoder.decodeBuffer(text));

        // BASE64Decoder decoder = new BASE64Decoder();

        // Base64.decode(input, flags)

        byte[] results = cipher.doFinal(Base64.decode(text, 0));


        return new String(results, "UTF-8");

    }


    public static String Encrypt(String text, String key) throws Exception

    {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] keyBytes = new byte[16];

        byte[] b = key.getBytes("UTF-8");

        int len = b.length;

        if (len > keyBytes.length) len = keyBytes.length;

        System.arraycopy(b, 0, keyBytes, 0, len);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);


        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

//               BASE64Encoder encoder = new BASE64Encoder();

//               return encoder.encode(results);


        return Base64.encodeToString(results, 0);

    }
}
