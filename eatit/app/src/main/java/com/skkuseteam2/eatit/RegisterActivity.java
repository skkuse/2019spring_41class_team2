package com.skkuseteam2.eatit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    // NAVER Login
    public static OAuthLogin mOAuthLoginModule;
    private static NetworkService networkService;
    private ApplicationController application;
    private String accessToken;
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
                accessToken = mOAuthLoginModule.getAccessToken(context);
//                String refreshToken = mOAuthLoginModule.getRefreshToken(context);
                long expiresAt = mOAuthLoginModule.getExpiresAt(context);
                String tokenType = mOAuthLoginModule.getTokenType(context);

                // 사용자 정보 조회
                application = (ApplicationController)getApplicationContext();

                LoginThread loginThread = new LoginThread();
                loginThread.start();

                // NaverMemberProfile.main(accessToken, application);
                while(loginThread.getState() != Thread.State.TERMINATED)
                    continue;
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

    class LoginThread extends Thread {

        @Override
        public void run() {
            // 프로필 요청 헤더
            String header = "Bearer " + accessToken;
            try {
                // 프로필 요청 접근 주소
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                // 연결 오픈
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", header);
                // 응답코드 받기
                int responseCode = connection.getResponseCode();

                // 읽어올 버퍼
                BufferedReader bufferedReader;
                if (responseCode == 200) { // 정상 호출의 경우
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else { // 에러가 발생한 경우
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }

                String inputLine;
                StringBuffer response = new StringBuffer();
                // 읽어오기
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }

                bufferedReader.close();

                // id를 읽어옴
                final String responseStr = response.toString();
                final int id = new JSONObject(responseStr).getJSONObject("response").getInt("id");

                // User 데이터베이스에 아이디가 존재하면 pass, 존재하지 않으면 회원가입
                // ip, port 연결
                final ApplicationController application = ApplicationController.getInstance();
                application.buildNetworkService("52.78.88.3",8080);
                networkService = ApplicationController.getInstance().getNetworkService();

                Call<User> userCall = networkService.getIdUser(id);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            application.setUserId(id);
                            application.setHasEval(user.getEvaluate());
                            System.out.println("가입된 사용자");
                        } else {
                            Boolean isSuccess = Boolean.FALSE;
                            while (isSuccess == Boolean.FALSE)
                                isSuccess = registerUser(responseStr);
                            application.setUserId(id);
                            System.out.println("사용자 등록 완료");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.i("eatit", "서버 onFailure 에러: " + t.getMessage());
                    }
                });
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static Boolean registerUser(String string) {
        try {
            JSONObject response = new JSONObject(string).getJSONObject("response");

            // User 변수 생성, 값 설정정
            User user = new User();
            user.setId(response.getInt("id"));
            user.setNickname(response.getString("nickname"));
            user.setName(response.getString("name"));
            user.setProfile_image(response.getString("profile_image"));
            user.setEvaluate(Boolean.FALSE);

            System.out.println("id: " + user.getId());
            System.out.println("nickname: " + user.getNickname());
            System.out.println("name: " + user.getName());
            System.out.println("image: " + user.getProfile_image());
            System.out.println("eval: " + user.getEvaluate());

            Call<User> userCall = networkService.post_user(user);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        System.out.println("★등록 완료");
                    } else {
                        int statusCode = response.code();
                        String message = response.toString();
                        Log.i("eatit", "등록안됨: " + message);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("eatit", "서버 onFailure 에러내용: " + t.getMessage());
                }
            });
            return Boolean.TRUE;
        } catch (JSONException e) {
            Log.e("eatIt", "jsonError", e);
        }
        return Boolean.FALSE;
    }
}
