package com.skkuseteam2.eatit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

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
                ApplicationController application = (ApplicationController)getApplicationContext();
                NaverMemberProfile.main(accessToken, application);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        setNaver();
    }
}
