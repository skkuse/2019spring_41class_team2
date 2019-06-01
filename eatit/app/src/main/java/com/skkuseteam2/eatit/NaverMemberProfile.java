package com.skkuseteam2.eatit;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import android.widget.Toast;
import java.util.HashMap;

import retrofit2.http.POST;

public class NaverMemberProfile {

    public static void main(String token) {

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
                    /*
                    System.out.println(response.toString());
                    String name = parser(response.toString());
                    System.out.println("name: " + name);
                    */

                    // id를 읽어옴
                    int id = new JSONObject(response.toString()).getJSONObject("response").getInt("id");

                    // User 데이터베이스에 아이디가 존재하면 pass, 존재하지 않으면 회원가입
                } catch (Exception e) {
                    System.out.println(e);
                }

                return null;
            }
        };

        asyncTask.execute(header);
    }

    public static Boolean register(String string) {
        try {
            JSONObject response = new JSONObject(string).getJSONObject("response");

        } catch (JSONException e) {
            Log.e("eatIt", "jsonError", e);
        }

        return Boolean.FALSE;
    }
}
