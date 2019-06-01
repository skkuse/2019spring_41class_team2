package com.skkuseteam2.eatit;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // NAVER Login
    public static OAuthLogin mOAuthLoginModule;
    OAuthLoginButton mOAuthLoginButton;

    private void setNaver() {
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(this, "fEQ965DGvII0C11vmnrX", "PsTfgCaRCA", "eat it!");

        mOAuthLoginButton = findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            Context context = (Context) getApplicationContext();
            if (success) {
                // 토큰 발급
                String accessToken = mOAuthLoginModule.getAccessToken(context);
                String refreshToken = mOAuthLoginModule.getRefreshToken(context);
                long expiresAt = mOAuthLoginModule.getExpiresAt(context);
                String tokenType = mOAuthLoginModule.getTokenType(context);

                // 사용자 정보 조회
                //new RequestApiTask().execute(context, accessToken);
                NaverMemberProfile.main(accessToken);
                //Map<String, String> userInfoMap = requestNaverUserInfo(mOAuthLoginModule.requestApi(context, accessToken, "https://apis.naver.com/nidlogin/nid/getHashId_v2.xml"));
                // Toast.makeText(context, userInfoMap.get("nickname"), Toast.LENGTH_LONG).show();

                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                setResult(RESULT_OK, outIntent);
                finish();
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(context).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(context);
                Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };
/*
    private Map<String, String> requestNaverUserInfo(String data) {
        String f_array[] = new String[9];

        try {
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            InputStream input = new ByteArrayInputStream(data.getBytes("UTF-8"));
            parser.setInput(input, "UTF-8");

            int parserEvent = parser.getEventType();
            String tag;
            boolean inText = false;
            boolean lastMatTag = false;

            int colIdx = 0;

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                Log.i("dd", "실행" + parserEvent);
                switch (parserEvent) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i("dd", "스타트");
                        tag = parser.getName();
                        Log.i("dd", "getname: " + tag);
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
                        Log.i("dd", "텍스트");
                        tag = parser.getName();
                        if (inText) {
                            if (parser.getText() == null)
                                f_array[colIdx] = "";
                            else
                                f_array[colIdx] = parser.getText().trim();

                            colIdx++;
                        }
                        inText = false;
                        break;
                    case XmlPullParser.END_TAG:
                        Log.i("dd", "엔드");
                        tag = parser.getName();
                        inText = false;
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e){
            Log.e("dd", "Error in network call", e);
        }
        Toast.makeText(getApplicationContext(), "0번: " + f_array[0], Toast.LENGTH_LONG).show();
        Map<String , String > resultMap = new HashMap<>();
        resultMap.put("nickname", f_array[0]);
        resultMap.put("enc_id", f_array[1]);
        resultMap.put("profile_image", f_array[2]);
        resultMap.put("id", f_array[3]);
        resultMap.put("name", f_array[4]);
        return resultMap;
    }*/
/*
    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {

            Context context = getApplicationContext();
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = mOAuthLoginModule.getAccessToken(context);
            //return mOAuthLoginModule.requestApi(context, at, url);
            return "hello";
        }
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        setNaver();
/*
        Button btnMain = (Button) findViewById(R.id.btnMain);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
    }
}
