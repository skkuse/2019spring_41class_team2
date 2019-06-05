package com.skkuseteam2.eatit;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.security.cert.TrustAnchor;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import android.widget.Toast;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

import android.graphics.drawable.Drawable;
import android.view.View;

public class NaverMemberProfile {

    private static NetworkService networkService;

    public static void main(String token) {

        final ApplicationController applicationController = ApplicationController.getInstance();

        // 프로필 요청 헤더
        String header = "Bearer " + token;

        AsyncTask<String, Void, Void> asyncTask = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... header) {

                try {
                    // 프로필 요청 접근 주소
                    String apiURL = "https://openapi.naver.com/v1/nid/me";
                    URL url = new URL(apiURL);
                    // 연결 오픈
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Authorization", header[0]);
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
                    ApplicationController application = ApplicationController.getInstance();
                    application.buildNetworkService("52.78.88.3",8080);
                    networkService = ApplicationController.getInstance().getNetworkService();

                    Call<User> userCall = networkService.getIdUser(id);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                applicationController.setUserId(id);
                                System.out.println("가입된 사용자");
                            } else {
                                Boolean isSuccess = Boolean.FALSE;
                                while (isSuccess == Boolean.FALSE)
                                    isSuccess = registerUser(responseStr);
                                applicationController.setUserId(id);
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
                return null;
            }
        };

        asyncTask.execute(header);
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
